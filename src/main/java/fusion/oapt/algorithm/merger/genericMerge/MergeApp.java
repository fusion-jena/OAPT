package fusion.oapt.algorithm.merger.genericMerge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.zip.GZIPInputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.formats.RDFXMLDocumentFormat;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.BadURIException;

import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
//import EDU.oswego.cs.dl.util.concurrent.TimeoutException;

import fusion.oapt.algorithm.merger.graphMerge.GraphComposition;
import fusion.oapt.algorithm.merger.graphMerge.GraphMerge;
import fusion.oapt.algorithm.merger.graphMerge.MappingAnalysis;
import fusion.oapt.algorithm.merger.graphMerge.mapping.MappingFile;
import fusion.oapt.algorithm.merger.matchingMerge.MatchingMerge;
import fusion.oapt.general.graphToOnto.GraphToOntology;
import fusion.oapt.general.gui.MergingPanel;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.general.ontoTograph.OntologyToGraph;


public class MergeApp {
	 List<File> filesInFolder;
	 String filesPath;
	 
	 public MergeApp(String path)
	 {
		this.filesPath=path;
		filesInFolder=new ArrayList<File>();
	 }

	public static void main(String[] args) throws Exception {
		String path="D:/test_ont/test";
		MergeApp MA=new MergeApp(path);
		MA.readFiles(path);
        MA.run();
	}
	
	public   void readFiles(String path) throws IOException  
	{
		File directory = new File(path);
	    File[] fList = directory.listFiles();
		for (File file : fList)
		{
		   if (file.isFile() && file.getName().endsWith(".owl")){
		     	filesInFolder.add(file);
		     	  }
		  }
     System.out.println("the total number number of owl files:\t"+filesInFolder.size());
	}
	
	public void run() throws Exception
	{
		int k=2;
		String loc="resources/merge/result/merge.owl";
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology owl=manager.createOntology();
		MergeEvaluation ME;
		switch(k)
		{
		case 1:
			simpleMerge SM=new simpleMerge();
			owl=SM.merge(filesInFolder,loc);
			//saveOntology(owl,loc);
			ME=new MergeEvaluation(filesInFolder,owl);
			ME.computeCoverage_New();
			//ME.computeRedudency();
			break;
		case 2:
			  //owl=execute_parallel();
			  GraphMerge GM=new GraphMerge(filesInFolder);
			  String model=GM.SetMerge(); 
			   owl=saveOntologyModel(model,loc);
			  ME=new MergeEvaluation(filesInFolder,owl);
			  ME.computeCoverage_New();
			  int map=GM.getNoMaps();
			  ME.computeRedudency(map);
			  break;
		case 3:
			 mappingMerge MM=new mappingMerge(filesInFolder);
			 owl=MM.run();
			 saveOntology(owl,loc);
			break;
		case 4:
			MatchingMerge mm=new MatchingMerge(filesInFolder);
			owl=mm.run();
			saveOntology(owl,loc);
			break;
		default:
			System.out.println("please enter a value between 1 and 4");
		}
		 File file=new File("resources/merge/temp");
			for(File f: file.listFiles())
			        f.delete();
		  File file1=new File("resources/mergeTemp");
			for(File f: file1.listFiles())
			        f.delete();
	}
	
	public OWLOntology saveOntologyModel(String model, String loc) throws IOException, OWLOntologyCreationException
	{
		InputStream in=null;
    	InputStream fileStream=null;
    	if(model.endsWith(".rdf"))  return null;
    	if(model.endsWith(".owl"))
    	 		in = new FileInputStream(model);
    	else if(model.endsWith(".gz"))
    		{
    			fileStream = new FileInputStream(new File(model));
    			in= new GZIPInputStream(fileStream);
    		}
    	 OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	 OWLOntology owl;
		 owl = manager.loadOntologyFromOntologyDocument(in);
		System.out.println(owl.getAxiomCount()+"\t The OWL ontology \t " +model);
		return owl;
	}
	
	public  void saveOntology(OWLOntology ontology, String loc) throws Exception 
	{
         OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        File file = new File(loc);
       // RDFXMLOntologyFormat owlxmlFormat = new RDFXMLOntologyFormat();
        RDFXMLDocumentFormat owlxmlFormat=new RDFXMLDocumentFormat();
        manager.saveOntology(ontology, owlxmlFormat, IRI.create(file.toURI()));
       
    }
	
	 public static void mainA(String numAlgorithm) throws Exception { //samira*
			
			String path = OPATgui.ontologyFileTextFieldMerge.getText();//Samira*
			MergeApp MA=new MergeApp(path);
			MA.runA(numAlgorithm);
		}
		
		public void runA(String numAlgorithm) throws Exception
		{
			String loc="resources/merge/result/merge"+numAlgorithm+".owl";
			String listofSource = OPATgui.ontologyFileTextFieldMerge.getText(); 
			OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology owl=manager.createOntology();
			MergeEvaluation ME;
			switch(numAlgorithm)
			{
			case "GenericMerge":
				simpleMerge SM=new simpleMerge(listofSource);
				owl=SM.merge(listofSource,loc);
				ME=new MergeEvaluation(filesInFolder,owl);
				ME.computeCoverage_New();
				System.out.println("the process is done...");
				break;
			case "GraphMerge":
				//GraphMerge GM=new GraphMerge(filesInFolder);
				 GraphMerge GM = new GraphMerge(listofSource); //samira* (if it is empty do not let to continue)
				 String model = GM.SetMerge(); 
				 owl = saveOntologyModel(model,loc);
				 ME = new MergeEvaluation(filesInFolder,owl);
				 ME.computeCoverage_New();
				 System.out.println("the process is done...");
				break;
			case "APIMerge":
				 mappingMerge MM=new mappingMerge(filesInFolder);
				 owl=MM.run();
				 saveOntology(owl,loc);
				break;
			case "MatchingMerge":
				MatchingMerge mm=new MatchingMerge(filesInFolder);
				owl=mm.run();
				saveOntology(owl,loc);
				break;
			default:
				System.out.println("please enter a value between 1 and 4");
			}
		}
		
		@SuppressWarnings({ "rawtypes", "unchecked" })
		private OWLOntology execute_parallel() throws Exception
		{
			 
			List<String> graphNames=new ArrayList<String>();
			double start=System.currentTimeMillis();
			System.out.println("the owl file conversion process---\n");
		     for(int i=0;i<filesInFolder.size();i++)
		     {
		    	 String fname=filesInFolder.get(i).getPath();
		    	 graphNames.add(convert(fname));
		     }
		     double end=System.currentTimeMillis();
		     System.out.println("the total number number of ggx files:\t"+graphNames.size()+"\t in time \t"+(end-start)*.001 +"\t sec");
			 int no_of_tasks=filesInFolder.size()/2;
			 int no_of_proc=Runtime.getRuntime().availableProcessors();
			
			 start=System.currentTimeMillis();
			 List< Future> futurelist=new ArrayList<Future>();
			 ScheduledExecutorService eservice=Executors.newScheduledThreadPool(no_of_proc);
			 CompletionService<Object> cservice=new ExecutorCompletionService<Object>(eservice);
			 for(int i=0;i<no_of_tasks;i++)
			 {
				 String Sgraph=graphNames.get(i*2);
				 String Tgraph=graphNames.get(i*2+1);
				 futurelist.add(cservice.submit(new FJTaskP(i, Sgraph, Tgraph)));
				 
			 }
			System.out.println(no_of_tasks+"<---the number of generated tasks--->"+futurelist.size());
			List<String> files=new ArrayList<String>();
			String mergeName=null;
			int k=0;
			for(int i=0; i<futurelist.size() ;i++)
			{
				Future future=futurelist.get(i);
				try
				{
					//System.out.println(future+"-----"+future.isDone());
					try 
					{
						//Thread.currentThread().join(100);
						mergeName=(String) cservice.take().get(3,TimeUnit.SECONDS);
						System.out.println(mergeName+"\t state---"+Thread.currentThread().getState()+"\t"+k);
					} catch (TimeoutException e) {
							e.printStackTrace();
					}
					files.add(mergeName);
					if(future.isDone())
						{
						 long id=Thread.currentThread().getId();  
						 //Thread.currentThread().interrupt();
						}
					System.out.println(future+"-----"+future.isDone()+"----result:\t"+mergeName);
					k++; 
				}
				catch(InterruptedException e){System.out.println("Interrupted");}
				catch(ExecutionException e){}
			}
			end=new java.util.Date().getTime();
			eservice.shutdown();
			OWLOntology owl=convertOWL(mergeName);
			System.out.println("parallel merging time:"+ (end-start)*0.001+" sec");
			return owl;
		}
		
		private class FJTaskP implements Callable
		{
			private int seq;
			int matchStrat;
			String graph1,graph2;
			public FJTaskP(int i,  String source, String target)
			{
				seq=i;
				this.graph1=source;
				this.graph2=target;
			}
			
			public String call() throws Exception
			{
				
				String fgraph=null;			
				try
				{
					Thread.sleep(100);
				
					 String mapF=map(graph1,graph2);
			    	 fgraph=merge(mapF,graph1,graph2);
	 		 }
				catch(InterruptedException e){System.out.println("the system is interrupted");}
				return fgraph;
	    	}
	    }

		
		public static String merge(String map, String file1, String file2) throws Exception
		   {
			   GraGra gramO1 =  new GraGra(true);		
				gramO1.setFileName("fileName");		
				gramO1.load(file1);
				gramO1.setName("Grammar");
								
			    GraGra gramO2 = new GraGra(true);		
				gramO2.setFileName("fileName");		
				gramO2.load(file2);
				gramO2.setName("Grammar");
											
				   	
		    // construct the global graph (OG) : in the beginning OG= O1=> then we must imported O1 in OG 
		   	 GraGra gramGlobal =  new GraGra(true);
		   	 gramGlobal.importTypeGraph(gramO1.getTypeGraph(), true);
		   	 gramGlobal.getTypeGraph().setName("Metamodel");
		     gramGlobal.importGraph(gramO1.getGraph(0), true);    	
		   	 Graph hostOG= gramGlobal.getGraph(0);
		   	 hostOG.setName("Ontology");    	         	  
		    	
		   	 MappingAnalysis mappAna = new MappingAnalysis ();
		   	 mappAna.getCorrespNodes(map, gramO1.getGraph(0), gramO2.getGraph(0));
		       
		   	 List <Node> comNode =  mappAna.getCommCC();  
		   	 List <String> commenNode = new LinkedList();
		   	 // List of common Nodes
		   	 for( Node nn : comNode )
		   	 {
		   		String nodeIRI= nn.getAttribute().getMemberAt(0).toString();
		   		if(nodeIRI.endsWith("\""))    		
		   			nodeIRI= (nodeIRI.substring(1, nodeIRI.length()-1));
		   		    commenNode.add(nodeIRI);
		   	}
		   	
		   	// List of equivalent terms
		   	List <List> equivalentNode= mappAna.getEquivCC();     	
		    
		    // List of synonyms terms
		    List <List> synonymNode= mappAna.getSemanticCC();     	
		       	
		    // Subsumption Relation
		    List <List> subClassNode= mappAna.getIsaCC();  	
		       	    	
		    GraphComposition GC= new GraphComposition (gramO1, gramO2, gramGlobal, commenNode, equivalentNode, synonymNode, subClassNode);
		    return GC.getMergedFile();
		    	
		   }
		
		public static String map(String gName1, String gName2) throws Exception
		{
			GraGra gram1 =  new GraGra(true);		
			gram1.setFileName("fileName");		
			gram1.load(gName1);
			gram1.setName("Grammar");
			
			GraGra gram2 = new GraGra(true);		
			gram2.setFileName("fileName");		
			gram2.load(gName2);
			gram2.setName("Grammar");
			
			MappingFile  fichCorresp= new  MappingFile(gram1,gram2);
	        fichCorresp.createMappingFile();
	        return fichCorresp.getMappingFileName();
			
		}

		public static  String convert(String name) throws Exception
		{
			String fileName=new File(name).getName();
			int pos = fileName.lastIndexOf(".");
			if (pos > 0) {
			    fileName = fileName.substring(0, pos);
			}
			 InputStream   in1 = new FileInputStream(new File( name));
			 OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
			 OntDocumentManager mgr = new OntDocumentManager();
			 spec.setDocumentManager(mgr);
	    	 OntModel model = ModelFactory.createOntologyModel(spec,null);
	    	 model.setStrictMode(false);
	 	     model.read(in1,null);	
	 		 model.prepare();
	         /********* Load the meta-model **********/
	        String fmtatOnto= "resources/metamodelOwl.ggx";  
	        GraGra graphGrammar =  new GraGra(true);		
	     	graphGrammar.setFileName("temp/"+name);		
	     	graphGrammar.load(fmtatOnto);       	
	     	
	     	
	     	/********* Start process  of transformation ********/
	     	Graph typeGrap=graphGrammar.getTypeGraph();
	     	OntologyToGraph og= new OntologyToGraph(model,typeGrap,fileName); 
	     	return og.getFileName();
	     	
		}
		
		public static OWLOntology convertOWL(String fName) throws Exception
		{
			GraGra gragra;	
			gragra =  new GraGra(true);		
			gragra.setFileName("fileName");		
			gragra.load(fName);
			gragra.setName("GRammar");
					
		    // create the owl file 		
			String fileOGName =fName.substring(0,fName.indexOf("."))+".owl";
			
			new File(fileOGName); 	
			OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);		 	
		
			/**** Transformation *****/ 
	     	//start the process of transformation  
	     	long begin = System.currentTimeMillis();
	     	
	     	new GraphToOntology(model, gragra);
	     	
	     	long end = System.currentTimeMillis();
	     	float time = ((float) (end-begin)) / 1000f;
	     	System.out.println( "\nExecution time of transformation is : "+ time);
	     	
	     	// save the result of the transformation 
	 		FileWriter fw = new FileWriter(fileOGName);
	 		System.out.println("The OWL ontology is saved in " + fileOGName);
	 		try{
			model.write( fw, "RDF/XML-ABBREV");
	 		}
	 		catch(BadURIException e){e.getMessage();}
	 		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
			OWLOntology owl=manager.loadOntologyFromOntologyDocument(new File(fileOGName));
	 		return owl;
		
		}
		
	   
	   
}
