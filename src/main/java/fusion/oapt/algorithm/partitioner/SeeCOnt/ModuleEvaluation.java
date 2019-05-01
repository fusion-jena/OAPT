
package fusion.oapt.algorithm.partitioner.SeeCOnt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;


//import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.algorithm.matcher.string.WordNetMatcher;
import fusion.oapt.model.Node;


public class ModuleEvaluation {
	LinkedHashMap<Integer, Cluster> clusters;
	public static ArrayList<OntModel> models;
	static double[] numTree;
	static int size;
	static List<String> list;
	static double HoMo[];
	static double HeMo[];
	double AvgRS;
	ModelBuild MB;
	public ModuleEvaluation(ModelBuild MB, LinkedHashMap<Integer, Cluster> clusters)
	{
//		models=CClustering.models;
		this.MB=MB;
		models=new ArrayList<OntModel>();
		this.clusters=clusters;
		numTree=CreateModule.numTree;
		size=0;
		list=new ArrayList<String>();
		AvgRS=0;
	}
	
public void  Eval_SeeCont(){ 

	setSize();
	setList();
	
	//////////////////////////////////////////////////////Calculating measure 1 : Lexical Level
	double MH[]= computeMH();
	double SumMH=0;
	//System.out.println('\n'+"*** Details of Clustere's Lexical Level:***");
	for (int i = 0; i< clusters.size(); i++  ){
		//System.out.println("homogeneity Level for Cluster "+ i+ " : "+ String.valueOf(new DecimalFormat("##.####").format(LexL_Cluster[i])));
		SumMH= SumMH + MH[i];
	}
	double AvgMH= SumMH / clusters.size();
	//System.out.println("\t AvgLexL for this ontology is: " + String.valueOf(new DecimalFormat("##.####").format(AvgMH)));
	
	////////////////////////////////////////////////////// Calculating measure 2 : Number of tree
	double NTree_Cluster[]=computeNtree(); 
	double SumNTree=0;
	//System.out.println('\n'+"*** Details of Clustere's number of trees:***");
	for (int i = 0; i< clusters.size(); i++  ){
		//System.out.println("Number of trees for Cluster "+ i+ " : "+ numTree[i] +"\t its avg:\t" +String.valueOf(new DecimalFormat("##.####").format(NTree_Cluster[i])));
		SumNTree= SumNTree + numTree[i];
	}
	double AvgNTree= SumNTree /size;
	//System.out.println("AvgNumTree for this ontology is: " + String.valueOf(new DecimalFormat("##.####").format(AvgNTree)));
	
	//////////////////////////////////////////////////////Calculating measure 3 : Average Depth
	double AvgDepth_Cluster[]= compute_AvgDepth();
	double SumAvgDepth=0;
	//System.out.println('\n'+"*** Details of Clustere's Average Depth:***");
	for (int i = 0; i< clusters.size(); i++  ){
		//System.out.println("Average Depth for Cluster "+ i+ " : "+ String.valueOf(new DecimalFormat("##.####").format(AvgDepth_Cluster[i])));
		SumAvgDepth= SumAvgDepth + AvgDepth_Cluster[i]*clusters.get(i).getSize();
	}
	double AvgAvgDepth= SumAvgDepth / size;
	//System.out.println("AvgDepth for all clusters in this ontology is: " + String.valueOf(new DecimalFormat("##.####").format(AvgAvgDepth)));
	
	
//////////////////////////////////////////////////////Calculating measure 4 : SrHM
  double srMH[]= computeSrMH();
  double sr=0;
   //System.out.println('\n'+"*** Details of Clustere's Average Depth:***");
   for (int i = 0; i< clusters.size(); i++  ){
	   //System.out.println("Average Depth for Cluster "+ i+ " : "+ String.valueOf(new DecimalFormat("##.####").format(srMH[i])));
	   sr= sr + srMH[i]*clusters.get(i).getSize();
   }
   double AvgAvsr= sr / size;
   //System.out.println("Avg SrHM for all clusters in this ontology is: " + String.valueOf(new DecimalFormat("##.####").format(AvgAvsr)));

   ///////////////////////////////////////////////////// Combination : HOMO
   //double goodness= (AvgLexL + AvgNTree + AvgAvgDepth + AvgAvsr) /4; 
   //System.out.println('\n'+"The module homogeneity for this clustering is " + String.valueOf(new DecimalFormat("##.####").format(goodness)));
   
   //System.out.println("the heterogeneity metrcis\n");
  ////////////////////////////////////////////////////// Calculating measure 5 : Detach Relation
   double DetachRel_Cluster[][]= DetachRel_func();
   double SumDetachRel=0;
   double [] sumDR = new double [clusters.size()];
	//System.out.println('\n'+"*** Details of Clustere's Detach Relation:***");
	for (int i=0; i<clusters.size(); i++)
	{
		for(int j=0;j<clusters.size();j++)
		{
			sumDR [i]=sumDR [i] + DetachRel_Cluster[i][j];
		}
		//System.out.println("Average Detach Relation for Cluster "+ i+ " : "+ String.valueOf(new DecimalFormat("##.####").format(sum)));
		SumDetachRel= SumDetachRel+ sumDR[i];
	}
	double AvgDetachRel= SumDetachRel / clusters.size();
	//System.out.println("AvgDepth for all clusters in this ontology is: "+ String.valueOf(new DecimalFormat("##.####").format(AvgDetachRel)));

	////////////////////////////////////////////////////// Calculating Relative Size
	double [] RS = RelativeSize_func();
	double sumRS = 0;
	for (int i = 0; i< clusters.size(); i++  ){
	   //System.out.println("RS for Cluster "+ i+ " : "+ String.valueOf(new DecimalFormat("##.####").format(RS[i])));
		sumRS = sumRS + RS[i];
	}
	  AvgRS= sumRS / size;
	   //System.out.println("Avg RS for all clusters in this ontology is: " + String.valueOf(new DecimalFormat("##.####").format(AvgRS)));


	///////////////////////////HoMo & HeMo for all
	HoMo = new double [clusters.size()];
	HeMo = new double [clusters.size()];
	for (int i = 0; i< clusters.size(); i++  ){
	  HoMo [i] = (MH[i] + NTree_Cluster[i] + srMH[i] + AvgDepth_Cluster[i]) / 4 ;
	  HeMo[i] = (RS[i] + sumDR[i])/2;
	  }
	
}

public double getRS()
{
	return AvgRS;
}

public double getHoMO()
{
	double sumHO=0;
	for(int i=0;i<clusters.size();i++)
		sumHO+=HoMo[i];
	return sumHO/clusters.size();
}

public double getHEMo()
{
	double sumHE=0;
	for(int i=0;i<clusters.size();i++)
		sumHE+=HeMo[i];
	return sumHE/clusters.size();
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////  compute the distance between SrMH

public  double[] computeSrMH()
{
     double [] SrMH =new double [clusters.size()];  
	     for(int i=0;i<clusters.size();i++)
	     {
	      Cluster cc=clusters.get(i);
	      Node ch=cc.getCH();
	      int d=0; int s=0;
	      Iterator<Node> it=  cc.getElements().values().iterator();
	      while(it.hasNext())
	      {
	       Node node=it.next(); 
	       if(list.contains(node.getLocalName())){d+=Math.abs(ch.getHierarchy()-node.getHierarchy());s++;};
	      }
	      if(d!=0 && s < d) 
	      SrMH[i]=s/(double)d;
	      else
	       SrMH[i]=1;
	     }
	    
	  return SrMH;
}

/////////////////////////////////////////////// Lexical Level function
   public  double [] computeMH()
   {
     double [] MH =new double [clusters.size()];
     WordNetMatcher matcher=new WordNetMatcher();
     double sum,sim;
     for (int k=0; k<clusters.size();k++)
     {
         sum=0;
    	 Cluster kCluster= clusters.get(k);
         Node CH=kCluster.getCH();
         Iterator<Node> Elements= kCluster.listElements();
         while(Elements.hasNext())
         {
        	 Node element=Elements.next(); sim=0;
        	 if(!CH.equals(element))
        	 {
        		sim=matcher.compute(CH.getLocalName(), element.getLocalName());
        	 }
        	 sum+=sim;
         }
         MH[k]=sum/ clusters.get(k).getSize();;  
       
     }
      return MH;
    }
/////////////////////////////////////////////RelativeSize_func
   private  double [] RelativeSize_func ()
   {
	   double [] RS = new double [clusters.size()];
	   
	   for(int i=0;i<clusters.size();i++)
	     {
		   int xsiz= clusters.get(i).getElements().size();
		   int ysiz=0; 
		   int sumSiz=0;
		  for (int j=0; j< clusters.size(); j++)
	      {
	    	  ysiz = clusters.get(j).getElements().size();
	    	  sumSiz = sumSiz + ysiz;
	    	  RS [i] = RS [i] + Math.abs(xsiz - ysiz)  ;
	     }
	      RS [i] = RS [i] /sumSiz;
		  //System.out.println("RS for Cluster "+ i+ " : "+ String.valueOf(new DecimalFormat("##.####").format(RS[i])));
	     }
	   
	   return RS;
   }
////////////////////////////////////////////// Number of Tree function
  private  void setSize()
  {
	  for(int i=0;i<clusters.size();i++)
	  {
		  size+=clusters.get(i).getSize();
	  }
	  
  }
  
  //////////////////////// 
  private  void setList()
  {
	  Iterator<OntClass> itt= MB.getModel().listNamedClasses();
	 
	  while(itt.hasNext())
	  {
		  OntClass cl=itt.next(); list.add(cl.getLocalName());
	  }
  }
  
  public  double[] computeNtree()
  {
	  numTree=CreateModule.numTree;
	  double Ntree[]=new double[numTree.length];
	  for(int i=0;i<clusters.size();i++)
	  {
		  Ntree[i]=numTree[i]/clusters.get(i).getSize();
	  }
	  return Ntree;
  }
  
  public  double [] compute_NTree()
  {
	double sum=0; 
	double NTree_Cluster[]=numTree;
	for(int i=0;i<models.size();i++)
	{
		sum+=NTree_Cluster[i];
		
	}
	
	for(int i=0;i<models.size();i++)
	{
		NTree_Cluster[i]/=sum;
	}
	return NTree_Cluster;
	
}
/////////////////////////////////////////////// Average Depth function *must be correct
public  double [] compute_AvgDepth(){
	double [] AvgDepth_Num = new double [clusters.size()];
	double tot_depth=0;
	for (int k=0; k<clusters.size();k++){
		Cluster kCluster= clusters.get(k);
		Iterator<Node> Elements= kCluster.listElements();
		double SumDepth = 0;
		for (int i=0; i<kCluster.getElements().size(); i++)
		{
			Node iNode= Elements.next();
			SumDepth +=  iNode.getHierarchy();
			
		}
		AvgDepth_Num[k] = SumDepth ; tot_depth+=SumDepth;
	}
	
	for (int k=0; k<clusters.size();k++)
	{
		AvgDepth_Num[k]=AvgDepth_Num[k]/tot_depth;
	}
	return AvgDepth_Num;
}
/////////////////////////////////////////////// Detach relation function *should be correct
public  double[][] DetachRel_func(){
	
	double [][] DetachRel_Num = new double [clusters.size()][clusters.size()];
	for (int i=0; i< clusters.size(); i++){
		Cluster icluster = clusters.get(i);
		List<Node> list1 = IteratorUtils.toList(icluster.listElements());
		DetachRel_Num[i][i]=0;
		for(int j=i+1;j<clusters.size();j++)
		{
			Cluster icluster1 = clusters.get(j);
			List<Node> list2 = IteratorUtils.toList(icluster1.listElements());
			//List<Node> intersect = list1.stream().filter(list2::contains).collect(Collectors.toList());
			List<Node> intersect=new ArrayList<Node>(list1);
			intersect.retainAll(list2);
			DetachRel_Num[i][j]=intersect.size()/(double)(list1.size()+list2.size());
		}
		
		}
					
	return DetachRel_Num;
}
				
	
////////////////////////////////////////////Size cluster
public  double [] Size_func(){
	double [] Size_Num = new double [clusters.size()];
	for (int k=0; k<clusters.size();k++){
		Size_Num [k]= clusters.get(k).getElements().size();
	}
	return Size_Num;
	
}
////////////////////////////////////////////////////////////////
public static boolean searchMethod (String iNode,  Cluster kCluster){
	boolean SearchAns = false;
	Iterator<Node> Elements= kCluster.listElements();
	for (int i=0; i<kCluster.getElements().size(); i++){
		if (iNode.equals(Elements.next().getLocalName()))
		{
			SearchAns= true;
		}
	}
	return SearchAns;
}
	
////////////////////////////////////////////////////////////////Normalization method
public static double [] Normalization_Method(double iArray[]){
	double Xmax= iArray[0], Xmin=iArray[0]; 
	
	for (int i=1; i<iArray.length; i++){
		if (iArray[i] > Xmax) Xmax= iArray[i];
		if (iArray[i] < Xmin) Xmin= iArray[i];
	}
	double DifferMaxMin= Xmax-Xmin;
	if (Xmax != 0 && Xmin != 0){
		for (int i=0; i<iArray.length; i++){
			iArray[i]= (iArray[i]-Xmin)/DifferMaxMin;
			iArray[i]= Math.round(iArray[i]*1000.0)/1000.0; //storing double value by precision of 3 digits not all 
		}
	}
	return iArray;
	
}
	}