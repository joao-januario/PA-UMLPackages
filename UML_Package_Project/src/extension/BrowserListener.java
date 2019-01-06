package extension;

import project.TreeView;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;

public class BrowserListener extends ProjectBrowserListener.Adapter{
	
	@Override
	public void doubleClick(SourceElement element) {

		TreeView treeView = TreeView.getInstance();
		
		if (treeView!=null) {
		treeView.highlightNode(element.toString());}
		
	}
}
