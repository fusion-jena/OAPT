package fusion.oapt.algorithm.merger.graphMerge;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.shared.BadURIException;

import fusion.oapt.algorithm.merger.graphMerge.mapping.MappingFile;
import fusion.oapt.general.graphToOnto.GraphToOntology;
import fusion.oapt.general.ontoTograph.OntologyToGraph;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;


public class GraphMerge 
{
	
	static GraGra gramO1 =  new GraGra(true);
	static GraGra gramO2 =  new GraGra(true);
	static List<File> filesInFolder;
	String path;
	static int totalMapp;
	public GraphMerge(List<File> files)
	{
		this.filesInFolder=files;
		this.path=null;
		totalMapp=0;
	}
	public GraphMerge(String pName)
	{
		filesInFolder=new ArrayList<File>();
		this.path=pName;
		totalMapp=0;
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
	
	public static String convertOWL(String fName) throws Exception
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
     	// save the result of the transformation 
 		FileWriter fw = new FileWriter(fileOGName);
 		System.out.println("The merged OWL ontology is saved in " + fileOGName);
 		try{
			model.write( fw, "RDF/XML-ABBREV");
	 		}
	 		catch(BadURIException e){e.getMessage();
	 		}
 		return fileOGName;
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
         //if(model!=null)
        // System.out.println(fileName+ "\tthis is the model\t");
        /********* Load the meta-model **********/
        String fmtatOnto= "resources/metamodelOwl.ggx";  
        GraGra graphGrammar =  new GraGra(true);		
     	graphGrammar.setFileName("resources/mergeTemp/"+name);		
     	graphGrammar.load(fmtatOnto);       	
     	
     	
     	/********* Start process  of transformation ********/
     	Graph typeGrap=graphGrammar.getTypeGraph();
     	OntologyToGraph og= new OntologyToGraph(model,typeGrap,fileName); 
     	return og.getFileName();
     	
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
        totalMapp+=fichCorresp.getNoMaps();
        return fichCorresp.getMappingFileName();
		
	}
	
	public String SetMerge() throws Exception
	{
		
	List<String> graphNames=new ArrayList<String>();
	if(this.path==null)
			{
	for(int i=0;i<filesInFolder.size();i++)
     {
    	//convert the owl files into ggx graph and save the location of these graphs
    	 String fname=filesInFolder.get(i).getPath();
    	 graphNames.add(convert(fname));
     }}
	else
	{
	String[] parts = this.path.split("\n");
    for (int i=0; i<parts.length; i++){
   	 String fname = parts[i];
   	 graphNames.add(convert(fname));
    }}
     System.out.println("the total number number of ggx files:\t"+graphNames.size());
     String fgraph=graphNames.get(0);
     for(int i=1;i<graphNames.size();i++)
     {
    	 String mapF=map(fgraph,graphNames.get(i));
    	 fgraph=merge(mapF,fgraph,graphNames.get(i));
      }
      return convertOWL(fgraph);
	}
	
	public int getNoMaps()
	{
		return totalMapp;
	}
}
