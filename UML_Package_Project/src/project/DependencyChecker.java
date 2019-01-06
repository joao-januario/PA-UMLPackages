package project;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import HelperClasses.PackageProperties;


public class DependencyChecker extends ASTVisitor{
	
	PackageTree tree = PackageTree.getInstance();
	PackageProperties pack;
	
	public DependencyChecker(PackageProperties pack) {
		this.pack=pack;
	}
	
	/**
	 * Given any node of the AST, returns the source code line where it was parsed.
	 */
	private static int sourceLine(ASTNode node) {
		return ((CompilationUnit) node.getRoot()).getLineNumber(node.getStartPosition());
	}

	
	@Override
	public boolean visit(TypeDeclaration node) {
		
	
		String header = node.getParent().toString();
		
		for (String s: header.split("\n")) {
			if (s.startsWith("import")) {
				tree.checkDependencies(pack, s.substring(s.indexOf(" ")+1,s.lastIndexOf(".")));
			}
		}
		
		
		return true;
	}
	
	
	

}
