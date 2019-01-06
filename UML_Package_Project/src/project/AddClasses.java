package project;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class AddClasses extends ASTVisitor{
	

	PackageTree tree = PackageTree.getInstance();
	
	@Override
	public boolean visit(TypeDeclaration node) {
		String classPath = tree.stackName() + "." + node.getName();
		classPath= classPath.replace(".","\\" );
		classPath= classPath+".java";
		
		tree.addClassToPackage(classPath);
		
		return true;
	}

}
