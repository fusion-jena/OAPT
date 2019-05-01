package fusion.oapt.general.cc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.DataFormatException;
import java.util.zip.GZIPInputStream;

import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;


import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyAlreadyExistsException;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyID;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

import ru.avicomp.ontapi.OntApiException;
import ru.avicomp.ontapi.OntManagers;
import ru.avicomp.ontapi.OntologyModel;
import fr.inrialpes.exmo.ontowrap.OntowrapException;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;
import fusion.oapt.model.RBGModel;
import fusion.oapt.model.RBGModelFactory;
import fusion.oapt.model.modelImpl.RBGModelImpl;

public class ModelBuild {
	
	public  RBGModel rbgmModel;
	public static String fn1;
	public static String wd;
	public static int [] ConnexionArray = null;
	public static float [] MinAngleRule = null;
	private  OntModel OntModel = null;
	public static OntModel model=null; 
	private OWLOntology OwlModel=null;
	public static NodeList entities = null;
	private  LinkedHashMap <String, Integer> indexNodeClassArray ;
	private  LinkedHashMap <String, Integer> indexNodeClassArray2;
	int NumCH;
    public static  String [] SortedNameOnt;
    public static boolean analysisTest = false;
    public static  int colorIndex;
    public int NumEntity;
    public  static String ontologyName = null;
    private  String nameOnt=null;
    private Boolean check;
    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    public ModelBuild()
    {
    	rbgmModel=new RBGModelImpl();
    	indexNodeClassArray=new LinkedHashMap<String, Integer>();
    	indexNodeClassArray2=new LinkedHashMap<String, Integer>();
    	NumCH=1;
    	SortedNameOnt= null;
    	OntModel=null;
    	OwlModel=null;
    	NumEntity=0;
    	check=false;
    }
    
    public ModelBuild(String OntName)
    {
    	rbgmModel=new RBGModelImpl();
    	indexNodeClassArray=new LinkedHashMap<String, Integer>();
    	indexNodeClassArray2=new LinkedHashMap<String, Integer>();
    	NumCH=1;
    	SortedNameOnt= null;
       	Coordinator.FinishPartitioning = false;
       	OntModel=null;
       	OwlModel=null;
    	nameOnt=OntName;  
    	NumEntity=0;
    	check=false;
    }
    
    public String getOntoName()
    {
    	return nameOnt;
    }
    
   public OWLOntology getOWLModel()
   {
	   if(OwlModel==null)
		   build();
	   return OwlModel;
   }
   public RBGModel getRBGModel()
   {
	   if(rbgmModel==null)
		   build();
	   return rbgmModel;
   }
    public OntModel getModel()
    {
    	if(OntModel==null)
 		   build();
    	return OntModel;
    }
    
    public void build() 
    {
    	InputStream in=null;
    	InputStream fileStream=null;
    	if(nameOnt.endsWith(".owl") || nameOnt.endsWith(".rdf") || nameOnt.endsWith(".obo") || nameOnt.endsWith(".ttl"))
			try {
				in = new FileInputStream(nameOnt);
			} catch (FileNotFoundException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		else if(nameOnt.endsWith(".gz"))
    		{
    			try {
					fileStream = new FileInputStream(new File(nameOnt));
					in= new GZIPInputStream(fileStream);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	else
    	{
    		System.out.println("Sorry!! invalid ontology file format");
    		return ;
    	}
    	
    	// Getting manager:
        OWLOntologyManager manager = OntManagers.createONT();
        OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
        config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
        StreamDocumentSource documentSource = new StreamDocumentSource(in);
       
        // Creating an ontology:
        OWLOntology owl = null;
        try{
        	 System.out.println("\t loading....."+nameOnt);  
             owl = manager.loadOntologyFromOntologyDocument(documentSource, config);
             System.out.println(nameOnt+"\t is loaded as OWLOntology.....");
        }
        catch (OWLOntologyAlreadyExistsException e) {
    		// exception is thrown if there is an ontology with the same ID already in memory 
    		OWLOntologyID id = e.getOntologyID();
    		 owl = manager.getOntology( id );
    		    if ( owl == null )
					try {
						throw new OntowrapException("Already loaded [owl cache failure] " +  e );
					} catch (OntowrapException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
    		} catch ( OWLOntologyCreationException oocex ) {
    		    oocex.printStackTrace();
    		    try {
					throw new OntowrapException("Cannot load " +  oocex );
				} catch (OntowrapException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
        catch(OntApiException|StackOverflowError| OutOfMemoryError | IllegalArgumentException e)
        {
        	
        }  
        
        int size=0;
        OntDocumentManager mgr;
        OntModelSpec spec;
        OWLDocumentFormat format=null;
    	if(owl!=null)
    	{ 
          format= manager.getOntologyFormat(owl);  
		  System.out.println("the ontology format\t"+format.toString());
		  OwlModel=owl;
        // =====================================
        // Interacting using jena-API interface:
        // =====================================
        Model model = ((OntologyModel) owl).asGraphModel();     
        mgr = new OntDocumentManager();
	 	mgr.setProcessImports(true);
        spec = new OntModelSpec(OntModelSpec.OWL_MEM);
	    spec.setDocumentManager(mgr);
	    //spec.setReasoner(reasoner);
	    OntModel = ModelFactory.createOntologyModel(spec, model); 
    	OntModel.setStrictMode(false); 
       	rbgmModel = RBGModelFactory.createModel("PBM_MODEL");
    	try {
    		 buildrbgmModel();
    		 Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    	
    	try{
    	size=rbgmModel.getNamedClassNodes().size();
    	 }
    	catch(ConcurrentModificationException e)
    	{
    		try {
				Thread.sleep(4000);
				size=rbgmModel.getNodes().size();
			} catch (InterruptedException |OutOfMemoryError e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}}
    	
    	if(size==0 ||owl==null)
    	{
     		if(format!=null){
     		if(!format.toString().contains("RDF/XML"))
        	{
        		changeFormat(nameOnt,owl, manager);
        	}}
     		try{
            	OntModel=null;
            	rbgmModel=null;
     			buildMo();
               }
           catch(Exception e)
           {
        	   
           }
    	}
    	model=OntModel;
    	 
         wd = "."+File.separator+"temp"+File.separator;
         File file = new File(wd);
         if (file.exists() == false) {
                file.mkdir();
            }
         String filepath1 = nameOnt;
     	 filepath1="file:"+filepath1;
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
        
        System.out.println(entities.size()+"\t"+filepath1+"\t the number of concepts\t"+OntModel.listNamedClasses().toList().size()+"\t"+NumEntity+"\t"
        +entities.size()+"\t"+rbgmModel.getNodes().size());
        
        if(Controller.CheckBuildModel==false)
        {
        	Controller.CheckBuildModel=true;
        	
        }
    }
    
    private void buildMo()
    {
    	OntDocumentManager mgr = new OntDocumentManager();
	 	mgr.setProcessImports(true);
	    OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
	    spec.setDocumentManager(mgr);
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
    		OntModel.read(gzips, "");  
   	        OntModel.setStrictMode(false);//  
   	        rbgmModel = RBGModelFactory.createModel("PBM_MODEL");
   	        rbgmModel.setOntModel(OntModel);  		    
    	}
    	
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
 
   
  private void changeFormat(String name,OWLOntology owl, OWLOntologyManager manager)
  {
	  OWLDocumentFormat rdfxmlFormat = new RDFXMLDocumentFormat();
	  if(name.endsWith(".owl"))
		{
			 manager.setOntologyFormat(owl, rdfxmlFormat);
			 File temp=new File(name.substring(0, name.indexOf(".owl")));
			 
			 checkTemp1();
			 String wdn = "."+File.separator+"temp1"+File.separator;
	         File f= new File(wdn);
	         if (f.exists() == false) {
	                f.mkdir();
	            }
			 File file = null;
			try {
				file = File.createTempFile(name.substring(0, name.indexOf(".owl")), ".owl",f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//new File("."+File.separator+"temp1"+File.separator));
			 try {
				manager.saveOntology(owl, rdfxmlFormat, IRI.create(file));
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 File ff=new File(nameOnt);
			 String str=ff.getName().substring(0, ff.getName().length());
			 File newFile = new File(file.getParent(), str);//
			 try {
				Files.move(file.toPath(), newFile.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 nameOnt=newFile.getAbsolutePath();
			 temp.deleteOnExit();			 
		}
		 if(name.endsWith(".gz"))
		 {
			try {
				try {
					unzipFunction(name);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (DataFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 manager.removeOntology(owl);
			 File temp=new File(name.substring(0, name.indexOf(".gz")));
			 FileInputStream fin = null;
			try {
				fin = new FileInputStream (temp);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 checkTemp1();
			 String wdn = "."+File.separator+"temp1"+File.separator;
	         File f= new File(wdn);
	         if (f.exists() == false) {
	                f.mkdir();
	            }
			 File file = null;
			try {
				file = File.createTempFile(name.substring(0, name.indexOf(".owl")), ".owl",f);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//new File("."+File.separator+"temp1"+File.separator));
			 try {
				owl=manager.loadOntologyFromOntologyDocument(fin);
			} catch (OWLOntologyCreationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 try {
				manager.saveOntology(owl, rdfxmlFormat, IRI.create(file));
			} catch (OWLOntologyStorageException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 File ff=new File(nameOnt);
			 String str=ff.getName().substring(0, ff.getName().indexOf(".gz"));
			 File newFile = new File(file.getParent(), str);//
			 try {
				Files.move(file.toPath(), newFile.toPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 nameOnt=newFile.getAbsolutePath();
			 System.out.println("the file\t"+nameOnt);
			 temp.deleteOnExit();			 
		 }
	 	  
  }
  
  private static void checkTemp1()
  {
  	File folder=new File("."+File.separator+"temp1"+File.separator);
  	if(folder.isDirectory()){
  	for (File file: folder.listFiles()) {
  		if (!file.isDirectory()) file.delete();
      }}
  }
  
  private void buildrbgmModel()
  {
	//int interval = 20; // 10 sec
	//Date timeToRun = new Date(System.currentTimeMillis()*1000 + interval);
  	Timer timer = new Timer();
  	timer.schedule(new TimerTask() {
  	           public void run() {
  	        	   rbgmModel.setOntModel(OntModel);
  	        	   check=false;
  	        	   //System.out.println(check+ "\t We are here\t"+check+"\t"+rbgmModel.getNodes().size());
  	        	   timer.cancel();
  	        }
  	      }, 0,4000);
	
  }
   
    
  ////////////////// Another Build model for other ontology formats
    public  void BuildModelOnt1(InputStream nameOnt, String filepath) 
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
    public  int  findIndex(String nameX ){
    	
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
    public  int  findIndexName(String nameX ){
    	
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
 
 public static void main(String args[]) throws OWLOntologyCreationException, IOException
 {
     
     double start=System.currentTimeMillis();
   String fp1 = "D:/owl/owl/envo.owl.gz";
 //    String fp2 = "D:/owl/ncit.owl.gz";
     String fp3="D:/alsayed_SVN/AquaDiva/rdf/study.owl";
    
    Controller con=new Controller(fp1);
    con.runPartition();
     ModelBuild model=  new ModelBuild(fp3);
     model.build(); 
     double end=System.currentTimeMillis();
     System.out.println(model.getOWLModel()+"\t The  build model time---->"+(end-start)*.001+"\t sec \t"+model);
     System.out.println("The optimal num. of partition time---->"+(end-start)*.001+"\t sec \t"+model.OntModel.size()+"\t"+
        model.rbgmModel.getClassNodes().size());

 } 

}
