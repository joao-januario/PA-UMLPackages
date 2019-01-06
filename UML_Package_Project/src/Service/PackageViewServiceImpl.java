package Service;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.internal.SWTEventListener;

import HelperClasses.ClassProperties;
import HelperClasses.PackageProperties;
import interfaces.PackageViewServices;
import project.Activator;
import project.PackageTree;
import project.TreeView;

public class PackageViewServiceImpl implements PackageViewServices{

	@Override
	public void addListener(SWTEventListener listener) {
		Activator.getInstance().addListener(listener);
	}

	@Override
	public void removeListener(SWTEventListener listener) {
		Activator.getInstance().removeListener(listener);
		
	}

	@Override
	public ArrayList<PackageProperties> getDependencies(String packageName) {
		return PackageTree.getInstance().getPackageDependencies(packageName);
		
	}

	@Override
	public ArrayList<ClassProperties> getPackageClasses(String packageName) {
		return  PackageTree.getInstance().getPackageContent(packageName);
			
	}

	@Override
	public PackageProperties getPackageObject(String packageName) {
		 return PackageTree.getInstance().getPackage(packageName);
	}

	@Override
	public ArrayList<PackageProperties> getPackages() {
		return PackageTree.getInstance().getPackages();
	}
	
	@Override
	public ArrayList<PackageProperties> getHighlitedPackages(){
		return TreeView.getInstance().getHighlitedPackages();
	}
	

}
