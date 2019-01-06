package testservice;

import interfaces.PackageViewServices;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import HelperClasses.ClassProperties;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		
		
		ServiceReference<PackageViewServices> serviceReference = context.getServiceReference(PackageViewServices.class);
		if (serviceReference!=null) {
			PackageViewServices service = (PackageViewServices) context.getService(serviceReference);
			
			service.addListener(new SelectionAdapter() {
	            @Override
	            public void widgetSelected(SelectionEvent e) {
	            	if (e.item!=null) {
	            	String item = e.item.toString();
	                ArrayList<ClassProperties> classes = service.getPackageClasses(item.substring(item.indexOf(" ")+1));
	                
	                for (ClassProperties c: classes) {
	                	System.out.println(c.getName());
	                }
	            	}
	            }}
					);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
