
package fusion.oapt.general.visualization;


import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.general.gui.MainFrame;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.general.visualization.Converter;

import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import fusion.oapt.general.visualization.net.Network;


public class Visualization2
{
	protected static GraphFrame2 graphFrame;
	private static Converter converter = new Converter();
    
 public  static void Run (Controller con){
	 
	     String ontName = con.MB.getOntoName();//ModelBuild.ontologyName;// .nameOnt;
		  MainFrame mainframe = null;
		 graphFrame = new GraphFrame2(mainframe,con);
		 graphFrame.clearGraph();
		 con.MB.colorIndex = -1;
		 
	    //ModuleName = tempDir + ontName + "_Module_" + cid + ".owl"; // Read our created modules
	    //ModuleName = tempDir ;
	    //DrawGraph(ontName); //convert Owl to Net and also show it in graphFrame
		 DrawGraphColorful(ontName, "CH", con); //convert Owl to Net and also show it in graphFrame
	    graphFrame.setVisible(true);
	 
	 
}
 ////////////////////////////////////////////////////////////////////////////////////////////
 private static Object DrawGraphColorful(final String ModuleName, String Sch, Controller con) {//GEN-FIRST:event_convertButtonActionPerformed
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
                 return null;
             }

             // Clear the converter
             converter.clear();

             // Read the ontology file
             try {
                 converter.readOntology(ontologyFile);
             } catch (IOException e) {
                 e.printStackTrace(System.err);
                 JOptionPane.showMessageDialog(null,"An I/O error occurred while reading the ontology file.", "Error", JOptionPane.ERROR_MESSAGE);
                 return null;
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
                 return null;
             }

             
             // Draw the graph in the graph window that corresponds to the network
             graphFrame.createGraph(converter.getNetwork(), Sch,con);

             
			return null;
             
         
     }
     
}
