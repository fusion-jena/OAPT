
package fusion.oapt.algorithm.partitioner.SeeCOnt.Findk;


import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.general.gui.MainFrame;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.general.gui.SelectCHWindows;
import fusion.oapt.model.NodeList;





public class SelectCH
{
	protected static SelectCHWindows SelectCHWindows;
	ModelBuild MB;
	
	public SelectCH(ModelBuild MB)
	{
		this.MB=MB;
	}
	
    
 public   void Run (){
	 String ModuleName = null;
	 String ontName =MB.ontologyName;
	 String tempDir = MB.wd;//It is equal to "./temp/" 
			 	 
	 MainFrame mainframe = null;
//	 SelectCHWindows = new SelectCHWindows(mainframe);
	 SelectCHWindows SelectCHWindows = new SelectCHWindows();
		 
	 RunSelectCH();
	 SelectCHWindows.Show();
//	 SelectCHWindows.setVisible(true);	 
	 
}
private  void RunSelectCH(){
	SelectCHWindows.ListArrayCH.clear();
		
//	 String CHs = FindOptimalClusterIntractive.CHset[0];
	  String CHs = null;
	  NodeList CHset=FindOptimalClusterIntractive.entitiesCH;
	  for (int i=0;i<CHset.size();i++){
		  if (CHset.get(i) != null) {
			  
 			String s = MB.entities.get(MB.findIndex(CHset.get(i).toString())).getLocalName(); //new:samira
     		if (s == null){
     			if (CHs != null){
     				CHs = CHs + " & " + CHset.get(i).toString();
     			}else{
     				CHs = CHset.get(i).toString();
     			}
     			
     		}else{
     			if (CHs != null ){
//     				CHs = CHs+ " & "+ s.get(0);
     				CHs = CHs + " & " + s ;
     			}else{
     				CHs = s ;
     			}
     		}
		  }
	  }
	  
	  SelectCHWindows.CHLabel.setText(CHs); //it should have scroll
	  SelectCHWindows.NumCHLabel.setText(Coordinator.KNumCH+" ");
	  
	  
	  //show Tree
	  ShowTree.ShowTreeCalculating(MB);
	  
	  //JOptionPane.showMessageDialog(null, "The optimal number of clusters for your ontology is  "+ optimalNumCH + "\n Your Cluster Heads are: \n"+ CHs, "Help", JOptionPane.INFORMATION_MESSAGE);
	  
	   	String info = null;
	   
       	for (int in=0; in<FindOptimalClusterIntractive.SortedNameOnt.length; in++){
       		//info = info + " , " + BuildModel.SortedNameOnt[in]; //should we show the value of rank function
       		//if (BuildModel.SortedNameOnt[in] != null) SelectCHWindows.ListArrayCH.addElement(BuildModel.SortedNameOnt[in]); //+score#
       		//if (BuildModel.SortedNameOnt[in] != null) SelectCHWindows.ListArrayCH.addElement(BuildModel.entities.get(BuildModel.findIndex(BuildModel.SortedNameOnt[in])).getLabel().toString());
       		
       		if (FindOptimalClusterIntractive.SortedNameOnt[in] != null) {
       			int a= MB.findIndex(FindOptimalClusterIntractive.SortedNameOnt[in]);
       			
//       			ArrayList<String> s= BuildModel.entities.get(a).getLabel();
       			String s= MB.entities.get(a).getLocalName();
	       		if (s == null){
	       			SelectCHWindows.ListArrayCH.addElement(FindOptimalClusterIntractive.SortedNameOnt[in]);
	       		}else{
//	       			SelectCHWindows.ListArrayCH.addElement(s.get(0));
	       			SelectCHWindows.ListArrayCH.addElement(s);
	       		}
       		}
       		
       	}
       	//JOptionPane.showMessageDialog(null, "The concepts with our ranking score are:"+ info, "Information", JOptionPane.INFORMATION_MESSAGE); // open a new window like visulaization window
       	
       	
       	//SelectCHWindows.ListArrayCH.addElement(info);
       	
       	
       	//save user CH
       	String UserCH = CHs; //it should be those one which user select from combobox
       	
       	//save user CH and then call Seecont
       	//CentralClustering SeeCOntObj= new CentralClustering ();
       	//LinkedHashMap<Integer, Cluster> Clusters;
       	//Clusters = SeeCOntObj.SeeCOntAlogorithmIntract(UserCH);
          
       

//when it finilized, it should also show in partitoing panle
//PartitioningPanel.NumCHTextField.setText(String.valueOf(Coordinator.KNumCH));
}
}