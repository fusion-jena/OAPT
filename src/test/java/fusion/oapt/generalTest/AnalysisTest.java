package fusion.oapt.generalTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
//import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;



import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntTools;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import fusion.oapt.general.analysis.GeneralAnalysis;
import fusion.oapt.general.analysis.RichnessAnalysis;
import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.ModelBuild;



public class AnalysisTest {

		//step-1 read input ontologies and build ontology models
		
        private  String filepath = null;   
        private  ArrayList<OntModel> models = null;
        List<File> filesInFolder, newFolder;
   
    
    public AnalysisTest(String file)
    {
    	this.filepath=file;
    	models=new ArrayList<OntModel>();
    	filesInFolder=new ArrayList<File>();
    	newFolder=new ArrayList<File>();
    }
  
 
	public static void main(String[]args) throws IOException 
   {
	 String file="resources/ont";
	  AnalysisTest AT=new AnalysisTest(file);
	  AT.read(file);
	  AT.analysis();
   }
   
   public   void read(String file) throws IOException  
   {
   	  try {
		filesInFolder = Files.walk(Paths.get(file))
		        .filter(Files::isRegularFile)
		        .map(Path::toFile)
		        .collect(Collectors.toList());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   	//to be used in java 7
   /*	File folder = new File(file);
   	File[] listOfFiles = folder.listFiles();

   	for (File file1 : listOfFiles) {
   	    if (file1.isFile()) {
   	        filesInFolder.add(file1);
   	    }
   	}*/
   	 int size=filesInFolder.size();
   	System.out.println("the total number number of owl files:\t"+size);
   	for(int i=0;i<size;i++)
   	{
   		File f=filesInFolder.get(i);
   		//if(checkFormat(f))
   		{
   		   newFolder.add(f);
   		   System.out.println("\t the file:\t"+f.getName());
   		}
   	}
   	//System.out.println("the number of Models:\t"+models.size());
  }
   
   
   
   public void analysis() throws IOException
   {
	  GeneralAnalysis GA;
	  RichnessAnalysis RA;
	  OntModel om;
	  String content = "Ontology\t No. class\t No. of total class \t No. of sub \t No. of Prop \t No. of object Pro. "
	  		+ "\t No. of data prop \t No. of Individual \t No. of comments \t No. of labels \t Design metric \t KB metric"
	  		+ " \t class metric \t richness \t time";
	  File file = new File("src/resources/results/analysis.txt");
	  File pfile=file.getParentFile();
	  if(!pfile.exists())
	  {
		   pfile.mkdir();
	  }
	  FileWriter fw = new FileWriter(file.getAbsoluteFile());
	  System.out.println(fw.toString());
	  BufferedWriter bw = new BufferedWriter(fw);
	  ModelBuild MB=null;
	  Controller con=new Controller();
	  bw.write(content);
	  for(int i=0;i<4;i++)
	  {
		  double start=System.currentTimeMillis(); double dur=0;
		  File f=newFolder.get(i);
		  String path=f.getPath();
		  System.out.println(i+"\t processing....."+f.getName());
		  con=new Controller(path);
 		  if (con.CheckBuildModel == false) {
      		MB=new ModelBuild(path); MB.build();
      		Controller.CheckBuildModel =true;
      	}
		  om=con.getOntModel();
		  GA=new GeneralAnalysis(om);
		  GA.computeStatics();
		  RA=new RichnessAnalysis(om, con.getRBGModel()); 
		  double end=System.currentTimeMillis();
		  dur=(end-start)*.001;
		  content="\n"+f.getName()+"\t"+GA.getNumClass()+"\t"+GA.getTotnumClass()+"\t"+GA.getNumSubClass()+"\t"+GA.getNumProp()
				  +"\t"+GA.getNumObectPro()+"\t"+GA.getNumDataPro()+"\t"+GA.getNumIndivial()
				   +"\t"+GA.getNumComments()+"\t"+ GA.getNumLabels()+"\t"+RA.computeDesignMertic()+"\t"+RA.computeKBMetric()
				   +"\t"+RA.computeClassmetric()+"\t"+RA.computeRichness()+"\t"+dur;
		  
		  bw.write(content); 
		  Controller.CheckBuildModel = false;
		  om=null; GA=null;RA=null;
	  }
	  bw.close();
   }
    
      

 
    
    public OntModel getOntModel1()
    {
        return models.get(0);
    }
     
   
    private boolean checkFormat(File file) throws IOException 
    {
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    	OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
        config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
        InputStream in, gzips;
    	Boolean checked=false;
    	try {
			in = new FileInputStream(file);
			gzips = new GZIPInputStream(in);
			StreamDocumentSource documentSource = new StreamDocumentSource(gzips);
			OWLOntology owl=manager.loadOntologyFromOntologyDocument(documentSource, config); //.loadOntologyFromOntologyDocument(gzips); 
		    // OWLOntologyFormat owlxmlFormat = new RDFXMLOntologyFormat();
		     //OWLOntologyFormat format=manager.getOntologyFormat(owl); 
			OWLDocumentFormat format=manager.getOntologyFormat(owl);   
			System.out.println("the owl format\t"+format.toString());
			 if(format.toString()=="RDF/XML")
				checked= true;
			 else
				 checked=false;
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	
    } catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return checked;
    }
    
    // A small function to print shortest path length between owl concepts
    private void computeDistance(OntModel model)
    {
    	    	
    	float dis=0;
    	List<OntClass> list=model.listNamedClasses().toList();
    	for(int i=0;i<list.size()-1;i++)
    	{
    		OntClass cl1= list.get(i);
    		for(int j=i+1;j<list.size();j++)
    		{
    			OntClass cl2=list.get(j);
    			Resource cc1 = model.getOntResource(cl1.toString());
        	    RDFNode cc2 =  (RDFNode) model.getOntClass(cl2.toString());
        	    //used by java 7
        	  	org.apache.jena.ontology.OntTools.Path shortestPath = null;//OntTools.findShortestPath(model, cc1, cc2, null);//,Filter.any);
        		if(shortestPath!=null)
        		{
        			//System.out.println(cl1.getLocalName()+"\tthe path\t"+shortestPath.size()+"\t"+cl2.getLocalName());
        			dis+=shortestPath.size();
        		}
        			
        		if(shortestPath ==null) dis+=10;//System.out.println(cl1.getLocalName()+"\t"+cl2.getLocalName());
    		}
    	}
    	System.out.println("the ontology dis\t"+dis/model.size());
    }

	}


