
package fusion.oapt.algorithm.partitioner.SeeCOnt;


import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.jena.ontology.OntModel;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.model.RBGModel;

public class CClustering {
	
	OntModel model;
	ModelBuild MB;
	RBGModel rbgm;
	String ontName;
	int NoCH;
	private  LinkedHashMap<Integer, Cluster> clusters;
	private ArrayList<OntModel> modules=new ArrayList<OntModel>();
	
	public CClustering(String OntName)
	{
		MB=new ModelBuild(OntName);
		MB.build();
		this.model=MB.getModel();
		this.rbgm=MB.getRBGModel();
		this.ontName=OntName;
		clusters=new LinkedHashMap<Integer, Cluster>();
		modules=new ArrayList<OntModel>();
		NoCH=0;
	}
	
  public CClustering(){}
	
	public CClustering(ModelBuild model)
	{
		this.MB=model;
		this.model=model.getModel();
		this.rbgm=model.getRBGModel();
		ontName=MB.getOntoName();
		clusters=new LinkedHashMap<Integer, Cluster>();
		modules=new ArrayList<OntModel>();
		NoCH=0;
	}
	
	public CClustering(ModelBuild model, int k)
	{
		this.MB=model;
		this.model=model.getModel();
		this.rbgm=model.getRBGModel();
		ontName=MB.getOntoName();
		clusters=new LinkedHashMap<Integer, Cluster>();
		modules=new ArrayList<OntModel>();
		NoCH=k;
	}
	
	public  ArrayList<OntModel> getModules()
	  {
	  	return modules;
	  }

	  
	  public  LinkedHashMap<Integer, Cluster> getClusters()
	  {
	 	return clusters; 
	  }

   public void  StepsCClustering (int arg)
   {
	   Partitioning part;
	   if(NoCH==0) part=new  Partitioning(MB);
	   else
		   part=new  Partitioning(MB,NoCH);
	   String tempDir = MB.wd;
	   int size=rbgm.getNamedClassNodes().size();
	   int size1=model.listNamedClasses().toList().size();
	   size = (size1 > size) ? size : size1;
	   CreateModule m = new CreateModule(MB);
	   String file= Paths.get(MB.getOntoName()).getFileName().toString();
	   int iend = file.indexOf(".");
	   String name=file.substring(0,iend);
       if(size<=0) return ; 
	   if(size<=40) 
			   
	   {
		    clusters=new LinkedHashMap<Integer, Cluster>();
	   		part.partition(1);
	   		clusters=part.getPartitions();
		    String outPath = tempDir + name + "_Module_" + 0 + ".owl";
		    m.saveOntologyByName(MB.getOntoName(),outPath);
			modules=m.getOntModels();
			return;
	 	}
	   
	    switch (arg){
	   	case 0: //call from Execute button to partition the whole ontology (normal case)
	   		clusters=new LinkedHashMap<Integer, Cluster>();
	   		part.partition();
	   		clusters=part.getPartitions();
	   		m=new CreateModule(MB,clusters);
	   		m.createOWLFiles_Phase(); 
	   		modules=m.getOntModels();
	   		break;

		case 1: //call from Apply button of adding CH (as an interactive process)
			clusters=new LinkedHashMap<Integer, Cluster>();
			part=new  Partitioning(MB);
			part.AddCH();
			part.createIniCluster_Phase();
			part.direct_Partition_Phase();
			part.indirect_Partition_Phase();
			clusters=part.getPartitions();
			 m=new CreateModule(MB,clusters);
		   	 m.createModules();
			 m.createOWLFiles_Phase();
			 modules=m.getOntModels();
			break;
			
		/*case 2: //call from FindK (first iterative) 
//			r= new Rank(); r.rank_old_Phase(); r.sort_old_Phase();
//			c = new ClusterProcess();
			//r= new Rank(); r.rankConcepts();
	   		rankVector=RC.getrankVector();
			c = new ClusterProcess(MB,rankVector);
//			c.selectClusterHead_old_Phase();
			c.selectClusterHead_Phase();
	   		c.createCluster_Phase();
//			c.createCluster_old_Phase();
	   		c.initialCluster_Phase();
//			c.initialCluster_old_Phase();
	   		c.finalizeClsuetring_old_Phase(); //better than new one
//	   		c.finalizeClsuetring_Phase();      
	   		m = new CreateModule(MB);
//	   		m.createOutput_old_Phase();
	   		m.createOWLFiles_Phase(); //better than old one 
	   		break;
	   		
		case 3: //call from FindK (other iterative)	
			c = new ClusterProcess(MB);
//	   		c.selectClusterHead_old_Phase();
			c.selectClusterHead_Phase();
	   		c.createCluster_Phase();
//	   		c.createCluster_old_Phase();
	   		c.initialCluster_Phase();
//	   		c.initialCluster_old_Phase();
	   		c.finalizeClsuetring_old_Phase();
//	   		c.finalizeClsuetring_Phase();      
	   		m = new CreateModule(MB);
//	   		m.createOutput_old_Phase();
	   		m.createOWLFiles_Phase();
	   		break;*/
		   
	   	}
   }

}