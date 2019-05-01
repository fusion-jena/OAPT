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
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import fusion.oapt.algorithm.partitioner.SeeCOnt.rankConcept;
import fusion.oapt.general.analysis.GeneralAnalysis;
import fusion.oapt.general.analysis.RichnessAnalysis;
import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.ModelBuild;

public class RankTest {

	 private  String filepath = null;   
     private  ArrayList<OntModel> models = null;
     List<File> filesInFolder, newFolder;

 
 public RankTest(String file)
 {
 	this.filepath=file;
 	models=new ArrayList<OntModel>();
 	filesInFolder=new ArrayList<File>();
 	newFolder=new ArrayList<File>();
 }


	public static void main(String[]args) throws IOException 
{
	 String file="resources/ont/";
	  RankTest AT=new RankTest(file);
	  AT.read(file);
	  AT.rank_analysis();
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
	Controller con;
	for(int i=0;i<size;i++)
	{
		File f=filesInFolder.get(i);
		//if(checkFormat(f))
		{
		   newFolder.add(f);
		   String path=f.getPath();
		    
		    System.out.println("\t the file:\t"+f.getName());
		}
	}
	//System.out.println("the number of Models:\t"+models.size());
}



public void rank_analysis() throws IOException
{
	  rankConcept RC;
	  HashMap<String,Double> mapClass=new HashMap<String, Double>();
	  HashMap<String, Set<String>> rankVector=new HashMap<String, Set<String>>();
	  OntModel om;
	  String content = "\t Concept ID\t Improt. Value \t Top-k concepts \t time";
	  File file = new File("src/resources/results/rank.txt");
	  File pfile=file.getParentFile();
	  if(!pfile.exists())
	  {
		   pfile.mkdir();
	  }
	  FileWriter fw = new FileWriter(file.getAbsoluteFile());
	  System.out.println(fw.toString());
	  BufferedWriter bw = new BufferedWriter(fw);
	  ModelBuild MB=null;
	  bw.write(content);
	  for(int i=0;i<newFolder.size();i++)
	  {
		  double start=System.currentTimeMillis(); double dur=0;
		  File f=newFolder.get(i);
		  String path=f.getPath();
		  System.out.println(i+"\t processing....."+f.getName());
		  if (Controller.CheckBuildModel == false)
		  {
   		  //BuildModel.BuildModelOnt(path);
   		  MB=new ModelBuild(path); MB.build();
   		 Controller.CheckBuildModel =true;
      	}
		  om=MB.getModel();//BuildModel.OntModel;//con.getOntModel1();//models.get(i);
		  RC=new rankConcept(om,MB.getRBGModel(),0);
		  System.out.println(i+"\t start ranking....."+f.getName());
		  RC.rank();
		  mapClass=RC.getMapClass();
		  rankVector=RC.getrankVector();
		  String[] rlist=RC.getRanked();
		  System.out.println(i+"\t  ranking \t"+f.getName()+"\t is done");
		  double end=System.currentTimeMillis();
		  dur=(end-start)*.001;
		  content="\n"+f.getName();
		  bw.write(content); 
		  int j=0;
		  for (HashMap.Entry<String, Double> entry : mapClass.entrySet()) {
	    	    //System.out.println("the ranking concepts\t"+entry.getKey()+" : "+entry.getValue()+ "\t"+rlist[j]); j++;
			    content="\n"+"the ranking concepts\t"+entry.getKey()+" : "+entry.getValue()+ "\t"+rlist[j]; j++;
			    bw.write(content); 
			    if(j>=10) break;
	    	}	  
		  
		  content="\n the time to rank the ontology: \t"+dur;
		  bw.write(content);
		 // bw.write(content); 
		  Controller.CheckBuildModel = false;
		  om=null; RC=null;mapClass=null;
	  }
	  bw.close();
}
 
   


 
 public OntModel getOntModel1()
 {
     return models.get(0);
 }
  




}
