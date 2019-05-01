
package fusion.oapt.algorithm.partitioner.axiomClustering;

import java.util.ArrayList;
import java.util.LinkedHashMap;


import fusion.oapt.model.RBGModel;

import org.apache.jena.ontology.OntModel;

public class AxiomNewClustering {
		
	private RBGModel jmodelRbg1 = null;
	private OntModel imodelOnt1 = null;
	//private OWLOntology imodelOnt1=null;
	private String name1 = null;
	private int maxSize = 500;
    private String tempDir = null;
    private int NumCH = 1;
//    private Alignment alignSet = null;
	
	
	public  AxiomNewClustering (  RBGModel jmodel1, String n1,  String dir, int kNumCh){
		
		this.jmodelRbg1 = jmodel1;
		//this.imodelOnt1 = imodel1;		
		this.name1 = n1;
        this.tempDir = dir;
        NumCH = kNumCh;
  
		
	}

	//public HashMap<Integer, Cluster> SeeCOntAlogorithm(){
	public LinkedHashMap<Integer, AxiomCluster> SeeCOntAlogorithm(){//new samira
		
		//CClustering C_Ont1= new CClustering (imodelOnt1, jmodelRbg1, name1, maxSize, tempDir, NumCH);
		AxiomClustering axiom_Ont1= new AxiomClustering (jmodelRbg1, name1, maxSize, tempDir);
		axiom_Ont1.StepsCClustering();
		LinkedHashMap<Integer, AxiomCluster> GeneratedClusters = axiom_Ont1.getClusters();
		//ArrayList<OntModel> modules=C_Ont1.PreProcessingForMatching_Phase();
		
		return GeneratedClusters;
		}
	
     public ArrayList<OntModel> SeeCOntAlogorithm_modules(){
		
    	 //CClustering C_Ont1= new CClustering (imodelOnt1, jmodelRbg1, name1, maxSize, tempDir, NumCH);
    	 AxiomClustering axiom_Ont1= new AxiomClustering ( jmodelRbg1, name1, maxSize, tempDir);
    	 axiom_Ont1.StepsCClustering();
		//HashMap<Integer, Cluster> GeneratedClusters = axiom_Ont1.getClusters();
		ArrayList<OntModel> modules=AxiomClustering.CreateOutput_Phase();
		
		return modules;
		}

     //public Alignment getAnchors()
     //{
       //  return alignSet;
     //}

}