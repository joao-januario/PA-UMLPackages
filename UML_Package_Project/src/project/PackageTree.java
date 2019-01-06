package project;

import java.util.ArrayList;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import HelperClasses.ClassProperties;
import HelperClasses.PackageProperties;

public class PackageTree {
	private static PackageTree tree = null;

	private PackageTree() {

	}

	public static PackageTree getInstance() {
		if (tree == null) {
			tree = new PackageTree();
		}
		return tree;

	}

	private ArrayList<String> stack = new ArrayList<>();
	ArrayList<PackageProperties> packages = new ArrayList<PackageProperties>();
	int branch = -1;

	public void push(String e) {
		if (!isDefault()) {
			stack.add(e);

		} else {
			branch++;
			stack.add(e);
		}

		packages.add(createLeaf());
	}

	public void pop() {
		stack.remove(stack.size() - 1);

	}

	private PackageProperties createLeaf() {
		String name = stackName();
		int level = (int) name.chars().filter(character -> character == '.').count();
		PackageProperties leaf = new PackageProperties(level, branch, name);
		return leaf;
	}

	public boolean isDefault() {
		return stack.isEmpty();
	}

	public ArrayList<PackageProperties> getPackages() {
		return packages;
	}

	public void resetPackages() {
		stack.clear();
		packages.clear();
	}

	public void checkDependencies(PackageProperties pack, String dependecy) {

		if (belongsToProject(dependecy)) {

			PackageProperties dependencyPackage = null;

			for (PackageProperties l : packages) {
				String comparison = "";
				if (l.getLevel() > 0) {
					comparison = l.getPackageName().substring(l.getPackageName().indexOf(".") + 1);
				}
				if (comparison.equals(dependecy)) {
					dependencyPackage = l;
					break;
				}

			}

			if (dependencyPackage != null) {
				for (PackageProperties l : packages) {
					if (l.equals(pack)) {
						l.addDependency(dependencyPackage);
					}
				}
			}
		}
	}

	public void addClassToPackage(String classPath) {
		for (PackageProperties packag : packages) {
			if (packag.getPackageName().equals(stackName())) {
				packag.addClass(classPath);
				break;
			}
		}
	}

	public String stackName() {
		return String.join(".", stack);
	}

	private boolean belongsToProject(String dependency) {
		String rootPackageName = dependency;
		if (dependency.contains(".")) {
			rootPackageName = dependency.substring(0, dependency.indexOf("."));
		}
		for (PackageProperties l : packages) {
			if (l.getLevel() == 1 && l.getSimplePackageName().equals(rootPackageName)) {
				return true;
			}
		}
		return false;

	}

	public ArrayList<ClassProperties> getPackageContent(String simplePackageName) {
		ArrayList<ClassProperties> emptyList = new ArrayList<>();
		for (PackageProperties packag : packages) {
			if (packag.getSimplePackageName().equals(simplePackageName) && !packag.getPackageName().startsWith("bin")) {
				return packag.getClassList();
			}
		}
		return emptyList;
	}
	
	public ArrayList<PackageProperties> getPackageDependencies(String simplePackageName){
		ArrayList<PackageProperties> emptyList = new ArrayList<>();
		for (PackageProperties packag : packages) {
			if (packag.getSimplePackageName().equals(simplePackageName) && !packag.getPackageName().startsWith("bin")) {
				return packag.getDependencyList();
			}
		}
		return emptyList;
	}
	
	public PackageProperties getPackage(String simplePackageName) {
		for (PackageProperties packag : packages) {
			if (packag.getSimplePackageName().equals(simplePackageName) && !packag.getPackageName().startsWith("bin")) {
				return packag;
			}
		}
		return null;
	}

}
