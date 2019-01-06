package HelperClasses;

public class ClassProperties {

	String path;
	
	public ClassProperties(String path) {
		this.path=path;
	}
	
	public String getSimpleName() {
		String name = getName();
		String simpleName = name.substring(0, name.indexOf("."));
		return simpleName;
	}
	
	public String getName() {
		
		String name = path.substring(path.lastIndexOf("\\")+1);
		return name;
	}
	
	public String getPath() {
		return path;
	}
}
