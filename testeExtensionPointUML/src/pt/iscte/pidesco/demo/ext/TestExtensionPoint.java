package pt.iscte.pidesco.demo.ext;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import UICustomization.WidgetProperties;
import interfaces.ColorScheme;
import interfaces.PackageViewServices;

public class TestExtensionPoint implements ColorScheme {

	@Override
	public WidgetProperties getWidgetProperties() {

		WidgetProperties widget = new WidgetProperties(4);
		widget.addPackage("testPackage");
		widget.addPackage("ola");
		widget.setSize(400, 200);
		return widget;
	}

}
