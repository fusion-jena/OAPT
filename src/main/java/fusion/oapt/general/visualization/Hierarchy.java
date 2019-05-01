
package fusion.oapt.general.visualization;


import fusion.oapt.general.gui.HierarchyWindows;
import fusion.oapt.general.visualization.HierarchyFrame;



public class Hierarchy
{
	protected static  HierarchyFrame HierFrame;
    
 public static   void Run (){

	// MainFrame mainframe = null;
	 HierarchyWindows HierFrame = new HierarchyWindows();
	 
//	 HierFrame = new HierarchyFrame(mainframe);

	 HierFrame.showHierarchy(0); //0 means show the first module
//     HierFrame.setVisible(true);


	 
}

     

}
