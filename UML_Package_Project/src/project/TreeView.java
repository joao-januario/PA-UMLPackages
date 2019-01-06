package project;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.GestureListener;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TouchListener;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import HelperClasses.ClassProperties;
import HelperClasses.PackageProperties;
import Service.PackageViewServiceImpl;
import UICustomization.WidgetProperties;
import interfaces.ColorScheme;
import interfaces.PackageViewServices;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class TreeView implements PidescoView {

	static TreeView instance = null;
	PackageTree tree = PackageTree.getInstance();
	ArrayList<GraphNode> nodes = new ArrayList<>();
	ArrayList<Integer> packagePositions = new ArrayList<Integer>();

	public static TreeView getInstance() {
		return instance;
	}

	public TreeView() {
		instance = this;
	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		BundleContext context = Activator.getContext();

		ArrayList<PackageProperties> packages = tree.getPackages();
		populatePositions();

		Graph graph = new Graph(viewArea, SWT.NONE);

		for (SWTEventListener packageListener : Activator.getInstance().getListeners()) {
			addSWTEventListener(packageListener, graph);
		}

		for (PackageProperties pack : packages) {
			if (pack.getLevel() > 0) {
				addNode(pack, graph);
			}
		}
		for (PackageProperties pack : packages) {
			for (PackageProperties p : pack.getDependencyList()) {
				GraphNode original = null;
				GraphNode dependency = null;
				for (GraphNode node : nodes) {
					if (node.getText().equals(pack.getSimplePackageName())) {
						original = node;
					}
					if (node.getText().equals(p.getSimplePackageName())) {
						dependency = node;
					}

				}
				new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, original, dependency);
			}
		}

	}

	private void addNode(PackageProperties packag, Graph graph) {
		boolean add = true;
		if (packag.getPackageName().startsWith("bin")) {
			add = false;
		}
		if (add) {

			ColorScheme widgetProperties = null;

			packagePositions.set(packag.getLevel(), packagePositions.get(packag.getLevel()) + 100);

			GraphNode graphNode = new GraphNode(graph, SWT.NONE, packag.getSimplePackageName());

			graphNode.setLocation(packagePositions.get(packag.getLevel()), packag.getLevel() * 100);

			IExtensionRegistry reg = Platform.getExtensionRegistry();
			IConfigurationElement[] elements = reg
					.getConfigurationElementsFor("pt.iscte.pidesco.umlPackage.color_scheme");
			System.out.println(elements.length);
			for (IConfigurationElement e : elements) {
				try {
					widgetProperties = (ColorScheme) e.createExecutableExtension("class");
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			if (widgetProperties != null) {
				ArrayList<String> packageNames = widgetProperties.getWidgetProperties().getPackages();

				WidgetProperties properties = widgetProperties.getWidgetProperties();

				if (packageNames.contains(packag.getSimplePackageName())) {

					graphNode.setBorderColor(graphNode.getDisplay().getSystemColor(properties.getColor()));
					double[] dimensions = properties.getDimensions();
					if (dimensions[0] > 0 && dimensions[1] > 0) {
						graphNode.setSize(dimensions[0], dimensions[1]);
					}
					if (properties.getImage() != null) {
						graphNode.setImage(properties.getImage());
					}
				} else {
					DefaultColorScheme colorScheme = new DefaultColorScheme();
					graphNode.setBorderColor(
							graphNode.getDisplay().getSystemColor(colorScheme.getWidgetProperties().getColor()));

				}
			} else {
				DefaultColorScheme colorScheme = new DefaultColorScheme();
				graphNode.setBorderColor(
						graphNode.getDisplay().getSystemColor(colorScheme.getWidgetProperties().getColor()));

			}
			nodes.add(graphNode);
		}
	}

	private void populatePositions() {
		int deepestLevel = 0;
		for (PackageProperties p : tree.getPackages()) {
			if (p.getLevel() > deepestLevel) {
				deepestLevel = p.getLevel();
			}
		}
		for (int i = 0; i <= deepestLevel; i++) {
			packagePositions.add(0);
		}
	}

	public void highlightNode(String packageName) {
		for (GraphNode node : nodes) {
			node.unhighlight();
		}
		for (GraphNode node : nodes) {
			if (node.getText().equals(packageName)) {
				node.highlight();
			}
		}
	}

	private void addSWTEventListener(SWTEventListener packageListener, Graph graph) {
		if (packageListener instanceof SelectionListener) {
			graph.addSelectionListener((SelectionListener) packageListener);
		}
		if (packageListener instanceof MouseWheelListener) {
			graph.addMouseWheelListener((MouseWheelListener) packageListener);
		}
		if (packageListener instanceof MouseListener) {
			graph.addMouseListener((MouseListener) packageListener);
		}
		if (packageListener instanceof ControlListener) {
			graph.addControlListener((ControlListener) packageListener);
		}
		if (packageListener instanceof DisposeListener) {
			graph.addDisposeListener((DisposeListener) packageListener);
		}
		if (packageListener instanceof DragDetectListener) {
			graph.addDragDetectListener((DragDetectListener) packageListener);
		}
		if (packageListener instanceof FocusListener) {
			graph.addFocusListener((FocusListener) packageListener);
		}
		if (packageListener instanceof GestureListener) {
			graph.addGestureListener((GestureListener) packageListener);
		}
		if (packageListener instanceof KeyListener) {
			graph.addKeyListener((KeyListener) packageListener);
		}
		if (packageListener instanceof HelpListener) {
			graph.addHelpListener((HelpListener) packageListener);
		}
		if (packageListener instanceof MenuDetectListener) {
			graph.addMenuDetectListener((MenuDetectListener) packageListener);
		}
		if (packageListener instanceof MouseMoveListener) {
			graph.addMouseMoveListener((MouseMoveListener) packageListener);
		}
		if (packageListener instanceof MouseTrackListener) {
			graph.addMouseTrackListener((MouseTrackListener) packageListener);
		}
		if (packageListener instanceof PaintListener) {
			graph.addPaintListener((PaintListener) packageListener);
		}
		if (packageListener instanceof TraverseListener) {
			graph.addTraverseListener((TraverseListener) packageListener);
		}
		if (packageListener instanceof TouchListener) {
			graph.addTouchListener((TouchListener) packageListener);
		}

	}
	
	
	public ArrayList<PackageProperties> getHighlitedPackages() {
		ArrayList<PackageProperties> packages = new ArrayList<PackageProperties>();
		
		for (GraphNode node: nodes) {
			if (node.isSelected()) {
				packages.add(tree.getPackage(node.getText()));
			}
		}
		
		return packages;
	}

	public class DefaultColorScheme implements ColorScheme {

		@Override
		public WidgetProperties getWidgetProperties() {

			return new WidgetProperties(6);
		}
	}



}
