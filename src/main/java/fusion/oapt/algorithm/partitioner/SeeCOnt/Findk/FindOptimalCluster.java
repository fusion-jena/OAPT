
package fusion.oapt.algorithm.partitioner.SeeCOnt.Findk;

import java.util.HashMap;
import java.util.Iterator;
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


public class FindOptimalCluster 
{
	float LogAIC []= null;
	int OptKAIC=1;
	float LogBIC []= null;
	int OptK=1;
	boolean flagStop = false;
	OntModel model;
    static int numEnt;
    ModelBuild MB;
    String nameOnt;
    RBGModel rbg;
    String [] SortedNameOnt=null;
    HashMap<String,Set<String>> rankVector;
    HashMap<Node,Set<Node>> nodeRankVector;
    NodeList entitiesCH;
    int enCh=0;
    int lb,ub=0;	
    
    public FindOptimalCluster()
    {
    	model=null;
    	MB=new ModelBuild();
    	numEnt=0;
    	rbg=null;
    	entitiesCH=new NodeList();
    	lb=ub=enCh=0;
    	rankVector=new HashMap<String, Set<String>>();
    	nodeRankVector=new HashMap<Node, Set<Node>>();
    }
	public FindOptimalCluster(String name)
	{
		this.nameOnt=name;
		if (Controller.CheckBuildModel == false) 
	 	{
	 		MB=new ModelBuild(nameOnt);
	 	
	 		MB.build();
	 		model=MB.getModel();
	 		rbg=MB.getRBGModel();
	 		numEnt=MB.NumEntity;
	 		Controller.CheckBuildModel =true;
	 	}
		numEnt=(int) model.size();
		entitiesCH=new NodeList();
		lb=ub=enCh=0;
		rankVector=new HashMap<String, Set<String>>();
		nodeRankVector=new HashMap<Node, Set<Node>>();
	}
	
	public FindOptimalCluster(ModelBuild MB)
	{
		this.MB=MB;
		model=MB.getModel();
		nameOnt=MB.getOntoName();
		rbg=MB.getRBGModel();
		numEnt=(int) model.size();
		entitiesCH=new NodeList();
		rankVector=new HashMap<String, Set<String>>();
		nodeRankVector=new HashMap<Node, Set<Node>>();
	}
	
	public  FindOptimalCluster(OntModel nameOnt)
	{
		this.model=nameOnt;
		numEnt=(int) model.size();
	}
 
		
   public NodeList getNodeCH()
  {
	return entitiesCH;
  }

   public ModelBuild getModelBuild()
   {
  	 return MB;
   }
   
  public int getOptCH()
  {
	if(entitiesCH==null)
		FindOptimalClusterFunc();
	return enCh;
 }

  public int getLB()
 {
	if(lb==0)
		FindOptimalClusterFunc();
	return lb;
 }

public int getUB()
{
	if(ub==0)
		FindOptimalClusterFunc();
	return ub;
}
  
public HashMap<String, Set<String>> getRankVector()
{
	return rankVector;
}

public HashMap<Node, Set<Node>> getNodeRankVector()
{
	return nodeRankVector;
}

 public  int FindOptimalClusterFunc()
 {
	 if (Controller.CheckBuildModel == false) 
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
			
	} 
	 int size=rbg.getNamedClassNodes().size();//model.listNamedClasses().toList().size();
	 int size1=model.listNamedClasses().toList().size();
	 size = (size1 > size) ? size : size1;
	 if(size<=0) return OptK=0; 
	 if(size<=40) {
		 lb=ub=1;
		 return OptK=1;
	 }
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
 	 if(enCh<=lb)
 	{
 		 entitiesCH=entitiesCHTemp;
 		 OptK=enCh;  Coordinator.KNumCH=OptK;
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
 private  int[] LowUppCalculation (){
	 int LU []= new int [2];
	 
	 float sum=0;
	 NodeList list=rbg.getNamedClassNodes();
	 int size=list.size();
	 for (int j=0; j<size; j++) {
	    	int hier= list.get(j).getHierarchy();
	    	if(hier<size)
	    	  sum= sum + list.get(j).getHierarchy();
	    }
	 float Mean= sum / size;
 	 float xxx= (float) (Math.log10(size)*Mean);
	 xxx= Math.round(xxx);
	 
	 LU[0]= (int) (xxx-5); if (LU[0] <1) LU[0]=1; 
	 LU[1]= (int) (xxx+5);
	 
	 return LU;
 }
 /////////////////////////////////////////////////////////////////
 
//////////////////////////////////////////////////////////////////////
//Creating the models and calculated their BIC
/* {	 int index = 0;
int NumModel = ub - lb + 1;
LogBIC = new  float [NumModel];

//first one with full SeeCOnt steps (7steps)
Coordinator.KNumCH = lb;

CClustering cc = new CClustering(MB);
//cc.StepsCClustering(2); //call from FindK (first iterative)
// Clusters = Coordinator.clusters;

LogBIC[index] = BIC (lb, Clusters,  MB.entities.size());
index ++;

// the next ones with SeeCOnt with 5 steps
for (int k=lb+1; k < ub + 1; k++){
Coordinator.KNumCH = k;

//cc.StepsCClustering(3); //call from FindK (other iterative)
//Clusters = Coordinator.clusters;
LogBIC[index] = BIC (k, Clusters,  MB.entities.size());
index ++;
}

// select optimal k with the lowest BIC value
float minBIC = LogBIC[0]; OptK= lb;  	
int indexM= lb ; 
for (int i=0; i <NumModel ; i++){
if (LogBIC [i] < minBIC) {
minBIC = LogBIC[i];
OptK= indexM; 
}
indexM ++;
}
System.out.println("LU\t"+lb +"\t UB"+ ub);
return OptK;
}*/
 
 
//////////////////////////////////////////////////// BIC ///////////////////////////////////////////////////////
/*public static float BIC (int k, LinkedHashMap<Integer, Cluster> Clusters, int NumConcept){
	float BICvalue=1;
	float RSS= RSScalculate(k,Clusters, NumConcept);
	BICvalue = (float) ((float) (NumConcept * Math.log (RSS/NumConcept) ) + (k * Math.log (NumConcept)));
	
	return BICvalue;
}

/////////////////////////////////////////////////// RSS ////////////////////////////////////////////////////////
public static float RSScalculate (int k, LinkedHashMap<Integer, Cluster> Clusters, int NumConcept){
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

private  float computeDistance(Cluster cluster, Node ch)
{
	//System.out.println("the ont\t"+ch.toString());
	Resource cc1 = MB.getModel().getOntResource(ch.toString());
	float dis=0;
	for (Iterator<Node> iter = cluster.listElements(); iter.hasNext();) {
		Node concept = iter.next(); 
		//OntClass cl2=BuildModel.OntModel.getOntClass(concept.getLocalName());
		 RDFNode cc2 =  (RDFNode) MB.getModel().getOntClass(concept.toString());
		//RDFNode cc2 =  (RDFNode) model.getOntClass(ch.getLocalName()).asNode();
		 Path shortestPath = null;//OntTools.findShortestPath(BuildModel.OntModel, cc1, cc2,x);
		//Path shortestPath = OntTools.findShortestPath(BuildModel.OntModel, cc1, cc2,null);
		if(shortestPath!=null) dis+=shortestPath.size();
		if(shortestPath==null) dis+=10;
	}
	return dis;
}*/
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

public static void main(String args[])
{
    
    double start=System.currentTimeMillis();
   // String fp1 = "D:/test_ont/test/conference.owl";
    String fp2 = "D:/owl/owl/IDOBRU.owl.gz";
    FindOptimalCluster OPC=new FindOptimalCluster(fp2);
    int k=OPC.FindOptimalClusterFunc();
    System.out.println(OPC.lb+ "\t the rank size\t"+k+"\t"+OPC.ub);
    double end=System.currentTimeMillis();
   System.out.println(k+"\t The partitioning time---->"+(end-start)*.001+"\t sec \t");
} 


}


