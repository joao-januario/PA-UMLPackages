package interfaces;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.internal.SWTEventListener;

import HelperClasses.ClassProperties;
import HelperClasses.PackageProperties;

public interface PackageViewServices {

	/**
	 * @param SelectionListener you want to add to the view, SelectionAdapter is one example of a class that implements SelectionListener
	 */
	void addListener(SWTEventListener listener);
	
	/**
	 * @param listener you want to remove
	 */
	void removeListener(SWTEventListener listener);
	
	/**
	 * @param packageName is the simple name of the package you want to get dependencies of, it's advised to build a listener to return the selected package in the view
	 * @return list of packages that the parameter package is dependent of, in case of no dependencies being found or the parameter package not existing an empty list is returned
	 */
	ArrayList<PackageProperties> getDependencies(String packageName);
		
	/**
	 * @param packageName is the simple name of the package you want to get classes of, it's advised to build a listener to return the selected package in the view
	 * @return list of classes of the parameter package, in case of no classes being found or the parameter package not existing an empty list is returned
	 */
	ArrayList<ClassProperties> getPackageClasses(String packageName);
	
	
	/**
	 * @param packageName is the simple name of the package you want to get instance of, it's advised to build a listener to return the selected package in the view
	 * @return PackageProperties of parameter package, in case of no package being found with the parameter name, null is returned
	 * PackageProperties contains information like the level of the package, full name, and others.
	 * 
	 */
	PackageProperties getPackageObject(String packageName);
	
	/**
	 * @return the list of PackageProperties
	 */
	ArrayList<PackageProperties> getPackages();
	
	/**
	 * @return the list of highlited packages in the tree view, if no package is highlited and empty list will be returned
	 */
	ArrayList<PackageProperties> getHighlitedPackages();
}
