
package fusion.oapt.general.visualization;


import fusion.oapt.algorithm.partitioner.SeeCOnt.Cluster;
import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.general.gui.MainFrame;
import fusion.oapt.general.gui.MergingPanel;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.general.visualization.GraphFrame;
import fusion.oapt.general.visualization.Converter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;

import javax.swing.JOptionPane;

import fusion.oapt.general.visualization.net.Network;



public class Visualization
{
	protected static GraphFrame graphFrame;
	private static Converter converter = new Converter();
    
 public  static void Run (int argnum, Controller con){
	 
	 LinkedHashMap<Integer, Cluster> clusters=con.getClusters();
	 Controller cont=con;
	 if (argnum == 0) { //it calls from partitioning tab
	 
		 String ModuleName = null;
		// String ontName = cont.MB.getOntoName();//ModelBuild.ontologyName;
		// String tempDir = cont.MB.wd;//ModelBuild.wd;//It is equal to "./temp/" 
		 String tempDir = ModelBuild.wd; 
		 String []stfn = ModelBuild.ontologyName.split("\\_"); 
		 String ontName = stfn[0];
		
		 MainFrame mainframe = null;
		 graphFrame = new GraphFrame(mainframe,con);
		 graphFrame.clearGraph();
		 ModelBuild.colorIndex = -1;
		 int size=clusters.size();
		 if (OPATgui.showAllModuleCheckBox.isSelected()){
			 for (int cid=0; cid<size; cid++){
				    ModuleName = tempDir + ontName + "_Module_" + cid + ".owl"; // Read our created modules 
				    //System.out.println("the module\t"+ModuleName);
				    DrawGraph(ModuleName, clusters.get(cid).getCH().getLocalName()); //convert Owl to Net and also show it in graphFrame
			     }
			 graphFrame.setVisible(true);
		 } else {
			 if (OPATgui.NumModuleTextField.getText() == null){
				 JOptionPane.showMessageDialog(null, "Please Select the number of module", "Error", JOptionPane.ERROR_MESSAGE);
			 } else {
				 int numModule=0;
				 String []nm =OPATgui.NumModuleTextField.getText().split(",");
				 for (int t=0; t<nm.length; t++){
					 try{
						 numModule = Integer.parseInt(nm[t]);
					     numModule = numModule -1;
						 if (numModule+1 > size || numModule+1 <= 0){
							 JOptionPane.showMessageDialog(null, "You have "+ Coordinator.KNumCH +" modules, while you select one value in other range. You can enter between 1 and " + Coordinator.KNumCH, "Error", JOptionPane.ERROR_MESSAGE);
						 }else {
							 ModuleName = tempDir + ontName + "_Module_" + numModule + ".owl"; // Read our created modules 
							 
//							 DrawGraph(ModuleName); //convert Owl to Net and also show it in graphFrame
							 DrawGraph(ModuleName,Coordinator.clusters.get(numModule).getCH().getLocalName()); //convert Owl to Net and also show it in graphFrame
							 graphFrame.setVisible(true);
							 }
					 } catch(NumberFormatException e){
						 JOptionPane.showMessageDialog(null, "You should seperate your selected modules by ',' \n For example enter 2,4 without any space ", "Error", JOptionPane.ERROR_MESSAGE);
					 }
				 }
			 }
		 }
	 } else if (argnum ==1){ //it calls from merge tab
		 String ModuleName = null;
		 String tempDir="resources/merge/result/merge"+MergingPanel.numAlgorithm+".owl";
		
		 MainFrame mainframe = null;
		 graphFrame = new GraphFrame(mainframe,con);
		 graphFrame.clearGraph();
		 ModelBuild.colorIndex = -1;
		 
	    //ModuleName = tempDir + ontName + "_Module_" + cid + ".owl"; // Read our created modules
	    ModuleName = tempDir ;
//	    # save elements of one ontology
//	    ArrayList<String> elm = elmFunc();
//	    DrawGraph(ModuleName,"CH"); //convert Owl to Net and also show it in graphFrame 
//	    DrawGraph(ModuleName,elm); //convert Owl to Net and also show it in graphFrame
	   
		 ModuleName = MergingPanel.MergeModels.get(0); 
	    DrawGraph(ModuleName, "CH");
	    ModelBuild.colorIndex  ++;
	    ModuleName = MergingPanel.MergeModels.get(1); 
	    DrawGraph(ModuleName, "CH");
	    
	    
	    
	    graphFrame.setVisible(true);

	 } else if (argnum == 2){ //it calls from Optimal number of CH
		 //String ModuleName = null;
		 //String ontName = BuildModel.ontologyName;
		 String ontName = ModelBuild.ontologyName;// .nameOnt;
		 		 
		 //String tempDir = BuildModel.wd;//It is equal to "./temp/" 
				
		 MainFrame mainframe = null;
		 graphFrame = new GraphFrame(mainframe,con);
		 graphFrame.clearGraph();
		 ModelBuild.colorIndex = -1;
		 
//	    ModuleName = tempDir + ontName + "_Module_" + cid + ".owl"; // Read our created modules
//	    ModuleName = tempDir ;
	    DrawGraph(ontName,"CH"); //convert Owl to Net and also show it in graphFrame
	    // argument "CH" means consider all CHs
	    
		 

		 graphFrame.setVisible(true);
	 }
	 
}

////////////////////////////////////////////////////////////////////////////////////////////
 private static void DrawGraph(final String ModuleName, String Sch) {//GEN-FIRST:event_convertButtonActionPerformed
     //This function convert OWL to NET and then Draw its graph in graphframe
	 
        	 String ontologyFilename = ModuleName;
             String networkFilename = "./temp/temp.net";
             File tempFile2 = new File (networkFilename);
             if (tempFile2.exists()) {
            	 tempFile2.delete();
             } 
                         
             File ontologyFile = new File(ontologyFilename);
             File networkFile = new File(networkFilename);

             if (!ontologyFile.exists()) {
                 JOptionPane.showMessageDialog(null,"The ontology file does not exist.", "Error",JOptionPane.ERROR_MESSAGE);
                 return ;
             }

             // Clear the converter
             converter.clear();

             // Read the ontology file
             try {
                 converter.readOntology(ontologyFile);
             } catch (IOException e) {
                 e.printStackTrace(System.err);
                 JOptionPane.showMessageDialog(null,"An I/O error occurred while reading the ontology file.", "Error", JOptionPane.ERROR_MESSAGE);
                 return ;
             }

             // Set the parameters for the conversion
             converter.setIncludeSubclassLinks(OPATgui.includeSubclassLinksCheckBox.isSelected());
             converter.setStrengthSubclassLinks(Float.parseFloat(OPATgui.strengthSubclassLinksSpinner.getValue().toString()));
             converter.setIncludePropertyLinks(OPATgui.includePropertyLinksCheckBox.isSelected());
             converter.setStrengthPropertyLinks(Float.parseFloat(OPATgui.strengthPropertyLinksSpinner.getValue().toString()));
             converter.setIncludeDefinitionLinks(OPATgui.includeDefinitionLinksCheckBox.isSelected());
             converter.setStrengthDefinitionLinks(Float.parseFloat(OPATgui.strengthDefinitionLinksSpinner.getValue().toString()));
             converter.setIncludeOnlyDefinitionProperties(OPATgui.includeDefinitionResourcesComboBox.getSelectedIndex() == 0);
             converter.setIncludeSubstringLinks(OPATgui.includeSubstringLinksCheckBox.isSelected());
             converter.setStrengthSubstringLinks(Float.parseFloat(OPATgui.strengthSubstringLinksSpinner.getValue().toString()));
             converter.setIncludeDistanceLinks(OPATgui.includeDistanceLinksCheckBox.isSelected());
             converter.setStrengthDistanceLinks(Float.parseFloat(OPATgui.strengthDistanceLinksSpinner.getValue().toString()));
             converter.setMaxDistance(Integer.parseInt(OPATgui.maxDistanceSpinner.getValue().toString()));
  
             if (OPATgui.linkTypeComboBox.getSelectedIndex() == 0) {
                 converter.setLinkType(fusion.oapt.general.visualization.net.Link.Type.EDGE);
             } else {
                 converter.setLinkType(fusion.oapt.general.visualization.net.Link.Type.ARC);
             }

             // Perform the conversion
             converter.convert();

             // Write the network file
             Network network = converter.getNetwork();
             try {
                 network.write(networkFile);
             } catch (IOException e) {
                 e.printStackTrace(System.err);
                 JOptionPane.showMessageDialog(null,"An I/O error occurred while writing the network file.","Error", JOptionPane.ERROR_MESSAGE);
                 return ;
             }

             // Draw the graph in the graph window that corresponds to the network
             
             graphFrame.createGraph(converter.getNetwork(), Sch);
             
			return ;
                   
     }
     

}
