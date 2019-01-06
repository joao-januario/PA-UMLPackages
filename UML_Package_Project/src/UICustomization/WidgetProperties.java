package UICustomization;

import java.util.ArrayList;

import org.eclipse.swt.graphics.Image;

public class WidgetProperties {

	ArrayList<String> packageNames = new ArrayList<String>();
	int color;
	double[] dimensions = new double[2];
	Image image=null;

	public WidgetProperties(int color) {
		this.color = color;
	}

	public void addPackage(String simplePackageName) {
		packageNames.add(simplePackageName);
	}
	
	public int getColor() {
		return color;
		
	}
	
	public boolean checkIfContains(String packageName) {
		for (String s: packageNames) {
			if (s.equals(packageName)) {
				return true;
			}else {
				return false;
			}
		}
		return false;
	}
	
	public ArrayList<String> getPackages(){
		return packageNames;
	}
	
	public void setSize(int width,int heigth) {
		
		dimensions[0]=width;
		dimensions[0]=heigth;
		
	}
	
	public double[] getDimensions() {
		return dimensions;
	}
	
	public void setImage(Image image) {
		this.image=image;
	}
	
	public Image getImage() {
		return image;
	}
}
