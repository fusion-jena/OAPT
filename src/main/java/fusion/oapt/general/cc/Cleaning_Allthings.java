
package fusion.oapt.general.cc;

import fusion.oapt.general.gui.EvalPanelPartitioning;
import fusion.oapt.general.gui.AnalysisPanel;
import fusion.oapt.general.gui.PartitioningPanel;



public class Cleaning_Allthings
{    
    public Cleaning_Allthings()
    {
        
       }
    public static  void clean(int NumTab)
    {
    	switch (NumTab){
    	case 0: // if this function called by the button on tab 1 (Analyzing)   	
        	AnalysisPanel.GeneralTextArea1.setText(null);
        	AnalysisPanel.GeneralTextArea2.setText(null);
        	AnalysisPanel.ConsistencyTextArea1.setText(null);
        	AnalysisPanel.ConsistencyTextArea2.setText(null);
        	AnalysisPanel.RichnessTextArea1.setText(null);
        	AnalysisPanel.RichnessTextArea2.setText(null);
        	if (AnalysisPanel.NameAddressOnt1 != null && AnalysisPanel.NameAddressOnt1.equals(AnalysisPanel.ontologyFileTextField1.getText())){
        		//do not need re-built, since the ontology files is same as before
        	}else{
	        	Controller.CheckBuildModel = false;
	        	ModelBuild.entities=null;
	        	//BuildModel.entities=null;
        	}

    		break;

    	case 1: // if this function called by the button on tab 1 (Partitioning)	
        	EvalPanelPartitioning.Result1_Details.setText(null);
        	EvalPanelPartitioning.Result3_Details.setText(null);
        	EvalPanelPartitioning.Result2_Details.setText(null);
        	EvalPanelPartitioning.AvgTextBox1.setText(null);
        	EvalPanelPartitioning.AvgTextBox2.setText(null);
        	EvalPanelPartitioning.AvgTextBox3.setText(null);
        	PartitioningPanel.OutputPartitioningTextArea.setText(null);    	
        	if ((PartitioningPanel.NameAddressOnt != null && PartitioningPanel.NameAddressOnt.equals(PartitioningPanel.ontologyFileTextField.getText())) ||
        	(AnalysisPanel.NameAddressOnt1 != null && AnalysisPanel.NameAddressOnt1.equals(PartitioningPanel.ontologyFileTextField.getText()))){
        		//do not need re-built, since the ontology files is same as before
        	}else{
	        	Controller.CheckBuildModel = false;
	        	ModelBuild.entities=null;
	        	//BuildModel.entities=null;
        	}

    		break;
    	case 2:// if this function called by the button on tab 2 (Partitioning Evaluation)
    		Controller.CheckBuildModel = false;
    		ModelBuild.entities=null;
        	//BuildModel.entities=null;
    		break;
    		
    	case 3:// if this function called by the button on tab 3(Matching)
    		//result table should be empty
    		Controller.CheckBuildModel = false;
    		ModelBuild.entities=null;
        	//BuildModel.entities=null;
    		break;
    		
    		
    	case 4:// if this function called by the button on tab 4 (Merging Evaluation)
    		Controller.CheckBuildModel = false;
    		ModelBuild.entities=null;
        	//BuildModel.entities=null;
    		break;
    	
    	case 5:// if this function called by the button on tab 5 (Merging Evaluation)
    		Controller.CheckBuildModel = false;
    		ModelBuild.entities=null;
        	//BuildModel.entities=null;
    		break;
    		
    	case 6:// if this function called by the button on tab 6 (Analyzing)
    		Controller.CheckBuildModel = false;
    		ModelBuild.entities=null;
        	//BuildModel.entities=null;
    		break;
    	
    	}
    }    
}

