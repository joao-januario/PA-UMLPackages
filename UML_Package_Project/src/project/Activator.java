package project;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Composite;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import HelperClasses.ClassProperties;
import HelperClasses.PackageProperties;
import Service.PackageViewServiceImpl;
import extension.BrowserListener;
import interfaces.ColorScheme;
import interfaces.PackageViewServices;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static Activator instance;
	private String path;
	private Set<SWTEventListener> listeners;
	
	PackageTree treeCreator = PackageTree.getInstance();
	JavaEditorServices javaServ;
	
	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		instance = this;
		Activator.context = bundleContext;
		listeners = new HashSet<SWTEventListener>();
		context.registerService(PackageViewServices.class, new PackageViewServiceImpl(), null);
		
		
		ServiceReference<JavaEditorServices> sv = context.getServiceReference(JavaEditorServices.class);
		javaServ = context.getService(sv);

		ServiceReference<ProjectBrowserServices> pb = context.getServiceReference(ProjectBrowserServices.class);
		ProjectBrowserServices projBrowser = context.getService(pb);
		projBrowser.addListener(new BrowserListener());
		PackageElement ele = projBrowser.getRootPackage();

		
		path=ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
		path = path.replace("/", "\\") + "\\";
		buildTreeVisitor(ele);
		checkDependencies();
		
		
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	
	public static Activator getInstance() {
		return instance;
	}
	
	public void addListener(SWTEventListener l) {
		listeners.add(l);
	}

	public void removeListener(SWTEventListener l) {
		listeners.remove(l);
	}

	public Set<SWTEventListener> getListeners() {
		// TODO Auto-generated method stub
		return listeners;
	}

	
	


	
	private void buildTreeVisitor(PackageElement ele) {

		for (SourceElement s: ele.getChildren()) {
			
			if (s.isPackage()) {
				treeCreator.push(s.getName());
				PackageElement dir = (PackageElement) s;
				buildTreeVisitor(dir);
				treeCreator.pop();
				

			} else {
					String filePath = path + treeCreator.stackName();
					filePath=filePath.replace(".", "\\");
					filePath = filePath + "\\" + s.getName();
					File f = new File(filePath);
					AddClasses builder = new AddClasses();
					javaServ.parseFile(f, builder);
			
				
			}
		}


	}
	
	private void checkDependencies() {
		
		//Adicionar as dependencias, tens o path das classes no package, é so verificar se extende passando no checkClassDependencies e ir ver a que package pertence essa classe iterando pela lista de classes de cada package
		
		for (PackageProperties pack: treeCreator.getPackages()) {
			DependencyChecker checker = new DependencyChecker(pack);
			for (ClassProperties s: pack.getClassList()) {
				String filePath=path+s.getPath();
				File f = new File(filePath);
				javaServ.parseFile(f, checker);
			}
		}
		
		
	}
	
	

}
