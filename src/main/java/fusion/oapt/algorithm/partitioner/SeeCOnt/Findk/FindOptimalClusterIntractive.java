
package fusion.oapt.algorithm.partitioner.SeeCOnt.Findk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;





import java.util.Set;



import org.apache.jena.ontology.OntModel;

import fusion.oapt.algorithm.partitioner.SeeCOnt.Cluster;
import fusion.oapt.algorithm.partitioner.SeeCOnt.rankConcept;
import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;
import fusion.oapt.model.RBGModel;



public class FindOptimalClusterIntractive 
{
	private static float[] importantAll=null ;
	public static  String [] SortedNameOnt=null;
	public static String [] CHset;
	static float LogAIC []= null;
	static int OptKAIC=1;
	static float LogBIC []= null;
	static int OptK=1;
	static boolean flagStop = false;
	static OntModel model;
    static int numEnt;
    private ModelBuild MB;
    NodeList entities;
    int enCh=0;
    int lb,ub=0;
    RBGModel rbg;
    HashMap<String,Set<String>> rankVector;
    HashMap<Node,Set<Node>> nodeRankVector;
    static NodeList entitiesCH;
    
	public FindOptimalClusterIntractive(String nameOnt){
		if (Controller.CheckBuildModel == false) 
	 	{
	 		MB=new ModelBuild(nameOnt);
	 		MB.build();
	 		model=MB.getModel();
	 		rbg=MB.getRBGModel();
	 		numEnt=MB.NumEntity;
			Controller.CheckBuildModel =true;
	 	}
		rankVector=new HashMap<String, Set<String>>();
    	nodeRankVector=new HashMap<Node, Set<Node>>();
		entities=MB.entities; 
		entitiesCH=new NodeList();
	}
	
	public FindOptimalClusterIntractive(ModelBuild MB){
		
	 	this.MB=MB;// 
	 	model=MB.getModel();
	 	rbg=MB.getRBGModel();
	 	numEnt=MB.NumEntity;
		Controller.CheckBuildModel =true;
	 	rankVector=new HashMap<String, Set<String>>();
    	nodeRankVector=new HashMap<Node, Set<Node>>();
		entities=MB.entities; 
		entitiesCH=new NodeList();
	}
	
	public FindOptimalClusterIntractive(String nameOnt,Controller con){
		if (con.CheckBuildModel == false) 
	 	{
	 		MB=new ModelBuild(nameOnt);
	 		MB.build();
	 		model=MB.getModel();
	 		rbg=MB.getRBGModel();
	 		numEnt=MB.NumEntity;
			con.CheckBuildModel =true;
	 	}
		rankVector=new HashMap<String, Set<String>>();
    	nodeRankVector=new HashMap<Node, Set<Node>>();
		entities=MB.entities; 
		entitiesCH=new NodeList();
	}
	
	public  FindOptimalClusterIntractive(OntModel nameOnt){
		this.model=nameOnt;
		numEnt=(int) model.size();
		entities=MB.entities;  
		}

 public ModelBuild getModelBuild()
 {
	 return MB;
 }
	
 public  int FindOptimalClusterFunc()
 {
	/*  if (Controller.CheckBuildModel == false) 
	 	{
			MB=new ModelBuild(nameOnt);
		 	MB.build();
		 	model=MB.getModel();
		 	rbg=MB.getRBGModel();
		 	numEnt=MB.NumEntity;
		 	Controller.CheckBuildModel =true;
	 	}
		if(MB==null)
		{
			MB=new ModelBuild(nameOnt);
			MB.build();
			model=MB.getModel();
			rbg=MB.getRBGModel();
			Controller.CheckBuildModel =true;
				
		} */
		 int size=rbg.getNamedClassNodes().size();//model.listNamedClasses().toList().size();
		 int size1=model.listNamedClasses().toList().size();
		 size = (size1 > size) ? size : size1;
		 if(size<=0) return OptK=0; 
		 int d=0; 
		 if(size<1000)
			 d=3;
		 else if(size>1000 && size<20000) d=2;
		 else if(size>20000 && size<50000) d=1;
		 else
		    d=0;
		 rankConcept RC=new rankConcept(model,rbg,d);
		 RC.rank();
		 SortedNameOnt=RC.getRanked();
		 rankVector=RC.getrankVector();
		 nodeRankVector=RC.getNoderankVector();
		 if(size<=40) {
			 lb=ub=1;
			 return OptK=1;
		 }
		 NodeList entitiesCHTemp = null;
		 if(SortedNameOnt.length>0)  
			 {
			 entitiesCHTemp=getInitialCH();
			  }
		  
	 	/////////////////////////////////////////////////////////////////////
	 	//determining the lower and upper bound to know how many model should be create
		 int lowupValue []= new int [2];
		 lowupValue = LowUppCalculation();
		 lb = lowupValue[0];
		 ub = lowupValue[1];
		 enCh=entitiesCHTemp.size();
	 	 if(enCh<lb)
	 	{
	 		OptK=enCh;
	 		return OptK;
	 	}
	 	int index=checkNoOP(entitiesCHTemp);
	 	enCh=lb+index;
	    OptK=enCh;  Coordinator.KNumCH=OptK;
	  	return OptK;
	  }
 private int checkNoOP(NodeList entitiesCHTemp)
 {
	int size=ub-lb+1;
	double[] sumMin= new double[size]; int k=0;
	for(int i=lb;i<ub-1 && i<entitiesCHTemp.size();i++)
	{
		Node nn=entitiesCHTemp.get(i);
		Set<String> s1=rankVector.get(nn.getLocalName());
		double sum=0;
		for(int j=i+1;j<ub && j<entitiesCHTemp.size();j++)
		{
			Node nnn=entitiesCHTemp.get(j);
    		Set<String> s2=rankVector.get(nnn.getLocalName());
    		Set<String> temp=s2;
    		temp.retainAll(s1);
			if(temp!=null) sum+=temp.size();  temp=s2=null;
   		}
		sumMin[k]=sum; k++;
	}
	int index = 0;
    double min = sumMin[index];
    for (int i=1; i<sumMin.length; i++){

        if (sumMin[i] < min ){
            min = sumMin[i];
            index = i;
        }
    }
    for(int i=0; i<lb+index && i<entitiesCHTemp.size();i++)
    {
    	Node nn= entitiesCHTemp.get(i);
    	entitiesCH.add(nn);
    }
     return index ;
  }

private  NodeList getInitialCH()
{
  NodeList entitiesCH= new NodeList();
  int indexCH = 0 ; int NumCH=1000;
  Node NodeTemp = rbg.getNode(SortedNameOnt[0]);    
  entitiesCH.add(NodeTemp); 
  int k=SortedNameOnt.length;
  int length=0;
  if(k<2000)    // a condition to reduce the search space of optimal chluster head search
	  length=k/10;
 else
     length=k/20;		  
 indexCH++;
 for(int i=1;i<length && indexCH<NumCH;i++)
    {
   	 NodeTemp=rbg.getNode(SortedNameOnt[i]);
   	 boolean check=test(entitiesCH,NodeTemp);
   	 if(check)
	 {
	 entitiesCH.add(NodeTemp);  
	 indexCH++;
	 }
     } 
     return entitiesCH;
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   private boolean test(NodeList list,Node temp)
   {
	   for(int i=0;i<list.size();i++)
	   {
		   Node chnode=list.get(i);
		   Set<String> llist=rankVector.get(chnode.getLocalName());
		   if(llist!=null &&temp.getLocalName().length()!=0)
		   {
			   if(llist.contains(temp.getLocalName())) return false;
		   }
		   
		   Set<String> Flist=rankVector.get(chnode.getLabel());
		   if(Flist!=null && temp.getLocalName().length()==0)
		   {
			   System.out.println(temp.getLabel()+"\t label");
			   if(Flist.contains(temp.getLabel())) return false;
		   }
		   
	   }
	   return true;
   }
////////////////////////////////////////////////////LowUpp ///////////////////////////////////////////////////////
 public  int[] LowUppCalculation (){
	 int LU []= new int [2];
	 NodeList list=rbg.getNamedClassNodes();
	 int size=list.size();
	 float Sum=0;
	 for (int j=0; j<entities.size(); j++) {
	    	Sum= Sum + entities.get(j).getHierarchy();
		}
	 float Mean= Sum / size;
 
	 float xxx= (float) (Math.log10(size)*Mean);
	 xxx= Math.round(xxx);
	 
	 LU[0]= (int) (xxx-5); if (LU[0] <1) LU[0]=1; 
	 LU[1]= (int) (xxx+5);
	 
	 return LU;
 }
//////////////////////////////////////////////////// BIC ///////////////////////////////////////////////////////
public  float BIC (int k, LinkedHashMap<Integer, Cluster> Clusters, int NumConcept){
	float BICvalue=1;
	float RSS= RSScalculate(k,Clusters, NumConcept);
	BICvalue = (float) ((float) (NumConcept * Math.log (RSS/NumConcept) ) + (k * Math.log (NumConcept)));
	
	return BICvalue;
}

/////////////////////////////////////////////////// RSS ////////////////////////////////////////////////////////
public  float RSScalculate (int k, LinkedHashMap<Integer, Cluster> Clusters, int NumConcept){
	Clusters = Coordinator.clusters;
	float RSSvalue = 0;
	float dist=0;  int size=0;
	
	for (Iterator<Cluster> i = Clusters.values().iterator(); i.hasNext();) {
	    Cluster icluster = i.next();
	    Node ch=icluster.getCH();
	    //float d=computeDistance(icluster,ch);
	    size+=icluster.getSize();
	    //dist+=d*icluster.getSize();
		float MeanCluster = MeanClaculate (icluster);
	    for (Iterator<Node> iter = icluster.listElements(); iter.hasNext();) {
			Node concept = iter.next();
			RSSvalue = RSSvalue + ((concept.getHierarchy()- MeanCluster) * (concept.getHierarchy()- MeanCluster));
		}
	 }
	//return dist/(float)size;	
	return RSSvalue;
}

private static float computeDistance(Cluster cluster, Node ch)
{
	/*
	//System.out.println("the ont\t"+ch.toString());
	OntResource cc1 = BuildModel.OntModel.getOntResource(ch.toString());
	float dis=0;
	for (Iterator<Node> iter = cluster.listElements(); iter.hasNext();) {
		Node concept = iter.next(); 
		//OntClass cl2=BuildModel.OntModel.getOntClass(concept.getLocalName());
		 RDFNode cc2 =  (RDFNode) BuildModel.OntModel.getOntClass(concept.toString());
		//RDFNode cc2 =  (RDFNode) model.getOntClass(ch.getLocalName()).asNode();
		Path shortestPath = OntTools.findShortestPath(BuildModel.OntModel, cc1, cc2,Filter.any);
		if(shortestPath!=null) dis+=shortestPath.size();
		if(shortestPath==null) dis+=10;
	}
	
	return dis;
	*/
	return 0;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////
public static float MeanClaculate (Cluster icluster){
	float MeanValue = 0; float Sumheir =0;
	
	for (Iterator<Node> iter = icluster.listElements(); iter.hasNext();) {
		Node concept = iter.next();
		Sumheir = Sumheir + concept.getHierarchy();
	}
	MeanValue = Sumheir / icluster.getElements().size();
	return MeanValue;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static float MeanClaculateNew (Cluster icluster){
	   float MeanValue=0; int sum=0;
	   Iterator<Node> ListNode = icluster.listElements();
	   Node CHnode = icluster.getCH();
	   int LCH= CHnode.getHierarchy();
	   while (ListNode.hasNext()){
		   Node iNode = ListNode.next();
		   int LNode = iNode.getHierarchy();
		   int dist = Math.abs(LCH - LNode);
		   sum = sum + dist;
		   }
	   
	   MeanValue = sum / icluster.getElements().size();
	   
	   return MeanValue;
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public  void LastCHRank(int OptK)
{

	 //CentralClustering SeeCOntObj2= new CentralClustering ();
	 //Clusters = SeeCOntObj2.SeeCOntAlogorithmk1();
	Coordinator.KNumCH = OptK; //It does not matter what is the number of ch in this phase
	Rank_Phase();										
	Sort_Phase();									
}
public  void Rank_Phase()
{
	importantAll = new float [entities.size()];
	for (int i=0; i<entities.size(); i++){
		ArrayList<Node> temp = MB.Connexion (MB.entities.get(i));
			if (temp.size() > 0) {
				importantAll[i] = (float) (temp.size() + chartoint(MB.entities.get(i).getLocalName()));
			}
	}
}
public  void Sort_Phase(){
	
	Sorter str = new Sorter();
	SortedNameOnt = new String [entities.size()]; 
	SortedNameOnt = str.sort(importantAll, MB.entities);
	MB.SortedNameOnt= SortedNameOnt;
}

public static double chartoint(String iname ){
	double r=0;
	iname = iname.toLowerCase(); 
	for (int i=0; i<iname.length(); i++){
		char xx= iname.charAt(i);
		r= r+ (int) xx;
	}
	r= r/iname.length();
	return r;
}

}


