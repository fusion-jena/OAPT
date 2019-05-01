
package fusion.oapt.general.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;


import org.apache.jena.util.iterator.ExtendedIterator;


import org.apache.jena.vocabulary.OWL;

import com.ibm.icu.text.DecimalFormat;

import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;
import fusion.oapt.model.RBGModel;

public class RichnessAnalysis
{
    public double avDesign;
    public double avKB;
    public double avClass;
    public double TotalRichness;
    OntModel model;
    RBGModel rbgm;
	public RichnessAnalysis()
	{
		model=BuildModel.OntModel;
		rbgm=BuildModel.rbgmModel;
		avDesign=0;
		avKB=0;
		avClass=0;
		TotalRichness=0;
	}
	
	public RichnessAnalysis(OntModel mod)
	{
		//model=BuildModel.OntModel;
		//rbgm=BuildModel.rbgmModel;
		avDesign=0;
		avKB=0;
		avClass=0;
		TotalRichness=0;
		this.model=mod;
	}
	
	public RichnessAnalysis(OntModel mod, RBGModel rbg)
	{
		rbgm=rbg;
		model=mod;
		avDesign=0;
		avKB=0;
		avClass=0;
		TotalRichness=0;
	}
   public void RichnessTest1(){
	 List list=new ArrayList();
	 list=model.listClasses().toList();
	 int countSub=0;
	 for(int i=0;i<list.size();i++)
	    {
	    	OntClass cls=(OntClass) list.get(i);  
	    	ExtendedIterator it=cls.listSubClasses();    
	    	while(it.hasNext())
	    	{
	    		countSub++;
	    		it.next();
	    	}
	    }
	 int NumClass=list.size();
	 OPATgui.RichnessTextArea1.append("============ Ontology design  metrics========= " +"\n");
	 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 int llist=model.listAllOntProperties().toList().size();
	 list=model.listObjectProperties().toList();
	 float RelationshipRichness= 0;
	 int NumRelation=list.size();
	 RelationshipRichness = (float) NumRelation / (float) (countSub + llist); 			
	 OPATgui.RichnessTextArea1.append("\n ---> Relationship richness:\t " + String.valueOf(new DecimalFormat("##.####").format(RelationshipRichness)) +"\n");
	 
	 /////////////////////////////////////////////////////////
	  list=model.listDatatypeProperties().toList();
	  float attRich=list.size()/(float)(countSub +llist);
	  OPATgui.RichnessTextArea1.append("\n ---> Attribute relationship richness is:\t " + String.valueOf(new DecimalFormat("##.####").format(attRich)) +"\n");
	  
	  /////////////////////////////////////////////////////////////////////////////
 	 float InheritanceRichness = (float) countSub / (countSub + llist);
	 OPATgui.RichnessTextArea1.append("\n ---> Inheritance richness:\t  "+ String.valueOf(new DecimalFormat("##.####").format(InheritanceRichness)) +"\n");
     avDesign= (RelationshipRichness+attRich+InheritanceRichness)/3.0;
     OPATgui.RichnessTextArea1.append("\n \t \t the average ontolgy design metric:\t"+String.valueOf(new DecimalFormat("##.####").format(avDesign))+"\n");
	////////////////////////////////////////////////////////////////////////////////////////////
		 
	 OPATgui.RichnessTextArea1.append("\n ==================== KB metrics  =============" +"\n");  
	 list=model.listClasses().toList();
	 int Incount=rbgm.getInstanceNodes().size();
	/* for(int i=0;i<list.size();i++)
	 {
		 OntClass node=(OntClass) list.get(i);
		 ExtendedIterator li=model.listIndividuals(node);
		 for (ExtendedIterator j =model.listIndividuals(node); 
			       j.hasNext(); ) {
			  Individual instance = (Individual) j.next();
			  Incount++;
			}
			 
	 }*/
	 float classRich =  Incount / (float)NumClass;
	 if(classRich>1) classRich=1;
	 OPATgui.RichnessTextArea1.append("\n ---> Class richness:\t  "+ String.valueOf(new DecimalFormat("##.####").format(classRich)) +"\n");
     double PopulationRichness = 0;
     int NumInstanceNode = model.listIndividuals().toList().size();  	 
	 if (NumClass > 0 ) {
		 PopulationRichness = (double) NumInstanceNode / (double) NumClass;
	 }
	 if(PopulationRichness>1) PopulationRichness=1;
	 OPATgui.RichnessTextArea1.append("\n ---> Average population richness:\t " + String.valueOf(new DecimalFormat("##.####").format(PopulationRichness)) +"\n");
	
	 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	 double ReadabilityRichness = 0;
     int NumComment =0; int NumLabel =0;
	 Iterator<Node> listclass2 = rbgm.listClassNodes();
	 while (listclass2.hasNext()){
		 Node iclass = listclass2.next();
		 if (iclass.getComment() != null) {
			 NumComment ++;
		 }
		 if (iclass.getLabel() != null) {
			 NumLabel ++;
		 }
	 }
	 ReadabilityRichness = (NumComment + NumLabel)/(float) NumClass;
	 if(ReadabilityRichness>1) ReadabilityRichness=1;
	 OPATgui.RichnessTextArea1.append("\n ----> The Readability Richness is:\t " + String.valueOf(new DecimalFormat("##.####").format(ReadabilityRichness)) +"\n");
	 
	 avKB=(PopulationRichness+classRich+ReadabilityRichness)/3.0;
	 OPATgui.RichnessTextArea1.append("\n \t\t the average KB metric:\t"+String.valueOf(new DecimalFormat("##.####").format(avKB))+"\n");
      ////////////////////////////////////////////////////////////////////////////////////////////
	 
	 OPATgui.RichnessTextArea1.append("\n ==================== Class metrics ===========" +"\n"); 
		
     //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
	
	 double ConnectedRichness = 0;
	 int NumRootClass=model.listHierarchyRootClasses().toList().size();//ithing.listSubClasses().toList().size();
	 ConnectedRichness = (double) (NumClass - NumRootClass) / (double) NumClass;
	 OPATgui.RichnessTextArea1.append("\n ---> The Connected componnet cohesion is:\t " + String.valueOf(new DecimalFormat("##.####").format(ConnectedRichness)) +"\n" );
	
	 float [] ImportnaceClass = new float [NumClass]; 
	 Iterator<Node> listclass = rbgm.listClassNodes();   
	 for (int i=0; i<NumClass; i++){
		 int NumInsClassi = 0;
		 Node iclass = listclass.next();
		 NodeList listParent = iclass.getNamedSupers();
		 if (listParent != null){
			 for (int j=0; j<listParent.size(); j++){
				 Node iParent = listParent.get(j);
			 	 Iterator<Node> listInstance = rbgm.listInstanceNodes();
			 	 while (listInstance.hasNext()){
			 		 Node InsN = listInstance.next();
			 		  if (InsN== iParent) NumInsClassi ++; //we should calculate the number of instance of class i that exist in the root of class i
			 		 }
			 }
			 if (NumInstanceNode >0) ImportnaceClass[i] = (float) NumInsClassi / (float) NumInstanceNode;
		 }
	 }
	 float sumImp = 0;
	 for(int i=0; i<NumClass; i++){
		 sumImp = sumImp + ImportnaceClass[i];
	 }
	 double AvgImprotnace = sumImp / (float)NumClass; 	 //To calculate the average importance of classes ontology
	 OPATgui.RichnessTextArea1.append("\n ----> The Average Importance class is:\t  " + String.valueOf(new DecimalFormat("##.####").format(AvgImprotnace)) +"\n");
	 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	 float ConceptRelativesRichness =0;
	 float SumAdjN =0; 
	 Iterator<Node> listclass3 = rbgm.listClassNodes();
	 while (listclass3.hasNext()){
		 Node iclass = listclass3.next();
		 int CRRi = 0;
		 NodeList subN = iclass.getNamedSubs(); //instead of it we can use getAdjacentNodes?!
		 if (subN != null){
			 CRRi = CRRi + subN.size();
			 }
		 NodeList supN = iclass.getNamedSupers(); //instead of it we can use getAdjacentNodes?!
		 if (supN != null){
			 CRRi = CRRi + supN.size();
			 //calculating the number of iclass sibling i.e. calculate the number of children for all icalss parents
			 for (int i=0; i<supN.size(); i++){
				 Node iParent= supN.get(i);
				 NodeList allParent= iParent.getNamedSupers();
				 if (allParent != null){
					 CRRi= CRRi + allParent.size();
				 }
			 }
		 }
		 SumAdjN = SumAdjN + ((float) (CRRi)/NumClass);		 
	 }
	 ConceptRelativesRichness = SumAdjN /(float) NumClass;
	 OPATgui.RichnessTextArea1.append("\n ---> The Concept relatives Richness is:\t "+ String.valueOf(new DecimalFormat("##.####").format(ConceptRelativesRichness)) +"\n");
	 	
	avClass=(ConceptRelativesRichness+ConnectedRichness)/2.0;
	OPATgui.RichnessTextArea1.append("\n \t\t the average class metric:\t"+String.valueOf(new DecimalFormat("##.####").format(avClass))+"\n");

	//Total Richness
	TotalRichness =  (avClass + avKB + avDesign)/3.0;
	 OPATgui.RichnessTextArea1.append("\n ==================== total ontology richness ===========" +"\n"); 
	OPATgui.RichnessTextArea1.append('\n'+ "\t\t The ontology richness:\t  "+ String.valueOf(new DecimalFormat("##.####").format(TotalRichness)) +"\n");
	 
	 } 	 

   public double computeDesignMertic()
   {
	     int NumClass=model.listClasses().toList().size();// .listClasses().toList().size();
	     //List list=model.listNamedClasses().toList();  
	     List list=model.listClasses().toList();
		 int countSub=0;
		 for(int i=0;i<list.size();i++)
		    {
		    	OntClass cls=(OntClass) list.get(i);   
		    	ExtendedIterator it=cls.listSubClasses();    
		    	while(it.hasNext())
		    	{
		    		countSub++;
		    		it.next();
		    	}
		    }
	     list=model.listObjectProperties().toList();
	     float RelationshipRichness= 0;
		 int NumRelation=list.size();
		 RelationshipRichness = (float) NumRelation / (float) (countSub + NumRelation); 			
		 list=model.listDatatypeProperties().toList();
		 float attRich=list.size()/(float)(countSub + list.size());
		 float InheritanceRichness = (float) countSub / (countSub + NumRelation); 
		 if(InheritanceRichness>1) InheritanceRichness=1;
		 avDesign= (0.3*RelationshipRichness+0.3*attRich+0.4*InheritanceRichness); 
		 avDesign = Math.round(avDesign*100)/100.0d;
		 return avDesign;
   }
   
   public double computeKBMetric()
   {
	    //int NumClass=model.listNamedClasses().toList().size();
	    int NumClass=model.listClasses().toList().size();
	    int instNodes=rbgm.getInstanceNodes().size();
	    float classRich =0;//  instNodes / (float)NumClass;
	   // if(classRich>1) classRich=1;
		double PopulationRichness = 0;
	     int NumInstanceNode = model.listIndividuals().toList().size();  	 
		 if (NumClass > 0 ) {
			 PopulationRichness = (double) NumInstanceNode / (double) NumClass;
		 }
		 if(PopulationRichness>1) PopulationRichness=1;
		 
		////////////////////////////////////////
		 double ReadabilityRichness = 0;
	     int NumComment =0; int NumLabel =0;
		 Iterator<Node> listclass2 = rbgm.listClassNodes();   
		 while (listclass2.hasNext()){
			 Node iclass = listclass2.next();     
			 if (iclass.getComment() != null) {
				 NumComment ++;
			 }
			 if (iclass.getLabel() != null) {
				 NumLabel ++;
			 }
		 }
		 int annlist=model.listAnnotationProperties().toList().size();
		 ReadabilityRichness = (NumComment + NumLabel+annlist)/(float) NumClass;
		 if(ReadabilityRichness>1) ReadabilityRichness=1; 
		 avKB=(PopulationRichness + classRich + ReadabilityRichness)/3.0;
		 avKB = Math.round(avKB*100)/100.0d;
		 return avKB;
   }
   
   public double computeClassmetric()
   {
	    int NumClass=model.listClasses().toList().size();
	    //int NumClass=model.listNamedClasses().toList().size();
	    double ConnectedRichness = 0;
		int NumRootClass=model.listHierarchyRootClasses().toList().size();
		ConnectedRichness = (double) (Math.abs(NumClass - NumRootClass)) / (double) NumClass;
		if(ConnectedRichness> 1)  ConnectedRichness=1;
		float SumAdjN =0; 
		Iterator<Node> listclass3 = rbgm.listClassNodes();
		while (listclass3.hasNext()){
			 Node iclass = listclass3.next();    
			 int CRRi = 0;
			 NodeList subN = iclass.getNamedSubs(); //instead of it we can use getAdjacentNodes?!
			 if (subN != null){
				 CRRi = CRRi + subN.size();
				 }
			 NodeList supN = iclass.getNamedSupers(); //instead of it we can use getAdjacentNodes?!
			 if (supN != null){
				 CRRi = CRRi + supN.size();
				 //calculating the number of iclass sibling i.e. calculate the number of children for all icalss parents
				 for (int i=0; i<supN.size(); i++){
					 Node iParent= supN.get(i);
					 NodeList allParent= iParent.getNamedSupers();
					 if (allParent != null){
						 CRRi= CRRi + allParent.size();
					 }
				 }
			 }
			 SumAdjN +=  CRRi*(float) (CRRi)/NumClass;		 
		 }
		SumAdjN/=NumClass;
		if(SumAdjN>1) SumAdjN=1;
		avClass= (SumAdjN+ConnectedRichness)/2;
		avClass = Math.round(avClass*100)/100.0d;
		return avClass;
   }
   
   public double computeRichness()
   {
	   TotalRichness =  (avClass + avKB + avDesign)/3.0;
	   TotalRichness = Math.round(TotalRichness*100)/100.0d;
	   return TotalRichness;
   }
 //------------------------------------------------------------------------------------------------------------------
   public void RichnessTest2(){
		 List list=new ArrayList();
		 list=model.listClasses().toList();
		 int countSub=0;
		 for(int i=0;i<list.size();i++)
		    {
		    	OntClass cls=(OntClass) list.get(i);  
		    	ExtendedIterator it=cls.listSubClasses();    
		    	while(it.hasNext())
		    	{
		    		countSub++;
		    		it.next();
		    	}
		    }
		 int NumClass=list.size();
		 OPATgui.RichnessTextArea2.append("============ Ontology design  metrics========= " +"\n");
		 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 list=model.listObjectProperties().toList();
		 float RelationshipRichness= 0;
		 int NumRelation=list.size();
		 RelationshipRichness = (float) NumRelation / (float) (countSub + NumRelation); 			
		 OPATgui.RichnessTextArea2.append("\n ---> Relationship richness:\t " + String.valueOf(new DecimalFormat("##.####").format(RelationshipRichness)) +"\n");
		 
		 /////////////////////////////////////////////////////////
		  list=model.listDatatypeProperties().toList();
		  float attRich=list.size()/(float)NumClass;
		  OPATgui.RichnessTextArea2.append("\n ---> Attribute relationship richness is:\t " + String.valueOf(new DecimalFormat("##.####").format(attRich)) +"\n");
		  
		  /////////////////////////////////////////////////////////////////////////////
	 	 float InheritanceRichness = (float) countSub / NumClass;
		 OPATgui.RichnessTextArea2.append("\n ---> Inheritance richness:\t  "+ String.valueOf(new DecimalFormat("##.####").format(InheritanceRichness)) +"\n");
	     double avDesign= (RelationshipRichness+attRich+InheritanceRichness)/3.0;
	     OPATgui.RichnessTextArea2.append("\n the average ontolgy design metric:\t"+String.valueOf(new DecimalFormat("##.####").format(avDesign))+"\n");
		////////////////////////////////////////////////////////////////////////////////////////////
			 
		 OPATgui.RichnessTextArea2.append("\n ==================== KB metrics  =============" +"\n");  
		 list=model.listClasses().toList();
		 int Incount=0;
		 for(int i=0;i<list.size();i++)
		 {
			 OntClass node=(OntClass) list.get(i);
			 ExtendedIterator li=model.listIndividuals(node);
			 for (ExtendedIterator j =model.listIndividuals(node); 
				       j.hasNext(); ) {
				  Individual instance = (Individual) j.next();
				  Incount++;
				}
				 
		 }
		 float classRich =  Incount / (float)NumClass;
		 OPATgui.RichnessTextArea2.append("\n ---> Class richness:\t  "+ String.valueOf(new DecimalFormat("##.####").format(classRich)) +"\n");
	     double PopulationRichness = 0;
	     int NumInstanceNode = model.listIndividuals().toList().size();  	 
		 if (NumClass > 0 ) {
			 PopulationRichness = (double) NumInstanceNode / (double) NumClass;
		 }
		 OPATgui.RichnessTextArea2.append("\n ---> Average population richness:\t " + String.valueOf(new DecimalFormat("##.####").format(PopulationRichness)) +"\n");
		
		 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 double ConnectedRichness = 0;
		 OntClass ithing=model.getOntClass(OWL.Thing.getURI());
		 int NumRootClass=ithing.listSubClasses().toList().size();
		 ConnectedRichness = (double) (NumClass - NumRootClass) / (double) NumClass;
		 OPATgui.RichnessTextArea2.append("\n ---> The Connected componnet cohesion is:\t " + String.valueOf(new DecimalFormat("##.####").format(ConnectedRichness)) +"\n" );
		 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 double ReadabilityRichness = 0;
	     int NumComment =0; int NumLabel =0;
		 Iterator<Node> listclass2 = rbgm.listClassNodes();
		 while (listclass2.hasNext()){
			 Node iclass = listclass2.next();
			 if (iclass.getComment() != null) {
				 NumComment ++;
			 }
			 if (iclass.getLabel() != null) {
				 NumLabel ++;
			 }
		 }
		 ReadabilityRichness = (NumComment + NumLabel)/(float) NumClass;
		 OPATgui.RichnessTextArea2.append("\n ----> The Readability Richness is:\t " + String.valueOf(new DecimalFormat("##.####").format(ReadabilityRichness)) +"\n");
		 
		 double avKB=(ConnectedRichness+PopulationRichness+classRich+ReadabilityRichness)/4.0;
		 OPATgui.RichnessTextArea2.append("\n \t\t the average KB metric:\t"+String.valueOf(new DecimalFormat("##.####").format(avKB))+"\n");
	      ////////////////////////////////////////////////////////////////////////////////////////////
		 
		 OPATgui.RichnessTextArea2.append("\n ==================== Class metrics ===========" +"\n"); 
			
	     //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////// 
		 float [] ImportnaceClass = new float [NumClass]; 
		 Iterator<Node> listclass = rbgm.listClassNodes();
		 for (int i=0; i<NumClass; i++){
			 int NumInsClassi = 0;
			 Node iclass = listclass.next();
			 NodeList listParent = iclass.getNamedSupers();
			 if (listParent != null){
				 for (int j=0; j<listParent.size(); j++){
					 Node iParent = listParent.get(j);
				 	 Iterator<Node> listInstance = rbgm.listInstanceNodes();
				 	 while (listInstance.hasNext()){
				 		 Node InsN = listInstance.next();
				 		  if (InsN== iParent) NumInsClassi ++; //we should calculate the number of instance of class i that exist in the root of class i
				 		 }
				 }
				 if (NumInstanceNode >0) ImportnaceClass[i] = (float) NumInsClassi / (float) NumInstanceNode;
			 }
		 }
		 float sumImp = 0;
		 for(int i=0; i<NumClass; i++){
			 sumImp = sumImp + ImportnaceClass[i];
		 }
		 float AvgImprotnace = sumImp / (float)NumClass; 	 //To calculate the average importance of classes ontology
		 OPATgui.RichnessTextArea2.append("\n ----> The Average Importance class is:\t  " + String.valueOf(new DecimalFormat("##.####").format(AvgImprotnace)) +"\n");
		 ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		 float ConceptRelativesRichness =0;
		 float SumAdjN =0; 
		 Iterator<Node> listclass3 = rbgm.listClassNodes();
		 while (listclass3.hasNext()){
			 Node iclass = listclass3.next();
			 int CRRi = 0;
			 NodeList subN = iclass.getNamedSubs(); //instead of it we can use getAdjacentNodes?!
			 if (subN != null){
				 CRRi = CRRi + subN.size();
				 }
			 NodeList supN = iclass.getNamedSupers(); //instead of it we can use getAdjacentNodes?!
			 if (supN != null){
				 CRRi = CRRi + supN.size();
				 //calculating the number of iclass sibling i.e. calculate the number of children for all icalss parents
				 for (int i=0; i<supN.size(); i++){
					 Node iParent= supN.get(i);
					 NodeList allParent= iParent.getNamedSupers();
					 if (allParent != null){
						 CRRi= CRRi + allParent.size();
					 }
				 }
			 }
			 SumAdjN = SumAdjN + ((float) (CRRi)/NumClass);		 
		 }
		 ConceptRelativesRichness = SumAdjN /(float) NumClass;
		 OPATgui.RichnessTextArea2.append("\n ---> The Concept relatives Richness is:\t "+ String.valueOf(new DecimalFormat("##.####").format(ConceptRelativesRichness)) +"\n");
		 	
		double avClass=(ConceptRelativesRichness+AvgImprotnace)/2.0;
		OPATgui.RichnessTextArea2.append("\n  \t\t the average class metric:\t"+String.valueOf(new DecimalFormat("##.####").format(avClass))+"\n");

		//Total Richness
		double TotalRichness =  (avClass + avKB + avDesign)/3.0;
		 OPATgui.RichnessTextArea2.append("\n ==================== total ontology richness ===========" +"\n"); 
		OPATgui.RichnessTextArea2.append('\n'+ "\t\t The ontology richness:\t  "+ String.valueOf(new DecimalFormat("##.####").format(TotalRichness)) +"\n");
		 
		 } 	 




}



