
package fusion.oapt.general.cc;


import java.util.ArrayList;
import java.util.LinkedHashMap;

import fusion.oapt.algorithm.partitioner.SeeCOnt.CClustering;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Cluster;
import fusion.oapt.algorithm.partitioner.SeeCOnt.EvaluationSeeCOnt;
import fusion.oapt.algorithm.partitioner.axiomClustering.AxiomClustering;
import fusion.oapt.algorithm.partitioner.pbm.Partitioner;

import org.apache.jena.ontology.OntModel;

import fusion.oapt.general.cc.Parameters;
import fusion.oapt.general.gui.PartitioningPanel;
import fusion.oapt.model.RBGModel;


public class Controller
{
	public static boolean CheckBuildModel ;
	private  String filepath1 = null;   
	public  OntModel OntModel = null;
	public static  ModelBuild MB;
	RBGModel rbgm;
	private  LinkedHashMap<Integer, Cluster> clusters;
	private ArrayList<OntModel> modules=new ArrayList<OntModel>();
 
  
 public Controller()
 {
	 CheckBuildModel =false;
	 MB=new ModelBuild();
	 filepath1 = Parameters.onto1;
	 clusters=new LinkedHashMap<Integer, Cluster>();
	 modules=new ArrayList<OntModel>();
 }
 
 public Controller(String fp1) 
 {
	CheckBuildModel=false;
 	if(CheckBuildModel==false)
 	{
 		filepath1 = fp1;
 		MB=new ModelBuild(fp1);
 		MB.build();
		Controller.CheckBuildModel =true;
 		OntModel=MB.getModel();
 		rbgm=MB.getRBGModel();
 	}
 	 	
 }
 
 public ModelBuild getModelBuild()
 {
	 return MB;
 }
 
 
 public RBGModel getRBGModel()
 {
	 return rbgm;
 }

  
 public OntModel getOntModel()
 {
     return OntModel;
 }
     
  public void runPartition()
  {
	  if (Controller.CheckBuildModel == false) {	
       MB=new ModelBuild(filepath1);
 	   MB.build();
 	   OntModel=MB.getModel();
 	   rbgm=MB.getRBGModel();
       Controller.CheckBuildModel =true;
 	}
       	CClustering C_Ont1= new CClustering (MB);
	    C_Ont1.StepsCClustering(0);   
	    EvaluationSeeCOnt ES=new EvaluationSeeCOnt(MB,clusters);
	   if(clusters!=null) ES.Evaluation_SeeCont();
     }
 
  public  void InitialRun (String selectedType, int k){
     if (Controller.CheckBuildModel == false) {
 		MB=new ModelBuild(PartitioningPanel.NameAddressOnt);
 		MB.build();
 		OntModel=MB.getModel();
 		rbgm=MB.getRBGModel();
    	Controller.CheckBuildModel =true;
 	}
         
     switch (selectedType){
     case "SeeCOnt": 
     	CClustering C_Ont1= new CClustering (MB,k);   
	    C_Ont1.StepsCClustering(0);   //0 means call from Execute button to partition the whole ontology (normal case)
	    clusters=C_Ont1.getClusters();
	    modules=C_Ont1.getModules();
	    EvaluationSeeCOnt ES=new EvaluationSeeCOnt(MB,clusters);
	    if(clusters!=null) ES.Evaluation_SeeCont();
       // EvaluationSeeCOnt.Evaluation_SeeCont(MB);
        break;
         
     case "AxCOnt":
    	AxiomClustering axiom_Ont1= new AxiomClustering ( BuildModel.rbgmModel,  BuildModel.fn1, 500, BuildModel.wd);
    	axiom_Ont1.StepsCClustering();
      	break;
     
     case "PBM":
    	 Partitioner p1 = new Partitioner(BuildModel.rbgmModel,  BuildModel.fn1, 500, BuildModel.wd);
         p1.partition();
    	 break;
     
     } 
 }
  
  
  public  void InitialRun_API (String selectedType, int k){
	     if (Controller.CheckBuildModel == false) {
	 		MB=new ModelBuild(PartitioningPanel.NameAddressOnt);
	 		MB.build();
	 		OntModel=MB.getModel();
	 		rbgm=MB.getRBGModel();
	    	Controller.CheckBuildModel =true;
	 	}
	         
	     switch (selectedType){
	     case "SeeCOnt": 
	     	CClustering C_Ont1= new CClustering (MB,k);   
		    C_Ont1.StepsCClustering(0);   //0 means call from Execute button to partition the whole ontology (normal case)
		    clusters=C_Ont1.getClusters();
		    modules=C_Ont1.getModules();
		    break;
	         
	     case "AxCOnt":
	    	AxiomClustering axiom_Ont1= new AxiomClustering ( BuildModel.rbgmModel,  BuildModel.fn1, 500, BuildModel.wd);
	    	axiom_Ont1.StepsCClustering();
	      	break;
	     
	     case "PBM":
	    	 Partitioner p1 = new Partitioner(BuildModel.rbgmModel,  BuildModel.fn1, 500, BuildModel.wd);
	         p1.partition();
	    	 break;
	     
	     } 
	 }
  
  public  ArrayList<OntModel> getModules()
  {
  	return modules;
  }

  
  public  LinkedHashMap<Integer, Cluster> getClusters()
  {
 	return clusters; 
  }
   
   	
}
