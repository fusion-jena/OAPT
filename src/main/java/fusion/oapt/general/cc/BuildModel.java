
package fusion.oapt.general.cc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.vocabulary.RDFS;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.io.GZipStreamDocumentTarget;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
//import org.semanticweb.owlapi.model.OntologyConfigurator;
import org.semanticweb.owlapi.model.UnloadableImportException;







import ru.avicomp.ontapi.OntManagers;
import ru.avicomp.ontapi.OntologyModel;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;
import fusion.oapt.model.RBGModel;
import fusion.oapt.model.RBGModelFactory;

public class BuildModel 
{
	public static RBGModel rbgmModel ;
	public static String fn1;
	public static String wd;
	public static int [] ConnexionArray = null;
	public static float [] MinAngleRule = null;
	public static OntModel OntModel = null;
	public static NodeList entities = null;
	private static LinkedHashMap <String, Integer> indexNodeClassArray = null;
	private static LinkedHashMap <String, Integer> indexNodeClassArray2 = null;
	static int NumCH= 1;
    public static  String [] SortedNameOnt=null;
    //public static boolean FinishPartitioning=false;
    public static boolean analysisTest = false;
    public static  int colorIndex;
    public static int NumEntity;
    public static String ontologyName = null;
    public static String nameOnt=null;
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public static void BuildModelOnt(String OntName)  
    {   	
    	Coordinator.FinishPartitioning = false;
    	nameOnt=OntName;  	
    	/*try {
			try {
				checkFormat(nameOnt);
			} catch (OWLOntologyStorageException e) {
					e.printStackTrace();
			}
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	 OntDocumentManager mgr = new OntDocumentManager();
 	 	 mgr.setProcessImports(true);
 	     OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
 	     //Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
 	     spec.setDocumentManager(mgr);
 	     //spec.setReasoner(reasoner);
 	     OntModel = ModelFactory.createOntologyModel(spec, null); 
 	     String filepath1 = nameOnt;
     	 filepath1="file:"+filepath1;
    	 if(nameOnt.endsWith(".owl")||(nameOnt.endsWith(".rdf")))
    	 {
    		OntModel.read(filepath1,null);   
    		// reasoner.bindSchema(OntModel); 
    	     OntModel.setStrictMode(false);
    	     rbgmModel = RBGModelFactory.createModel("PBM_MODEL");   
             rbgmModel.setOntModel(OntModel);       
    	 }
		       
    	else if(nameOnt.endsWith(".gz"))
     	{
     		InputStream fileStream=null;
    		try {
    			fileStream = new FileInputStream(new File(nameOnt));
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
       	    InputStream gzips=null;
    		try {
    			gzips = new GZIPInputStream(fileStream);
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
     		 //filepath1=nameOnt.substring(0, nameOnt.lastIndexOf("."));
    		  // BuildModelOnt1(gzips,filepath1);
    		   OntModel.read(gzips, "");  
    	        OntModel.setStrictMode(false);//  
    	        rbgmModel = RBGModelFactory.createModel("PBM_MODEL");
    	         rbgmModel.setOntModel(OntModel);  		    
     	}
    	else
    	{
    		System.out.println("Sorry!! invalid ontology file format");
    	}
                
         wd = "."+File.separator+"temp"+File.separator;
         File file = new File(wd);
         if (file.exists() == false) {
                file.mkdir();
            }
         fn1 = new File(filepath1).getName(); 
         String []stfn = fn1.split("\\."); 
         ontologyName = stfn[0];

    	 entities = new NodeList();
    	 indexNodeClassArray = new LinkedHashMap<String, Integer>();
    	 indexNodeClassArray2 = new LinkedHashMap<String, Integer>();
    	 int x=0;
    	 for (Iterator<Node> i = rbgmModel.listNamedClassNodes(); i.hasNext();)  
    	 {
    		Node tt = i.next();
    		if (tt.getLocalName() != null){
	    		entities.add(tt);
	            indexNodeClassArray2.put(entities.get(x).getLocalName(), x);
	            indexNodeClassArray.put(entities.get(x).toString(), x);//new:samira
	            //buildIndex(tt,x);
	            //System.out.println(tt.getLocalName()+"\t the local name:\t"+entities.get(x).getLabel());
	            x++;
    		}
    	 }
    	 NumEntity = entities.size();
    	//Add properties
        
    	 for (Iterator<Node> i = rbgmModel.listPropertyNodes(); i.hasNext();) {
        	Node tt = i.next();
        	if (tt.getLocalName() != null){
	        	entities.add(tt);
	            indexNodeClassArray2.put(entities.get(x).getLocalName(), x);
	            indexNodeClassArray.put(entities.get(x).toString(), x);//new:samira
	            x++;
        	}
        }
        
        System.out.println(filepath1+"\t the number of concepts\t"+OntModel.listNamedClasses().toList().size()+"\t"+NumEntity+"\t"
        +entities.size()+"\t"+rbgmModel.getNodes().size()+"\t"+rbgmModel.getNamedClassNodes().size());
    }
    
       
    public static void checkFormat(String Ontname) throws IOException, OWLOntologyCreationException,  OWLOntologyStorageException
    {
    	InputStream in=null;
    	InputStream fileStream=null;
    	if(Ontname.endsWith(".rdf"))  return;
    	if(Ontname.endsWith(".owl"))
    	 		in = new FileInputStream(Ontname);
    	else if(Ontname.endsWith(".gz"))
    		{
    			fileStream = new FileInputStream(new File(Ontname));
    			in= new GZIPInputStream(fileStream);
    		}
    	
    	  // Getting manager:
        OWLOntologyManager manager = OntManagers.createONT();

        // ====================================
        // Interacting using OWL-API interface:
        // ====================================
        OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
        config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
        StreamDocumentSource documentSource = new StreamDocumentSource(in);
        // Related data-factory:
        OWLDataFactory factory = manager.getOWLDataFactory();
        // Creating an ontology:
        OWLOntology owl = manager.loadOntologyFromOntologyDocument(documentSource, config);// .createOntology(IRI.create("first"));
        // Adding sub-class-of axiom:
       // ontology.addAxiom(factory.getOWLSubClassOfAxiom(factory.getOWLClass("first-class"), factory.getOWLThing()));

        // =====================================
        // Interacting using jena-API interface:
        // =====================================
        Model model = ( (OntologyModel) owl).asGraphModel();  
        model.createResource("second-class").addProperty(RDFS.subClassOf, ResourceFactory.createResource("first-class"));

        // =================
        // Printing results:
        // =================
      //  System.out.println("Ontology as Turtle:");
       // ontology.saveOntology(System.out);
        //System.out.println("\nList of owl-axioms:");
        //owl.axioms().forEach(System.out::println);
        //System.out.println("\nAll jena-statements:");
       // model.listStatements().forEachRemaining(System.out::println);
   // 	OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
   //   config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
   //  StreamDocumentSource documentSource = new StreamDocumentSource(in);
       // OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    //	OWLOntology owl;
	//	owl = manager.loadOntologyFromOntologyDocument(documentSource, config); //.loadOntologyFromOntologyDocument(in);
		@SuppressWarnings("deprecation")
		OWLDocumentFormat format= manager.getOntologyFormat(owl);  
		 System.out.println("the ontology format\t"+format.toString());
		 if(format.toString().contains("RDF/XML"))
		 {
			 System.out.println(); return;
		 }
		 else if(format.toString().contains("OBOFormat"))
		 {
			 System.out.println("the OBO format\t"+format);
		 }
		 else
		 {
			
			//RDFXMLOntologyFormat rdfxmlFormat = new RDFXMLOntologyFormat();
			OWLDocumentFormat rdfxmlFormat = new RDFXMLDocumentFormat();
			if(Ontname.endsWith(".owl"))
			{
				 manager.setOntologyFormat(owl, rdfxmlFormat);
				 File temp=new File(Ontname.substring(0, Ontname.indexOf(".owl")));
				 
				 checkTemp1();
				 String wdn = "."+File.separator+"temp1"+File.separator;
		         File f= new File(wdn);
		         if (f.exists() == false) {
		                f.mkdir();
		            }
				 File file=File.createTempFile(Ontname.substring(0, Ontname.indexOf(".owl")), ".owl",f);//new File("."+File.separator+"temp1"+File.separator));
				 manager.saveOntology(owl, rdfxmlFormat, IRI.create(file));
				 File ff=new File(nameOnt);
				 String str=ff.getName().substring(0, ff.getName().length());
				 File newFile = new File(file.getParent(), str);//
				 Files.move(file.toPath(), newFile.toPath());
				 nameOnt=newFile.getAbsolutePath();
				 temp.deleteOnExit();			 
			}
			 if(Ontname.endsWith(".gz"))
			 {
				try {
					unzipFunction(Ontname);
				} catch (DataFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 manager.removeOntology(owl);
				 File temp=new File(Ontname.substring(0, Ontname.indexOf(".gz")));
				 FileInputStream fin= new FileInputStream (temp);
				 checkTemp1();
				 String wdn = "."+File.separator+"temp1"+File.separator;
		         File f= new File(wdn);
		         if (f.exists() == false) {
		                f.mkdir();
		            }
				 File file=File.createTempFile(Ontname.substring(0, Ontname.indexOf(".owl")), ".owl",f);//new File("."+File.separator+"temp1"+File.separator));
				 owl=manager.loadOntologyFromOntologyDocument(fin);
				 manager.saveOntology(owl, rdfxmlFormat, IRI.create(file));
				 File ff=new File(nameOnt);
				 String str=ff.getName().substring(0, ff.getName().indexOf(".gz"));
				 File newFile = new File(file.getParent(), str);//
				 Files.move(file.toPath(), newFile.toPath());
				 nameOnt=newFile.getAbsolutePath();
				 System.out.println("the file\t"+nameOnt);
				 temp.deleteOnExit();			 
			 }
			//JOptionPane.showMessageDialog(null, "Your selected ontology is not in RDF/XML format. \n Please select a compatible ontology and re-run project.", "Error", JOptionPane.INFORMATION_MESSAGE); 
			
			
			
		 }
		 manager.removeOntology(owl);
    }
    
    private static void checkTemp1()
    {
    	File folder=new File("."+File.separator+"temp1"+File.separator);
    	if(folder.isDirectory()){
    	for (File file: folder.listFiles()) {
    		if (!file.isDirectory()) file.delete();
        }}
    }
    
    private static String unzipFunction(String file)    	 
    	        throws IOException, DataFormatException {
    	    //Allocate resources.
    	    FileInputStream fis = new FileInputStream(file);
    	    FileOutputStream fos = new FileOutputStream(file.substring(0, file.indexOf(".gz")));
    	    GZIPInputStream gzis = new GZIPInputStream(fis);
    	    byte[] buffer = new byte[1024];
    	    int len = 0;
    	    
    	    //Extract compressed content.
    	    while ((len = gzis.read(buffer)) > 0) {
    	        fos.write(buffer, 0, len);
    	    }
    	    
    	    //Release resources.
    	    fos.close();
    	    fis.close();
    	    gzis.close();
    	    buffer = null;
    	 return fos.toString();
    	
    	    }

    
  ////////////////// Another Build model for other ontology formats
    public static void BuildModelOnt1(InputStream nameOnt, String filepath) 
    {
    	InputStream filepath1 = null;
    	filepath1=nameOnt;
    	   	     	  
    	OntDocumentManager mgr = new OntDocumentManager();
        mgr.setProcessImports(false);
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
       spec.setDocumentManager(mgr);
        OntModel = ModelFactory.createOntologyModel(spec, null);
        OntModel.read(filepath1, "");  
        OntModel.setStrictMode(false);//  
        rbgmModel = RBGModelFactory.createModel("PBM_MODEL");
        //rbgmModel = RBGModelFactory.createModel("GMO_MODEL");
        rbgmModel.setOntModel(OntModel);            

      	entities = new NodeList();
    	indexNodeClassArray = (LinkedHashMap<String, Integer>) new LinkedHashMap<String, Integer>();
    	indexNodeClassArray2 = (LinkedHashMap<String, Integer>) new LinkedHashMap<String, Integer>();
    	int x=0;
    	for (Iterator<Node> i = rbgmModel.listNamedClassNodes(); i.hasNext();) {
    		entities.add(i.next());
            indexNodeClassArray2.put(entities.get(x).getLocalName(), x);
            indexNodeClassArray.put(entities.get(x).toString(), x);//new:samira
            x++;
        }

    	
    	//Add properties
        for (Iterator<Node> i = rbgmModel.listPropertyNodes(); i.hasNext();) {
        	entities.add(i.next());
            indexNodeClassArray2.put(entities.get(x).getLocalName(), x);
            indexNodeClassArray.put(entities.get(x).toString(), x);//new:samira
            x++;
        }

    }
    
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
    public static int  findIndex(String nameX ){
    	
    	int indexed = -1;
    	if (nameX == "[STATEMENT]"){
    		indexed = -1;
    	}else{
    	
    	if (nameX != null){
    		if (nameX.equals("Thing")) {
    			indexed = 1;
    	} else{
    		if (indexNodeClassArray.get(nameX) == null ){
    			indexed = -1;
    		}else{
        		indexed = indexNodeClassArray.get(nameX);
    		}
    	}
    	}}
    	return indexed;
    }
    public static int  findIndexName(String nameX ){
    	
    	int indexed = -1;
    	if (nameX == "[STATEMENT]"){
    		indexed = -1;
    	}else{
    	
    	if (nameX != null){
    		if (nameX.equals("Thing")) {
    			indexed = 1;
    	} else{
    		if (indexNodeClassArray2.get(nameX) == null ){
    			indexed = -1;
    		}else{
        		indexed = indexNodeClassArray2.get(nameX);
    		}
    	}
    	}}
    	return indexed;
    }
 /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
 public static ArrayList<Node> Connexion(Node iclass)
 {
	 ArrayList<Node> ConnexionNode=new ArrayList<Node>();
	 
	 NodeList Subs= iclass.getNamedSubs();
	 if (Subs != null) {
		 for (int i=0; i< Subs.size(); i++){
			 ConnexionNode.add(Subs.get(i));
		 }
	 } 
	 NodeList Supers = iclass.getNamedSupers();
	 if (Supers != null ){
		 for (int i=0; i< Supers.size(); i++){
			 ConnexionNode.add(Supers.get(i));
		 }
	 } 
 			return ConnexionNode;
 	}
 
 public static void main(String args[])
 {
     
     double start=System.currentTimeMillis();
     String fp1 = "D:/test_modularization/test/test/FLOPO.owl";
     String fp2 = "D:/owl/rank/DRON.owl.gz";
     String fp3="D:/owl/owl/ego.owl.gz";
     BuildModel model=  new BuildModel();
     model.BuildModelOnt(fp3); 
     double end=System.currentTimeMillis();
     System.out.println("The  build model time---->"+(end-start)*.001+"\t sec \t"+model);
     System.out.println(model.rbgmModel.getNamedClassNodes().size()+"\t The optimal num. of partition time---->"+(end-start)*.001+"\t sec \t"+model.OntModel.size());

 } 

 }
    