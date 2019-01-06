package HelperClasses;

import java.util.ArrayList;
import java.util.HashMap;

public class PackageProperties {
	int level;
	int branch;
	String packageName;

	ArrayList<PackageProperties> dependencies = new ArrayList<>();
	ArrayList<ClassProperties> classes = new ArrayList<>();

	public PackageProperties(int level, int branch, String packageName) {
		this.level = level;
		this.branch = branch;
		this.packageName = packageName;
	}

	public int getLevel() {
		return level;
	}

	public int getBranch() {
		return branch;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getSimplePackageName() {
		int level = (int) packageName.chars().filter(character -> character == '.').count();
		if (level != 0) {
			return packageName.substring(packageName.lastIndexOf(".") + 1);
		} else {
			return packageName;
		}
	}

	public void addDependency(PackageProperties pack) {
		boolean add = true;

		for (PackageProperties p : dependencies) {
			if (pack.getSimplePackageName().equals(p.getSimplePackageName())) {
				add = false;
			}
		}
		if (add)
			dependencies.add(pack);
	}

	public void addClass(String classPath) {
		classes.add(new ClassProperties(classPath));
	}

	public ArrayList<ClassProperties> getClassList() {
		return classes;
	}

	public ArrayList<PackageProperties> getDependencyList() {

		return dependencies;

	}
}
