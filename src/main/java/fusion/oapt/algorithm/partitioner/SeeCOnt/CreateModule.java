
package fusion.oapt.algorithm.partitioner.SeeCOnt;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import fusion.oapt.algorithm.partitioner.SeeCOnt.moduleExtractor.Extractor;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.model.ext.sentence.RDFSentence;
import fusion.oapt.model.ext.sentence.RDFSentenceGraph;
import fusion.oapt.model.ext.sentence.filter.OntologyHeaderFilter;
import fusion.oapt.model.ext.sentence.filter.PureSchemaFilter;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;

import org.apache.jena.ontology.ConversionException;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.riot.RiotException;
import org.apache.jena.shared.BadURIException;
import org.apache.jena.vocabulary.OWL;


import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.model.AddAxiom;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLInverseFunctionalObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLIrreflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLReflexiveObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLSymmetricObjectPropertyAxiom;
import org.semanticweb.owlapi.model.OWLTransitiveObjectPropertyAxiom;

import org.semanticweb.owlapi.search.EntitySearcher;



public class CreateModule {
	private  LinkedHashMap<Integer, Cluster> clusters; 
	private OntModel model; 
	private OWLOntology owl;
	private  String ontName = null;
	public static String tempDir = null;
	public static ArrayList<OntModel> models ;
	private LinkedHashMap<String, Integer> uriToClusterID = null;
	public static  int [][] NumLickConcept=null;
	private int numEntity;
	public static int [] numAloneElemnt;
	public static double[] numTree;
	ModelBuild MB;
	public  ArrayList<String> modelNames ;
	
	
	public  CreateModule (ModelBuild MBm)
	{
	 this.MB=MBm;	
	 this.model=MB.getModel();
	 this.owl=MB.getOWLModel();
     this.numEntity=MB.NumEntity;
     tempDir = MB.wd;
     ontName = MB.ontologyName;
     models=new ArrayList<OntModel>();
     clusters=new LinkedHashMap<Integer,Cluster>();
     modelNames=new ArrayList<String>();
	}
	
	public  CreateModule (ModelBuild MB, LinkedHashMap<Integer, Cluster> clusters)
	{
	 this.MB=MB;	
	 this.model=MB.getModel();
	 this.owl=MB.getOWLModel();
     this.numEntity=MB.NumEntity;
     tempDir = MB.wd;
     ontName = MB.ontologyName;
     models=new ArrayList<OntModel>();
     this.clusters=clusters;
     modelNames=new ArrayList<String>();
	}
	
	public ArrayList<OntModel> getOntModels()
	{
	    if(models==null)
	    	return createOWLFiles_Phase();
	    else
		return models;
	} 


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////CreateOutput_Phase ////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//another implementation for owl file for each module

   public ArrayList<OntModel> createOWLFiles_Phase() //throws ConversionException
   {
	 if(MB==null)
	 { System.out.println("there is no model to get modules");
	     return null;}
	 if(clusters==null)
	 {
		 System.out.println("the model is not partitioned yet");
	 }
	 if(owl==null)
		 return createOntModelFiles();
	OWLOntologyManager manager = owl.getOWLOntologyManager();
 	OWLOntologyManager managerN = null;
 	OWLOntology owlN = null;
 	OWLDataFactory factory = manager.getOWLDataFactory();   
 	OWLDataFactory factoryN = null;
 	OWLClass thing = factory.getOWLThing();
	NodeList nList = MB.rbgmModel.getNamedClassNodes();  
	int i=0, count=0;
	for (Iterator<Cluster> j = clusters.values().iterator(); j.hasNext();) 
	 {
		 Cluster cluster = j.next();
		 managerN = OWLManager.createOWLOntologyManager(); 
		 try {
			owlN = managerN.createOntology();
			factoryN = managerN.getOWLDataFactory();
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
		 OWLDeclarationAxiom axiomT = factoryN.getOWLDeclarationAxiom(thing);
		 AddAxiom adx = new AddAxiom(owlN, axiomT);
		 managerN.applyChange(adx);
		 Set<OWLClass> redClass = new HashSet<OWLClass>(); 
		 for (Iterator<Node> iter = cluster.listElements(); iter.hasNext();) {
	         Node inode = iter.next();
	         OntClass cls = model.getOntClass(inode.toString());  
	         if(nList.contains(inode) && cls!=null)
	         {
	             IRI iri = IRI.create(cls.getURI());
	        	 OWLClass ocls = factory.getOWLClass(iri);   
	        	 OWLDeclarationAxiom axiom = factory.getOWLDeclarationAxiom(ocls);  
	        	 AddAxiom addAxiom = new AddAxiom(owlN, axiom); 
	        	 managerN.applyChange(addAxiom);
	        	 Set<OWLClassAxiom> axioms = new HashSet<OWLClassAxiom>();  
	        	 try{
	        	 axioms = owl.getAxioms(ocls); 
	        	 }
	        	 catch(ConversionException| IllegalStateException e){}
	        	 Iterator<OWLClassAxiom> it = axioms.iterator();
	        	 while(it.hasNext())
	        	 {
	        		 OWLAxiom ax = it.next();
	        		 owlN.addAxiom(ax);
	        		 Iterator<OWLEntity> sg = ax.getSignature().iterator();   
	        		 while(sg.hasNext())
	        		 {
	        			 String name = sg.next().getIRI().getShortForm().toString();
	        			// if(cluster.getlistName().contains(name))
	        			//	axioms.remove(ax); //Samira:why remove?
	           		 }
	        		
	        	 }
	        	 owlN.addAxioms(axioms);
	        	 Iterator<OWLClassExpression> ssub=EntitySearcher.getSubClasses(ocls, owl).iterator();
	        	 try{
	        	 while(ssub.hasNext())
	        	 {
	        		OWLClassExpression osubs=ssub.next();  
	        		if(osubs.isOWLClass()){
	        		IRI iris=osubs.asOWLClass().getIRI();
	        		OWLClass osubc=factory.getOWLClass(iris);
	        		OWLDeclarationAxiom axiomSub = factory.getOWLDeclarationAxiom(osubc);  
		        	AddAxiom addAxiomSub = new AddAxiom(owlN, axiomSub);
		        	managerN.applyChange(addAxiomSub);
	        		addAxiom=new AddAxiom(owlN,factory.getOWLSubClassOfAxiom(osubs, ocls));
	        		managerN.applyChange(addAxiom);
	        		Iterator<OWLAnnotationAssertionAxiom> axiomAn= EntitySearcher.getAnnotationAssertionAxioms((OWLEntity) osubs, owl).iterator();
		          	 Set<OWLAnnotationAssertionAxiom> nAxio=new HashSet<OWLAnnotationAssertionAxiom>();
		          	while (axiomAn.hasNext()) {
		          	    nAxio.add(axiomAn.next());
		          	}
		          	 managerN.addAxioms(owlN, nAxio);
	        	 }}}
	        	 catch(IllegalStateException e){}
	        	 Iterator<OWLClassExpression> ssup=EntitySearcher.getSuperClasses(ocls, owl).iterator();   	
	          	 Iterator<OWLAnnotationAssertionAxiom> axiomAn= EntitySearcher.getAnnotationAssertionAxioms(ocls, owl).iterator();
	          	 Set<OWLAnnotationAssertionAxiom> nAxio=new HashSet<OWLAnnotationAssertionAxiom>();
	          	while (axiomAn.hasNext()) {
	          	    nAxio.add(axiomAn.next());
	          	}
	          	 managerN.addAxioms(owlN, nAxio); 
	          	 addObjectPropDomain(owl,factory,managerN,ocls,owlN);
	        	// addObjectPropRange(owl,managerN,ocls,owlN);
	        	 addDataPropDomain(owl,factory,managerN,ocls,owlN);
	        	 //addDataPropRange(owl,managerN,ocls,owlN); 
	        	 Iterator<OWLIndividual> cid=EntitySearcher.getIndividuals(ocls, owl).iterator();//ocls.getIndividuals(owl).iterator();
	        	 while(cid.hasNext())
	        	 {
	        		 OWLIndividual inv=cid.next();
	        		 addAxiom= new AddAxiom(owlN, factory.getOWLClassAssertionAxiom(ocls, inv));
	        		 managerN.applyChange(addAxiom);
	        	 }
	        	 if(!(ssup==null))
	        	 {
	        		redClass.add(ocls);
	           	 }
	        if(EntitySearcher.getSuperClasses(ocls, owlN)==null) System.out.println(ocls.getIRI().toString());
	         }
	                	    	 
	     }
		  /*OWLReasonerFactory reasonerFactory = new ElkReasonerFactory();
			OWLReasoner reasoner = reasonerFactory.createReasoner(owlN);
			reasoner.precomputeInferences(InferenceType.CLASS_HIERARCHY);*/
		 String outPath = tempDir + ontName + "_Module_" + i + ".owl";
		 saveOntology(owlN,outPath);//"D:/result/test/"+ontName+"_Module_"+i+".owl"); //(owlN,outPath);
		  i++;
	 }
	 count=i;
	 //used during modules quality evaluation
	 numTree=new double[models.size()];
		for( i=0;i<models.size();i++)
		 {
			  List Trlist=new ArrayList();
			  OntModel mod=models.get(i);
			  OntClass thng = mod.getOntClass( OWL.Thing.getURI() );
			  Trlist=thng.listSubClasses(true).toList();
			  if(count>0) numTree[i]=Trlist.size(); 
		 }
		
	System.out.println("Modularization is done!!");
	Coordinator.FinishPartitioning = true;
	return models;  
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static void addObjectPropDomain(OWLOntology ontology,OWLDataFactory factory, OWLOntologyManager manager,OWLClass ocls, OWLOntology ont)
{
	 
	   for (OWLObjectPropertyDomainAxiom op : ontology.getAxioms(AxiomType.OBJECT_PROPERTY_DOMAIN)) {                        
         if (op.getDomain().equals(ocls)) {   
             for(OWLObjectProperty oop : op.getObjectPropertiesInSignature())  
             {
            	 OWLAxiom axio=factory.getOWLObjectPropertyDomainAxiom(oop, ocls);
            	 AddAxiom ax=new AddAxiom(ont,axio);
            	 manager.applyChange(ax);
            	 Set<OWLObjectPropertyRangeAxiom> axiomO=ontology.getObjectPropertyRangeAxioms(oop);	
        		 manager.addAxioms(ont, axiomO);
            	 
            	 //Set<OWLObjectPropertyDomainAxiom> axiomO=ontology.getObjectPropertyDomainAxioms(oop);
            	 // manager.addAxioms(ont, axiomO);
            	  if(EntitySearcher.isFunctional(oop, ontology)){//oop.isFunctional(ontology)){
                 	 OWLFunctionalObjectPropertyAxiom axiom=factory.getOWLFunctionalObjectPropertyAxiom(oop);
                 	 AddAxiom addAxiom = new AddAxiom(ont, axiom);  manager.applyChange(addAxiom);
                  }
             	
                  if(EntitySearcher.isInverseFunctional(oop, ontology)){//oop.isInverseFunctional(ontology)){
                 	 OWLInverseFunctionalObjectPropertyAxiom axiom=factory.getOWLInverseFunctionalObjectPropertyAxiom(oop);
                 	AddAxiom addAxiom = new AddAxiom(ont, axiom);  manager.applyChange(addAxiom);
                        }
                  if(EntitySearcher.isReflexive(oop, ontology)){//oop.isReflexive(ontology)){
                 	 OWLReflexiveObjectPropertyAxiom axiom=factory.getOWLReflexiveObjectPropertyAxiom(oop);
                 	AddAxiom addAxiom = new AddAxiom(ont, axiom);  manager.applyChange(addAxiom);
                  }
                  if(EntitySearcher.isIrreflexive(oop, ontology)){//oop.isIrreflexive(ontology)){
                 	 OWLIrreflexiveObjectPropertyAxiom axiom=factory.getOWLIrreflexiveObjectPropertyAxiom(oop);
                 	AddAxiom addAxiom = new AddAxiom(ont, axiom);  manager.applyChange(addAxiom);
                  }
                  if(EntitySearcher.isSymmetric(oop, ontology)){//oop.isSymmetric(ontology)){
                 	 OWLSymmetricObjectPropertyAxiom axiom=factory.getOWLSymmetricObjectPropertyAxiom(oop);
                 	AddAxiom addAxiom = new AddAxiom(ont, axiom);  manager.applyChange(addAxiom);
                  }
                  if(EntitySearcher.isTransitive(oop, ontology)){//oop.isTransitive(ontology)){
                 	 OWLTransitiveObjectPropertyAxiom axiom=factory.getOWLTransitiveObjectPropertyAxiom(oop);
                 	AddAxiom addAxiom = new AddAxiom(ont, axiom);  manager.applyChange(addAxiom);
                  }
                  Iterator<OWLObjectProperty>  sop=EntitySearcher.getSubProperties(oop, ontology).iterator();//oop.getSubProperties(ontology).iterator(); 
                  Iterator<OWLObjectPropertyExpression> eop=EntitySearcher.getEquivalentProperties(oop, ontology).iterator();//oop.getEquivalentProperties(ontology);
                  Set<OWLObjectPropertyExpression> eopN=new HashSet<OWLObjectPropertyExpression>();
                  while (eop.hasNext()) {
                	    eopN.add(eop.next());
                	}
                  Iterator<OWLObjectPropertyExpression>  iop=EntitySearcher.getInverses(oop, ontology).iterator();//oop.getInverses(ontology).iterator();
                  while(sop.hasNext())
                  {
                	  OWLObjectPropertyExpression sopi=sop.next();
                	  AddAxiom addAxiom = new AddAxiom(ont, factory.getOWLSubObjectPropertyOfAxiom(sopi, oop));
                	  manager.applyChange(addAxiom);
                  }
                  try{
                  while(iop.hasNext())
                  {
                	  OWLObjectPropertyExpression iopi=iop.next();
                	  AddAxiom addAxiom = new AddAxiom(ont, factory.getOWLInverseObjectPropertiesAxiom(oop, iopi));
                	  manager.applyChange(addAxiom); 
                  }}
                  catch(IllegalStateException e){}
                  AddAxiom addAxiom = new AddAxiom(ont, factory.getOWLEquivalentObjectPropertiesAxiom(eopN));
                 manager.applyChange(addAxiom);
            	             	
             }
          }
     } 
}



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static void addDataPropDomain(OWLOntology ontology, OWLDataFactory factory, OWLOntologyManager manager,OWLClass ocls,OWLOntology ont)
{
	 for (OWLDataPropertyDomainAxiom dp : ontology.getAxioms(AxiomType.DATA_PROPERTY_DOMAIN))
       {
	          if (dp.getDomain().equals(ocls)) {   
	                for(OWLDataProperty odp : dp.getDataPropertiesInSignature())
	                {
	                	 OWLDataPropertyDomainAxiom axiomO=manager.getOWLDataFactory().getOWLDataPropertyDomainAxiom(odp, ocls);
	                	 AddAxiom ax=new AddAxiom(ont,axiomO);
	                	 manager.applyChange(ax);
	                	 //Set<OWLDataRange> sdr=odp.getRanges(ontology);
	                	 Set<OWLDataPropertyRangeAxiom> axiomDR=ontology.getDataPropertyRangeAxioms(odp);	
	            		 manager.addAxioms(ont, axiomDR); 
	            		 if(EntitySearcher.isFunctional(odp, ontology)){//odp.isFunctional(ontology)){
	                		 AddAxiom axio=new AddAxiom(ont,factory.getOWLFunctionalDataPropertyAxiom(odp));
	                		 manager.applyChange(axio);
	               	    }
	                	 Iterator<OWLDataPropertyExpression> edp=EntitySearcher.getEquivalentProperties(odp, ontology).iterator();//odp.getEquivalentProperties(ontology).iterator();
	                	 Iterator<OWLDataProperty> sdp=EntitySearcher.getSubProperties(odp, ontology).iterator();//odp.getSubProperties(ontology).iterator();
	                	 while(edp.hasNext())
	                	 {
	                		 OWLDataPropertyExpression idp=edp.next();   
	                		 AddAxiom axio=new AddAxiom(ont, factory.getOWLSubDataPropertyOfAxiom(idp, odp));
	                		 manager.applyChange(axio);
	                		 
	                	 }
	                	 
	                	 while(sdp.hasNext())
	                	 {
	                		 OWLDataPropertyExpression idp=sdp.next();
	                		 AddAxiom axio=new AddAxiom(ont, factory.getOWLSubDataPropertyOfAxiom(idp, odp));
	                		  manager.applyChange(axio);
	                	 }
	                }
	                
	            }
	       }
}

public void saveOntologyByName(String name , String loc)
{
	
     models.add(MB.getModel());
     numTree=new double[models.size()];
		for(int i=0;i<models.size();i++)
		 {
			  List Trlist=new ArrayList();
			  OntModel mod=models.get(i);
			  OntClass thng = mod.getOntClass( OWL.Thing.getURI() );
			  Trlist=thng.listSubClasses(true).toList();
			   numTree[i]=Trlist.size(); 
		 }
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		 OWLDocumentFormat format=new RDFXMLDocumentFormat();
		 File file=new File(loc);
		 try {
			manager.saveOntology(MB.getOWLModel(), format, IRI.create(file.toURI()));
		} catch (OWLOntologyStorageException e) {
			e.printStackTrace();
		}		
	System.out.println("Modularization is done!!");
	Coordinator.FinishPartitioning = true;
	
}

public void OntologySave(OWLOntology owlN, String loc)
{
	 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	 OWLDocumentFormat format=new RDFXMLDocumentFormat();
	 File file=new File(loc);
	 try {
		manager.saveOntology(owlN, format, IRI.create(file.toURI()));
	} catch (OWLOntologyStorageException e) {
		e.printStackTrace();
	}
	 OntDocumentManager mgr = new OntDocumentManager();
     mgr.setProcessImports(true);
     OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
     Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
     spec.setDocumentManager(mgr);
     OntModel model = ModelFactory.createOntologyModel(spec, null);    
     model.read("file:"+loc);  
     model.setStrictMode(false);
     models.add(model);
}

 private static void saveOntology(OWLOntology owlN, String loc)
 {
	 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	 OWLDocumentFormat format=new RDFXMLDocumentFormat();
	 File file=new File(loc);
	 try {
		manager.saveOntology(owlN, format, IRI.create(file.toURI()));
	} catch (OWLOntologyStorageException e) {
		e.printStackTrace();
	}
	 OntDocumentManager mgr = new OntDocumentManager();
     mgr.setProcessImports(true);
     OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
     Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
     spec.setDocumentManager(mgr);
     OntModel model = ModelFactory.createOntologyModel(spec, null); 
     try{
     model.read("file:"+loc);}
     catch(RiotException e){}
      model.setStrictMode(false);
     models.add(model);
 }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private  void createLink_Phase()
{
	clusters = Coordinator.clusters;
	for (Iterator<Cluster> i = clusters.values().iterator(); i.hasNext();) {
		Cluster icluster = i.next();  
		//System.out.println("Cluster:\t"+i);icluster.printCluster();
		addProperties(icluster); 
		//System.out.println(icluster.getClusterID()+"\t"+icluster.getElements().size()); 
		//icluster.printCluster();
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private void addProperties(Cluster icluster)
{
	Iterator<Node> stm= MB.rbgmModel.listStmtNodes();
	boolean isDatatype = false;
	Cluster tempCluster = new Cluster(0);
	while (stm.hasNext()){
		Node istm= stm.next();
		Node iobject = istm.getObject();
		Node isubject = istm.getSubject();
		Node ipredicate = istm.getPredicate();
		if ((ipredicate.getLocalName() != null) && (ipredicate.getLocalName().toLowerCase().toString().equals("domain") || ipredicate.getLocalName().toLowerCase().toString().equals("range")))
		{
			/*	isDatatype = false;
			ExtendedIterator datalist= BuildModel.OntModel.listDatatypeProperties(); 
			while (datalist.hasNext()){
			Object id= datalist.next();
			if (id.toString() == ipredicate.toString() || id.toString() ==isubject.toString() ){
			isDatatype = true;
				}
			}
			if (isDatatype == false){*/
		int u= clusterExistence(iobject, isubject,  icluster);
		if (u == 1){
			//icluster.putElement(isubject.toString(), isubject);
			tempCluster.putElement(isubject.toString(), isubject);
		}else if (u == 2){
			//icluster.putElement(iobject.toString(), iobject);
			tempCluster.putElement(iobject.toString(), iobject);
		}
		//}
		}
	}
	Iterator<Node> ilist =tempCluster.listElements();
	while (ilist.hasNext()){
		Node ind = ilist.next();
		icluster.putElement(ind.toString(), ind);
	}
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static int clusterExistence (Node inode, Node jnode , Cluster icluster)
{
	int u=0;

	Iterator<Node> ls= icluster.listElements();
	while (ls.hasNext()){
		Node nd = ls.next();
		if (nd.equals(inode) ){
			u=1;
		}else if (nd.equals(jnode) ){
			u=2;
		}
	}
	return u;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 //another implmentation of create output
  public   ArrayList<OntModel> createOntModelFiles()
   {
	 //clusters= Coordinator.clusters;
	 int KNumCH=clusters.size();
	 NumLickConcept = new int [KNumCH][numEntity+1]; // This array store the number of link for each element in each cluster
     ArrayList<Cluster> list = new ArrayList<Cluster>();
     for (Iterator<Cluster> i = clusters.values().iterator(); i.hasNext();) 
     {
      list.add(i.next());
     }
    LinkedHashMap<String, Integer> uriToClusterID = new LinkedHashMap<String, Integer>();
    for (int i = 0, n = list.size(); i < n; i++) {
     Cluster cluster = list.get(i);
     int clusterID = cluster.getClusterID();
     for (Iterator<Node> iter = cluster.listElements(); iter.hasNext();) {
         Node inode= iter.next();
    	 String uri = inode.toString(); 
         uriToClusterID.put(uri, clusterID);
     }
    }
 
    RDFSentenceGraph sg = new RDFSentenceGraph(MB.rbgmModel.getOntModel());
    sg.build(); //build with list statement of original ontology model
    ArrayList <String> iii= sg.getOntologyURIs();
    sg.filter(new OntologyHeaderFilter(sg.getOntologyURIs()));
    sg.filter(new PureSchemaFilter());
   
    //Creating one model for each partition to store them as separated files
    models = new ArrayList<OntModel>(list.size()); // create models (Array list with OntModel type with number of partition (NumCH))
    LinkedHashMap<Integer, Integer> clusterIDToOntModelID = new LinkedHashMap<Integer, Integer>();
    for (int i = 0, n = list.size(); i < n; i++) 
    {
     models.add(ModelFactory.createOntologyModel()); //Create as RDF format
     int cid = list.get(i).getClusterID();
     clusterIDToOntModelID.put(cid, i);
    }
    for (int i = 0, n = sg.getRDFSentences().size(); i < n; i++)
    {
     RDFSentence sentence = sg.getRDFSentence(i);     
     ArrayList<String> uris = sentence.getSubjectDomainVocabularyURIs(); 
     LinkedHashMap<Integer, Object> uniqueURIs = new LinkedHashMap<Integer, Object>();
     for (int j = 0, m = uris.size(); j < m; j++) {
         Integer clusterID = uriToClusterID.get(uris.get(j));
         if (clusterID != null) {
             uniqueURIs.put(clusterID, null);
         }
        }
      if (uniqueURIs.size() == 1) {
         Integer cid = uniqueURIs.keySet().iterator().next();
         Integer mid = clusterIDToOntModelID.get(cid);
         OntModel block = models.get(mid); //mid is the cluster index
         ArrayList<Statement> statements = sentence.getStatements();
         
         for (int j = 0, m = statements.size(); j < m; j++) {
        	 block.add(statements.get(j));  
        	 // if one statement add to the file, we should for its subject-object save the number of  link
        	 RDFNode ObjectURI = statements.get(j).getObject();
             RDFNode SubjectURI = statements.get(j).getSubject();
             RDFNode PropertyURI = statements.get(j).getPredicate();  
             if (ObjectURI.isURIResource() && SubjectURI.isURIResource() ){
	             String[] iProperty = PropertyURI.toString().split("\\#");
	       		 String[] iSubject = SubjectURI.toString().split("\\#");
	       		 String[] iObject = ObjectURI.toString().split("\\#");
	       		 if(iProperty!=null && iSubject!=null && iObject!=null)
	       		 {
	       		 if (iProperty.length>1){
		       		 if (iProperty[1].toLowerCase().equals("subclassof") || iProperty[1].toLowerCase().equals("haspropoerty")) { // TO DO: we should those acceptable property in this line such as SubclassOf 
			         	 int indexSubjectName =0;
			         	 if(iSubject.length >1) indexSubjectName=MB.findIndex(iSubject[1]); 
				         if (indexSubjectName >0 )
				             {NumLickConcept[mid][indexSubjectName] = NumLickConcept[mid][indexSubjectName] +1; }  // mid is the index of CH
				      	 int indexObjectName =0;
				      	 if(iObject.length>1) MB.findIndex(iObject[1]); 
				      	 if (indexObjectName >0 )
				         	 {NumLickConcept[mid][indexObjectName] = NumLickConcept[mid][indexObjectName] +1; }
			          }
	       		 	}
	       		 }
	       	}
         }
     }
 }
 


 
 // adding root 1- for Root concept (RootTag=false) ,  2- for those element with numLink=1
 	//First phase (1-for Root concept (RootTag=false))
	//if the node does not have superNode, we suppose it is Root and it is alone, so we call addRoot() function for it
 	numAloneElemnt = new int [KNumCH];
 	for (int ia=0; ia<MB.NumEntity; ia++){
		 if (MB.entities.get(ia).getNamedSupers() == null){
			 Node alone_element= MB.entities.get(ia); 
			 int indexCH_aloneElement = uriToClusterID.get(MB.entities.get(ia).toString());
			 addRoot(alone_element.toString(),indexCH_aloneElement ); //add this element in the ch block
			 numAloneElemnt [indexCH_aloneElement] = numAloneElemnt [indexCH_aloneElement] +1; 
			 //since we add one link in the file (alone_elemenet, subClassOf, "Thing"), so, we should count one link for alone_element in the NumLickConcept array 
			 NumLickConcept[indexCH_aloneElement][ia] = NumLickConcept[indexCH_aloneElement][ia] +2; //in the next lines, if this array home==1, then it thinks it is alone and does not link to Thing class, so, we add (+2) till it does not be equal 1
			 // we count those classes that are connected to alone_element
			 Iterator<Node> listStm = MB.rbgmModel.listStmtNodes();
			 while (listStm.hasNext()){
				 Node st= listStm.next();
				if (st.getPredicate().getLocalName().toLowerCase().equals("subclassof") ){
				 if (st.getSubject().getLocalName() != null && st.getObject().getLocalName() != null){
					 if (st.getSubject() == alone_element){
						 int isx= MB.findIndex(st.getObject().getLocalName());
						 if ((isx>0) && (uriToClusterID.get(st.getObject().toString()) != null) )  NumLickConcept[indexCH_aloneElement][isx] = NumLickConcept[indexCH_aloneElement][isx] +2;
					 }
					 if (st.getObject() == alone_element){
						 int isx= MB.findIndex(st.getSubject().getLocalName());
						 if ((isx>0) && (uriToClusterID.get(st.getSubject().toString()) != null) )  NumLickConcept[indexCH_aloneElement][isx] = NumLickConcept[indexCH_aloneElement][isx] +2;
					 }
				 }
				}
			 }
		}
	}
 
	//Second phase (2- for those element with numLink=1)		
	for (int i=0; i<KNumCH; i++){
		for (int j=0; j<MB.NumEntity; j++){
			if (NumLickConcept[i][j] == 1) {
				//addRoot(BuildModel.entities.get(j).toString(), i);
				numAloneElemnt [i] = numAloneElemnt [i] +1;
				// add it in NumLinkConcept till do not add twice one statements to Thing
				 // we count those classes that are connected to alone_element
				 Iterator<Node> listStm = MB.rbgmModel.listStmtNodes();
				 while (listStm.hasNext()){
					 Node st= listStm.next();
					if (st.getPredicate().getLocalName().toLowerCase().equals("subclassof") ){
					 if (st.getSubject().getLocalName() != null && st.getObject().getLocalName() != null){
						 if (st.getSubject() == MB.entities.get(j)){
							 int isx= MB.findIndex(st.getObject().getLocalName());
							 if ((isx>0) && (uriToClusterID.get(st.getObject().toString()) != null))  NumLickConcept[i][isx] = NumLickConcept[i][isx] +2;
						 }
						 if (st.getObject() == MB.entities.get(j)){
							 int isx= MB.findIndex(st.getSubject().getLocalName());
							 if ((isx>0) && (uriToClusterID.get(st.getSubject().toString()) != null) )  NumLickConcept[i][isx] = NumLickConcept[i][isx] +2;
						 }
					 }
					}
				 }
			}
		}
	}
 
	numTree=new double[models.size()];
	for(int i=0;i<models.size();i++)
	 {
		  List Trlist=new ArrayList();
		  OntModel model=models.get(i);
		  OntClass thing = model.getOntClass( OWL.Thing.getURI() );
		  Trlist=thing.listSubClasses(true).toList();
		  if(list!=null) numTree[i]=Trlist.size(); 
	 }
	
 
 //Creating Files in Temp folder
 for (int i = 0, n = models.size(); i < n; i++) {
     int cid = list.get(i).getClusterID();
     String filepath = tempDir + ontName + "_module_" + cid + ".owl";
     File file = new File(filepath);
     if (file.exists()) {
         file.delete();
     }
     Cluster c=list.get(cid);
     
     OntModel block = models.get(i); // Write one block as one model in owl file
      try {
         FileOutputStream fos = new FileOutputStream(filepath);
         BufferedOutputStream bos = new BufferedOutputStream(fos);
         block.write(bos, "RDF/XML"); //XML format
         //block.write(bos, "Turtle"); //compact and more readable
         bos.close();
         fos.close();
     } catch (IOException e) {
         e.printStackTrace();
     }
     block.close();
   }
  return models;
  }


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//another implementation for creating output using OWL API
  public   ArrayList<OntModel> createOutput_Phase3()
  {
	clusters = Coordinator.clusters;
	NumLickConcept = new int [Coordinator.KNumCH][numEntity+1]; // This array store the number of link for each element in each cluster
	ArrayList<Cluster> list = new ArrayList<Cluster>();
	 for (Iterator<Cluster> i = clusters.values().iterator(); i.hasNext();) {
	     list.add(i.next());
	 }
	//>>>start to creating a list of OWLmodels
	 models = new ArrayList<OntModel>(list.size()); // create one array of OWLmodels 
	 for (int i = 0, n = list.size(); i < n; i++) { 
		models.add(ModelFactory.createOntologyModel());
	 } 
	 //>>>> finish creating a list of OWLmodels
	 
	 
	 //>> Saving which nodes exist in which cluster(module), e.g "Paper" node exist in cluster 1 since its clusterID is 1
	 for (int i = 0, n = list.size(); i < n; i++) {
	     Cluster cluster = list.get(i);
	     int clusterID = cluster.getClusterID();
	     for (Iterator<Node> iter = cluster.listElements(); iter.hasNext();) {
	         Node inode= iter.next();
	    	 String uri = inode.toString(); 
	         uriToClusterID.put(uri, clusterID);
	     }
	 }
	 //>> Finishing the saving index of cluster's node
	 
	 
	 //>>>> Start to process all statements of original model, then put each statements in proper modules
	// for (int i = 0, n = Controller.KNumCH; i < n; i++) 
	 {
		StmtIterator stm= MB.getModel().listStatements(); 	
		while (stm.hasNext()){
			Statement istm= stm.nextStatement();
			Resource isubject = istm.getSubject();
			RDFNode iobject = istm.getObject();
			Property ipredicate = istm.getPredicate();
			if (isubject.getLocalName() != null) // we do not add those statements that contain a "Blank nodes"
			{
				Integer clusterID = uriToClusterID.get(isubject.toString());
				if (clusterID != null){
					models.get(clusterID).add(istm);
					countLink(istm, clusterID);
				}
			}else if (iobject != null){
				Integer clusterID = uriToClusterID.get(iobject.toString());
				if (clusterID != null){
					models.get(clusterID).add(istm);
					countLink(istm, clusterID);
				}
			} else if (ipredicate.getLocalName() != null){  // we do not add those statements that contain a "Blank nodes"
				Integer clusterID = uriToClusterID.get(ipredicate.toString());
				if (clusterID != null){
					models.get(clusterID).add(istm);
					countLink(istm, clusterID);
				}
			}
	}
	 }
	//>>>> Finish processing statements
	  processAloneElments();
	
	//>>save our generated modules as files in temp folder
	 for (int i = 0, n = models.size(); i < n; i++) {
	     int cid = list.get(i).getClusterID();
	     String filepath = tempDir + ontName + "_Module_" + cid + ".owl";
	     File file = new File(filepath);
	     if (file.exists()) {
	         file.delete();
	     }	     
	     OntModel block = models.get(i); // Write one block as one model in owl file
	     OWLOntology owll=((OWLOntology) block);
	     saveOntology(owll,filepath);
	      try {
	         FileOutputStream fos = new FileOutputStream(filepath);//create on the local address
	         BufferedOutputStream bos = new BufferedOutputStream(fos);
	         try{
	        // Model m=block.write(bos, "RDF/XML"); //XML format  
	         }
	         catch(BadURIException e){
	        	 block.write(bos,"TURTLE");   
	         }
	         bos.close();
	         fos.close();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	     //block.close(); //TO DO: it should not comment (but if it works, we reach to error in AddRoot function (ClosedException: already closed))
	 }
	//>>finishing saving
	
		
	//Save values for Evaluating panel
	numTree=new double[models.size()];
	for(int i=0;i<models.size();i++)
	 {
		  List Trlist=new ArrayList();
		  OntModel model=models.get(i);
		  OntClass thing = model.getOntClass( OWL.Thing.getURI() );
		  Trlist=thing.listSubClasses(true).toList();
		  if(list!=null) numTree[i]=Trlist.size(); 
	 }
 
 
	 
	return models; 

}
/////////////////////////////////////////////////////////////////////////////////////////////////
private void countLink(Statement statements, Integer clusterID){
	 // if one statement add to the file, we should for its subject-object save the number of  link
	 RDFNode ObjectURI = statements.getObject();
    RDFNode SubjectURI = statements.getSubject();
    RDFNode PropertyURI = statements.getPredicate();  
    if (ObjectURI.isURIResource() && SubjectURI.isURIResource() ){
        String[] iProperty = PropertyURI.toString().split("\\#");
  		 String[] iSubject = SubjectURI.toString().split("\\#");
  		 String[] iObject = ObjectURI.toString().split("\\#");
  		 if(iProperty!=null && iSubject!=null && iObject!=null)
  		 {
  		 if (iProperty.length>1){
      		 if (iProperty[1].toLowerCase().equals("subclassof") || iProperty[1].toLowerCase().equals("haspropoerty")) { // TO DO: we should those acceptable property in this line such as SubclassOf 
	         	 int indexSubjectName =0;
	         	 if(iSubject.length >1) indexSubjectName=MB.findIndex(iSubject[1]); 
		         if (indexSubjectName >0 )
		             {NumLickConcept[clusterID][indexSubjectName] = NumLickConcept[clusterID][indexSubjectName] +1; }  // mid is the index of CH
		      	 int indexObjectName =0;
		      	 if(iObject.length>1) MB.findIndex(iObject[1]); 
		      	 if (indexObjectName >0 )
		         	 {NumLickConcept[clusterID][indexObjectName] = NumLickConcept[clusterID][indexObjectName] +1; }
	          }
  		 	}
  		 }
  	}
    
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private void  processAloneElments(){
	 // adding root 1- for Root concept (RootTag=false) ,  2- for those element with numLink=1
	//First phase (1-for Root concept (RootTag=false))
	//if the node does not have superNode, we suppose it is Root and it is alone, so we call addRoot() function for it
	numAloneElemnt = new int [Coordinator.KNumCH]; //delete
	for (int ia=0; ia<MB.NumEntity; ia++){ 
		Node alone_element= MB.entities.get(ia);
		if(alone_element!=null)
		{
		//System.out.println(uriToClusterID.size());
		Integer indexCH_aloneElement = uriToClusterID.get(alone_element.toString());
		if(indexCH_aloneElement!=null){
		NodeList supAloneElem= MB.entities.get(ia).getNamedSupers();
		if (supAloneElem == null){
			ConnectAloneElement (alone_element,indexCH_aloneElement, ia);
		}
		else{ //Maybe one concept has superNode but its superNode exist in another cluster, in this case, this concept is alone in VS
			boolean test = false;
			for (int s=0; s<supAloneElem.size();s++){
				test =ExistinClusterTest(supAloneElem.get(s),indexCH_aloneElement);
			}
			if (test == false){
				ConnectAloneElement (alone_element,indexCH_aloneElement, ia);
			}
		}}}
	}
		 
	//Second phase (2- for those element with numLink=1)		
	for (int i=0; i<Coordinator.KNumCH; i++)
	{
		for (int j=0; j<MB.NumEntity; j++){
			if (MB.entities.get(j).getLocalName().toString().toLowerCase().equals("laminar")){
				int wait2=0;
			}
			if (NumLickConcept[i][j] == 1) {
				addRoot(MB.entities.get(j).toString(), i);
				numAloneElemnt [i] = numAloneElemnt [i] +1;
				// add it in NumLinkConcept till do not add twice one statements to Thing
				 // we count those classes that are connected to alone_element
				 Iterator<Node> listStm = MB.rbgmModel.listStmtNodes();
				 while (listStm.hasNext()){
					 Node st= listStm.next();
					if (st.getPredicate().getLocalName().toLowerCase().equals("subclassof") ){
					 if (st.getSubject().getLocalName() != null && st.getObject().getLocalName() != null){
						 if (st.getSubject() == MB.entities.get(j)){
							 int isx= MB.findIndex(st.getObject().getLocalName());
							 if ((isx>0) && (uriToClusterID.get(st.getObject().toString()) != null))  NumLickConcept[i][isx] = NumLickConcept[i][isx] +2;
						 }
						 if (st.getObject() == MB.entities.get(j)){
							 int isx= MB.findIndex(st.getSubject().getLocalName());
							 if ((isx>0) && (uriToClusterID.get(st.getSubject().toString()) != null) )  NumLickConcept[i][isx] = NumLickConcept[i][isx] +2;
						 }
					 }
					}
				 }
			}
		}
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private boolean ExistinClusterTest (Node inode, int NumCluster ){
	boolean test = false;
	List ilist=new ArrayList(); 
	ilist =  models.get(NumCluster).listClasses().toList();
	for (int i=0; i< ilist.size(); i++){
		if (ilist.get(i).toString().equals(inode.toString())){
			test = true; 
			return test;
		}
	}
	//System.out.println(test);
	return test;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private void ConnectAloneElement(Node alone_element,int indexCH_aloneElement, int ia){
	addRoot(alone_element.toString(),indexCH_aloneElement ); //add this element in the ch block
	 numAloneElemnt [indexCH_aloneElement] = numAloneElemnt [indexCH_aloneElement] +1; 
	 //since we add one link in the file (alone_elemenet, subClassOf, "Thing"), so, we should count one link for alone_element in the NumLickConcept array 
	 NumLickConcept[indexCH_aloneElement][ia] = NumLickConcept[indexCH_aloneElement][ia] +2; //in the next lines, if this array home==1, then it thinks it is alone and does not link to Thing class, so, we add (+2) till it does not be equal 1
	 // we count those classes that are connected to alone_element
	 Iterator<Node> listStm = MB.rbgmModel.listStmtNodes();
	 while (listStm.hasNext()){
		 Node st= listStm.next();
		if (st.getPredicate().getLocalName().toLowerCase().equals("subclassof") ){
		 if (st.getSubject().getLocalName() != null && st.getObject().getLocalName() != null){
			 if (st.getSubject() == alone_element){
				 int isx= MB.findIndex(st.getObject().getLocalName());
				 if ((isx>0 && isx<=numEntity) && (uriToClusterID.get(st.getObject().toString()) != null) )  NumLickConcept[indexCH_aloneElement][isx] = NumLickConcept[indexCH_aloneElement][isx] +2;
			 }
			 if (st.getObject() == alone_element){
				 int isx= MB.findIndex(st.getSubject().getLocalName());
				 if ((isx>0 && isx<=numEntity) && (uriToClusterID.get(st.getSubject().toString()) != null) )  NumLickConcept[indexCH_aloneElement][isx] = NumLickConcept[indexCH_aloneElement][isx] +2;
			 }
		 }
		}
	 }
}





///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////AddingRoot_Phase //////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void addRoot(String concept, int NumCluster){
	
	OntModel block = models.get(NumCluster);
	Resource subjectNew =  ResourceFactory.createResource(concept); 
	Property predicateNew  = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");
	RDFNode objectNew = ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Thing");
	Statement statementNew = block.createStatement(subjectNew,  predicateNew, objectNew);
	block.add(statementNew);
}


/// based on the local modularity 
   public ArrayList<OntModel> createModules()
   {
	
	 ArrayList<Cluster> list = new ArrayList<Cluster>();
	 String moduletype[]= new String[]{"UM","LM","LUM","DCM","DRM"}; 
	 String onName=MB.getOntoName();// .nameOnt;
     Set<String> signatureNames = new HashSet<String>();
     ModulesGenerator MC; 
     OWLOntology owlN=null;
	 for (Iterator<Cluster> i = clusters.values().iterator(); i.hasNext();) 
	 {
	     list.add(i.next());  
	 }
	 
	 for(int i=0;i<list.size();i++)
	 {
		 Cluster cluster = list.get(i);
		 signatureNames.addAll(cluster.getURI());
		 String outPath = tempDir + ontName + "_Module_" + i + ".owl";
		 MC=new   ModulesGenerator(onName, signatureNames, moduletype[2], outPath);
		 owlN=MC.getModule();
		 signatureNames = new HashSet<String>();
		 saveOntology(owlN,outPath);
		 modelNames.add(outPath);
	 }	 
	 
	 //used during modules quality evaluation
	 numTree=new double[models.size()];
		for(int i=0;i<models.size();i++)
		 {
			  List Trlist=new ArrayList();
			  OntModel mod=models.get(i);
			  OntClass thng = mod.getOntClass( OWL.Thing.getURI() );
			  Trlist=thng.listSubClasses(true).toList();
			  if(list!=null) numTree[i]=Trlist.size(); 
		 }
	System.out.println("Modularization is done!!");
	return models; 
}



   /// based on the local modularity 
   public ArrayList<OntModel> createModules_E()
   {
	
	 ArrayList<Cluster> list = new ArrayList<Cluster>();
	 int moduletype[]= new int[]{0,1,2}; 
	 ArrayList<String> signatureNames = new ArrayList<String>();
     Extractor MC=new Extractor(); 
	 String onName=MB.getOntoName();// .nameOnt;
     OWLOntology owlN=null;
     int k=0;
	 for (Iterator<Cluster> i = clusters.values().iterator(); i.hasNext();) 
	 {
	     Cluster cluster = i.next();
		 signatureNames.addAll(cluster.getURI());
		 String outPath = tempDir + ontName + "_Module_" + k + ".owl"; k++;
		 owlN=MC.run(onName, signatureNames, moduletype[0]);
	   	 signatureNames = new ArrayList<String>();
		 saveOntology(owlN,outPath);
		 modelNames.add(outPath);
	 }	 
	 //used during modules quality evaluation
	 numTree=new double[models.size()];
		for(int i=0;i<models.size();i++)
		 {
			  List Trlist=new ArrayList();
			  OntModel mod=models.get(i);
			  OntClass thng = mod.getOntClass( OWL.Thing.getURI() );
			  Trlist=thng.listSubClasses(true).toList();
			  if(list!=null) numTree[i]=Trlist.size(); 
		 }
	System.out.println("Modularization is done!!");
	return models; 
}

}
