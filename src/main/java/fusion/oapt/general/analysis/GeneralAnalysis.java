
package fusion.oapt.general.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.ontology.ConversionException;
import org.apache.jena.ontology.FunctionalProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.LiteralRequiredException;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;

import com.hp.hpl.jena.ontology.Ontology;


//import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.gui.OPATgui;

public class GeneralAnalysis
{
    private int Number_of_classes;
    private int Number_of_tot_classes;
    private int Number_of_Property;
    private int Numbr_of_Object_pro;
    private int Number_of_data_pro;
    private int Number_of_fun_pro;
    private int Number_of_individual;
    private int Number_of_Stat;
    private int Number_of_subject;
    private int Number_of_object;
    private int Number_of_subClasses;
    private int Number_of_SupClasses;
    private int Number_of_comments;
    private int Number_of_labels;
    private int Number_of_EquiClasses;
    private int Number_of_disClasses;
    private int Number_of_subObjectPro;
    private int Number_of_EqObject_pro;
    private int Number_of_obj_pro_domain;
    private int Number_of_obj_pro_range;
    private int Number_of_inver_ob_pro;
    private int Number_of_fun_ob_pro;
    private int Number_ofinv_ob_pro;
    private int Number_of_fun_ob_pro_domain;
    private int Number_of_fun_ob_pro_range;
    OntModel model;
	  
	  public GeneralAnalysis()
	  {
		  Number_of_classes=0;
		  Number_of_tot_classes=0;
		  Number_of_Property=0;
		  Numbr_of_Object_pro=0;
		  Number_of_data_pro=0;
		  Number_of_fun_pro=0;
		  Number_of_individual=0;
		  Number_of_Stat=0;
		  Number_of_subject=0;
		  Number_of_object=0;
		  Number_of_comments=0;
		  Number_of_labels=0;
		  Number_of_subClasses=0;
		  Number_of_SupClasses=0;
		  Number_of_EquiClasses=0;
		  Number_of_disClasses=0;
		  Number_of_subObjectPro=0;
		  Number_of_EqObject_pro=0;
		  Number_of_obj_pro_domain=0;
		  Number_of_obj_pro_range=0;
		  Number_of_inver_ob_pro=0;
		  Number_of_fun_ob_pro=0;
		  Number_ofinv_ob_pro=0;
		  Number_of_fun_ob_pro_domain=0;
		  Number_of_fun_ob_pro_range=0;
		  model=null;//BuildModel.OntModel;
	  }
	  
	  public GeneralAnalysis(OntModel ont)
	  {
		  Number_of_classes=0;
		  Number_of_tot_classes=0;
		  Number_of_Property=0;
		  Numbr_of_Object_pro=0;
		  Number_of_data_pro=0;
		  Number_of_fun_pro=0;
		  Number_of_individual=0;
		  Number_of_Stat=0;
		  Number_of_subject=0;
		  Number_of_object=0;
		  Number_of_comments=0;
		  Number_of_labels=0;
		  Number_of_subClasses=0;
		  Number_of_SupClasses=0;
		  Number_of_EquiClasses=0;
		  Number_of_disClasses=0;
		  Number_of_subObjectPro=0;
		  Number_of_EqObject_pro=0;
		  Number_of_obj_pro_domain=0;
		  Number_of_obj_pro_range=0;
		  Number_of_inver_ob_pro=0;
		  Number_of_fun_ob_pro=0;
		  Number_ofinv_ob_pro=0;
		  Number_of_fun_ob_pro_domain=0;
		  Number_of_fun_ob_pro_range=0;
		  model=ont;
	  }
	  
	 ///// A set of get methods  
	  public int getNumComments()
	  {
		  return  Number_of_comments;
	  }
	  
	  public int getNumLabels()
	  {
		  return  Number_of_labels;
	  }
	  
	  public int getNumClass()
	  {
		  return  Number_of_classes;
	  }
	  
	  public int getTotnumClass()
	  {
		  return Number_of_tot_classes;
	  }
	  
	  public int getNumSubClass()
	  {
		  return  Number_of_subClasses;
	  }
	  
	  public int getNumEqClass()
	  {
		  return Number_of_EquiClasses;
	  }
	  
	  public int getNumDisClass()
	  {
		  return  Number_of_disClasses;
	  }
	  public int getNumProp()
	  {
		  return Number_of_Property;
	  }
	  
	  public int getNumIndivial()
	  {
		  return Number_of_individual;
	  }
	  public int getNumObectPro()
	  {
		  return Numbr_of_Object_pro;
	  }
	  
	  public int getNumDataPro()
	  {
		  return Number_of_data_pro;
	  }
	  
	  public int getFunProp()
	  {
		  return Number_of_fun_ob_pro;
	  }
	  
	
	  public int getNumStat()
	  {
		  return Number_of_Stat;
	  }
	  ///// a method to analysis the given ontology extracting its features
	  @SuppressWarnings("unchecked")
	public void computeStatics() throws ConversionException
	  {
		  List list=new ArrayList();      
		  if(!(model==null)){
		  list=model.listNamedClasses().toList();
		  Number_of_classes=list.size(); list=null;
		  list=model.listClasses().toList();
		  Number_of_tot_classes=list.size(); list=null;
		  list=model.listAllOntProperties().toList();
		  Number_of_Property=list.size();   list=null;
		  list= model.listDatatypeProperties().toList();
		  Number_of_data_pro=list.size(); list=null;
		  list=  model.listObjectProperties().toList();
		  Numbr_of_Object_pro=list.size();  list=null;
		  list=model.listFunctionalProperties().toList();
		  Number_of_fun_pro=list.size();   list=null;
		  list=model.listIndividuals().toList();
		  Number_of_individual=list.size(); list=null;
		   list=model.listStatements().toList();
		  Number_of_Stat=list.size(); list=null;
		  
		  list=model.listSubjects().toList();
		  Number_of_subject=list.size(); list=null;
		  
		  list=model.listObjects().toList();
		  Number_of_object=list.size(); list=null;
		  List<OntClass> list1=new ArrayList<OntClass>(); 
		  list1=model.listNamedClasses().toList();// listClasses().toList();
		     int countSub=0, countEq=0, countDis=0;
		     for(int i=0;i<list1.size();i++)
		     {
		    	OntClass cls= list1.get(i);   
		    	if(cls.isClass()){
		    	ExtendedIterator it=cls.listSubClasses();    
		    	ExtendedIterator  it2=cls.listEquivalentClasses();
		    	ExtendedIterator it3=cls.listDisjointWith();
		    	try{
		    	String lab=cls.getLabel(null);  
		    	String com=cls.getComment(null);
		    	if(lab!=null)
		    		Number_of_labels++;
		    	if(com!=null)
		    		Number_of_comments++;
		    	}
		    	catch(LiteralRequiredException e){e.printStackTrace();}
		    	while(it.hasNext())
		    	{
		    		countSub++;
		    		it.next();
		    	}
		    	
		    	while(it2.hasNext())
		    	{
		    		countEq++;
		    		it2.next();
		    	}
		    	
		    	while(it3.hasNext())
		    	{
		    		countDis++;
		    		it3.next();
		    	}
		    	}	
		    }
		    OntClass thing = model.getOntClass(OWL.Thing.getURI() ); 
		    list=thing.listSubClasses(true).toList();
		    Number_of_subClasses=countSub;
		    Number_of_EquiClasses=countEq;
		    Number_of_disClasses=countDis;
		    List<ObjectProperty> list2=new ArrayList<ObjectProperty>();
		    list2=model.listObjectProperties().toList();
		    int countSo=0, countEo=0, countIo=0,countDo=0,countRo=0;
		    for(int i=0;i<list2.size();i++)
		    {
		    	ObjectProperty obpr= list2.get(i);	
		    	ExtendedIterator it=obpr.listSubProperties(); 
		    	ExtendedIterator  it2=obpr.listEquivalentProperties(); 
		    	ExtendedIterator it3=obpr.listInverse();
		    	ExtendedIterator it4=obpr.listDomain();
		    	ExtendedIterator it5=obpr.listRange();   
		    	while(it.hasNext())
		    	{
		    		countSo++;
		    		it.next();
		    	}
		    	
		    	while(it2.hasNext())
		    	{
		    		countEo++;
		    		it2.next();
		    	}
		    	
		    	while(it3.hasNext())
		    	{
		    		countIo++;
		    		it3.next();
		    	}
		    	
		    	while(it4.hasNext())
		    	{
		    		countDo++;
		    		it4.next();
		    		
		    	}
		    	
		    	while(it5.hasNext())
		    	{
		    		countRo++;
		    		it5.next();
		    	}
		    	
		    }
		    Number_of_subObjectPro=countSo;
		    Number_of_EqObject_pro=countEo;
		    Number_of_obj_pro_domain=countDo;
		    Number_of_obj_pro_range=countRo;
		    Number_of_inver_ob_pro=countIo;
		    
		    list=null;
		    list=model.listFunctionalProperties().toList();   
		    int countIf=0,countDf=0,countRf=0;
		    for(int i=0;i<list.size();i++)
		    {
		    	FunctionalProperty fpr=(FunctionalProperty) list.get(i);	  
		    	
		    	ExtendedIterator it3=fpr.listInverse();
		    	ExtendedIterator it4=fpr.listDomain();
		    	ExtendedIterator it5=fpr.listRange();   
		    	
		    	
		    	while(it3.hasNext())
		    	{
		    		countIf++;
		    		it3.next();
		    	}
		    	
		    	while(it4.hasNext())
		    	{
		    		countDf++;
		    		it4.next();
		    		
		    	}
		    	
		    	while(it5.hasNext())
		    	{
		    		countRf++;
		    		it5.next();
		    	}
		    	
		    }
		    Number_of_fun_ob_pro=list.size();
		    Number_ofinv_ob_pro=countIf;
			Number_of_fun_ob_pro_domain=countDf;
			Number_of_fun_ob_pro_range=countRf;
	  }}
	
 public void GeneralTest(){
	  	 
	 List list=new ArrayList();
	 OPATgui.GeneralTextArea1.append("==============  Statistical metrics ==========  \n" );
	 	 
	 OPATgui.GeneralTextArea1.append("\n" + "---> Number of named classes:\t " + Number_of_classes +"\n") ; 
	 	  
	 OPATgui.GeneralTextArea1.append("\n" + "---> Number of all classes (+ blank nodes):\t " + Number_of_tot_classes +"\n") ; list=null;
	 
	 
	 OPATgui.GeneralTextArea1.append("\n---> Number of all properties:\t " + Number_of_Property +"\n"); 
	
	 
	 OPATgui.GeneralTextArea1.append("\n" + "---> Number of data properties:\t " + Number_of_data_pro+"\n") ; 

	 
     OPATgui.GeneralTextArea1.append("\n" + "---> Number of Object properties:\t " + Numbr_of_Object_pro+"\n") ;
	
    
	 OPATgui.GeneralTextArea1.append("\n" + "---> Number of functional properties:\t " + Number_of_fun_pro +"\n") ; 
	
	 
     OPATgui.GeneralTextArea1.append("\n" + "---> Number of individuals:\t " +  Number_of_individual+"\n") ; 
    
     
     OPATgui.GeneralTextArea1.append("\n" + "---> Numebr of statements:\t " + Number_of_Stat+"\n") ;

     
     OPATgui.GeneralTextArea1.append("\n" + "---> Number of subjects:\t " + Number_of_subject+"\n") ; 

     
     OPATgui.GeneralTextArea1.append("\n" + "---> Number of objects:\t " + Number_of_object+"\n") ; 
     
     OPATgui.GeneralTextArea1.append("\n" + "---> Number of comments:\t " + Number_of_comments+"\n") ; 
     OPATgui.GeneralTextArea1.append("\n" + "---> Number of labels:\t " + Number_of_labels+"\n") ;
     
     
     OPATgui.GeneralTextArea1.append("\n"+"==============  Class axiom metrics ==========  \n" );
	
     
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of sub classes:\t " +  Number_of_subClasses+"\n") ; 
    
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of equivalent classes:\t " +  Number_of_EquiClasses+"\n") ; 
    
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of disjoint classes:\t " + Number_of_disClasses+"\n") ; 
    
    
    
	OPATgui.GeneralTextArea1.append("\n"+"==============  Object property axiom metrics ==========  \n" );
	
   
    
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of sub object properties:\t " + Number_of_subObjectPro+"\n") ; 
    
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of equivalent object properties:\t " + Number_of_EqObject_pro+"\n") ; 
    
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of inverse object properties:\t " + Number_of_inver_ob_pro+"\n") ; 
    
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of  object properties domain:\t " + Number_of_obj_pro_domain+"\n") ;
    
    OPATgui.GeneralTextArea1.append("\n" + "---> Number of object properties range:\t " + Number_of_obj_pro_range+"\n") ; 
	
	
	
	 OPATgui.GeneralTextArea1.append("\n"+"============== Functional property axiom metrics ==========  \n" );
	 
	
	    
	    OPATgui.GeneralTextArea1.append("\n" + "---> Number of object functional properties:\t " + Number_of_fun_ob_pro+"\n") ;  
	    
	    OPATgui.GeneralTextArea1.append("\n" + "---> Number of inverse object functional properties:\t " + Number_ofinv_ob_pro+"\n") ; 
	    
	    OPATgui.GeneralTextArea1.append("\n" + "---> Number of  object properties domain:\t " + Number_of_fun_ob_pro_domain+"\n") ;
	    
	    OPATgui.GeneralTextArea1.append("\n" + "---> Number of object properties range:\t " + Number_of_fun_ob_pro_range+"\n") ; 
		
	 
	 
	    OPATgui.GeneralTextArea1.append("\n"+"==============  Individual axiom metrics ==========  \n" );
	 
	 
	    OPATgui.GeneralTextArea1.append("\n"+"==============  Imported Ontologies ==========  \n" );
	   // OntModel m =model;
	 /*   OntModel mBase = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, model.getBaseModel() );
	    for (Iterator i = mBase.listOntologies(); i.hasNext(); ) {
			     Ontology ont = (Ontology) i.next();
			     OPATgui.GeneralTextArea1.append("\n"+"======"+ ont.getLocalName() +"\n" );
			 }
	    if(mBase.listOntologies().toList().size()==0)
		   OPATgui.GeneralTextArea1.append("\n"+"The ontology contains no imported ontologies \n" ); */
 	} 
 
 // used only for comparing ontologies
 public void GeneralTest2(){
  	 
	 List list=new ArrayList();
	 OPATgui.GeneralTextArea2.append("==============  Statistical metrics ==========  \n" );
	 	 
	 OPATgui.GeneralTextArea2.append("\n" + "---> Number of named classes:\t " + Number_of_classes +"\n") ; 
	 	  
	 OPATgui.GeneralTextArea2.append("\n" + "---> Number of all classes (+ blank nodes):\t " + Number_of_tot_classes +"\n") ; list=null;
	 
	 
	 OPATgui.GeneralTextArea2.append("\n---> Number of all properties:\t " + Number_of_Property +"\n"); 
	
	 
	 OPATgui.GeneralTextArea2.append("\n" + "---> Number of data properties:\t " + Number_of_data_pro+"\n") ; 

	 
     OPATgui.GeneralTextArea2.append("\n" + "---> Number of Object properties:\t " + Numbr_of_Object_pro+"\n") ;
	
    
	 OPATgui.GeneralTextArea2.append("\n" + "---> Number of functional properties:\t " + Number_of_fun_pro +"\n") ; 
	
	 
     OPATgui.GeneralTextArea2.append("\n" + "---> Number of individuals:\t " +  Number_of_individual+"\n") ; 
    
     
     OPATgui.GeneralTextArea2.append("\n" + "---> Numebr of statements:\t " + Number_of_Stat+"\n") ;

     
     OPATgui.GeneralTextArea2.append("\n" + "---> Number of subjects:\t " + Number_of_subject+"\n") ; 

     
     OPATgui.GeneralTextArea2.append("\n" + "---> Number of objects:\t " + Number_of_object+"\n") ; 
     
     OPATgui.GeneralTextArea2.append("\n" + "---> Number of comments:\t " + Number_of_comments+"\n") ; 
     OPATgui.GeneralTextArea2.append("\n" + "---> Number of labels:\t " + Number_of_labels+"\n") ;
     
     
     OPATgui.GeneralTextArea2.append("\n"+"==============  Class axiom metrics ==========  \n" );
	
     
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of sub classes:\t " +  Number_of_subClasses+"\n") ; 
    
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of equivalent classes:\t " +  Number_of_EquiClasses+"\n") ; 
    
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of disjoint classes:\t " + Number_of_disClasses+"\n") ; 
    
    
    
	OPATgui.GeneralTextArea2.append("\n"+"==============  Object property axiom metrics ==========  \n" );
	
   
    
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of sub object properties:\t " + Number_of_subObjectPro+"\n") ; 
    
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of equivalent object properties:\t " + Number_of_EqObject_pro+"\n") ; 
    
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of inverse object properties:\t " + Number_of_inver_ob_pro+"\n") ; 
    
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of  object properties domain:\t " + Number_of_obj_pro_domain+"\n") ;
    
    OPATgui.GeneralTextArea2.append("\n" + "---> Number of object properties range:\t " + Number_of_obj_pro_range+"\n") ; 
	
	
	
	 OPATgui.GeneralTextArea2.append("\n"+"============== Functional property axiom metrics ==========  \n" );
	 
	
	    
	    OPATgui.GeneralTextArea2.append("\n" + "---> Number of object functional properties:\t " + Number_of_fun_ob_pro+"\n") ;  
	    
	    OPATgui.GeneralTextArea2.append("\n" + "---> Number of inverse object functional properties:\t " + Number_ofinv_ob_pro+"\n") ; 
	    
	    OPATgui.GeneralTextArea2.append("\n" + "---> Number of  object properties domain:\t " + Number_of_fun_ob_pro_domain+"\n") ;
	    
	    OPATgui.GeneralTextArea2.append("\n" + "---> Number of object properties range:\t " + Number_of_fun_ob_pro_range+"\n") ; 
		
	 
	 
	    OPATgui.GeneralTextArea2.append("\n"+"==============  Individual axiom metrics ==========  \n" );
	 
	 
	    OPATgui.GeneralTextArea2.append("\n"+"==============  Imported Ontologies ==========  \n" );
	    OntModel m =model;
	    OntModel mBase = ModelFactory.createOntologyModel( OntModelSpec.OWL_MEM, m.getBaseModel() );
	    for (Iterator i = mBase.listOntologies(); i.hasNext(); ) {
			     Ontology ont = (Ontology) i.next();
			     OPATgui.GeneralTextArea2.append("\n"+"======"+ ont.getLocalName() +"\n" );
			 }
	    if(mBase.listOntologies().toList().size()==0)
		   OPATgui.GeneralTextArea2.append("\n"+"The ontology contains no imported ontologies \n" ); 
 	}
 
 }


