package fusion.oapt.generalTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.jena.ontology.OntModel;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Findk.FindOptimalCluster;
import fusion.oapt.general.cc.Controller;


public class OptimalTest {

	//step-1 read input ontologies and build ontology models
	
    private  String filepath = null;   
    private  ArrayList<OntModel> models = null;
    List<File> filesInFolder, newFolder;


public OptimalTest(String file)
{
	this.filepath=file;
	models=new ArrayList<OntModel>();
	filesInFolder=new ArrayList<File>();
	newFolder=new ArrayList<File>();
}


public static void main(String[]args) throws IOException 
{
 String file="resources/ont/cmt.owl";
 OptimalTest OT=new OptimalTest(file);
  OT.read(file);
  OT.analysis();
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
		   //System.out.println("\t the file:\t"+f.getName());
		}
	}
	//System.out.println("the number of Models:\t"+models.size());
}



public void analysis() throws IOException
{
   String content = "Ontology\t No. class \t No. rank \t No. LB \t No. UB \t time ";
  File file = new File("src/resources/results/optimal.txt");
  File pfile=file.getParentFile();
  if(!pfile.exists())
  {
	   pfile.mkdir();
  }
  FileWriter fw = new FileWriter(file.getAbsoluteFile());
  System.out.println(fw.toString());
  BufferedWriter bw = new BufferedWriter(fw);
  bw.write(content);
  int size=filesInFolder.size();
  for(int i=0;i<size;i++)
  {
	  double start=System.currentTimeMillis(); double dur=0;
	  File f=newFolder.get(i);
	  String path=f.getPath();
	  System.out.println(i+"\t processing....."+f.getName());
	  FindOptimalCluster OPC=new FindOptimalCluster(path);
	  OPC.FindOptimalClusterFunc();
	  int k=OPC.getOptCH();
	  int LB=OPC.getLB();
	  int UB=OPC.getUB();
	  int noentity=0;//OPC.getNoEntity();
	  double end=System.currentTimeMillis();
	  dur=(end-start)*.001;
	  content="\n"+f.getName()+"\t"+noentity+"\t"+k+"\t"+LB+"\t"+UB
			  +"\t"+dur;
	  
	  bw.write(content); 
	  Controller.CheckBuildModel = false;
	  
  }
  bw.close();
}

  



public OntModel getOntModel1()
{
    return models.get(0);
}
 
}
