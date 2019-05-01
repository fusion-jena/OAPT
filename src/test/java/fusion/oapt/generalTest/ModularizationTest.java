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

import fusion.oapt.algorithm.partitioner.SeeCOnt.ModuleEvaluation;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Findk.FindOptimalCluster;
import fusion.oapt.general.analysis.GeneralAnalysis;
import fusion.oapt.general.cc.Controller;
import fusion.oapt.general.cc.Coordinator;

import org.apache.jena.ontology.OntModel;

public class ModularizationTest {
	
	 private  String filepath = null;   
     private  ArrayList<OntModel> models = null;
     List<File> filesInFolder;

 
 public ModularizationTest(String file)
 {
 	this.filepath=file;
 	models=new ArrayList<OntModel>();
 	filesInFolder=new ArrayList<File>();
 }


	public static void main(String[]args) throws IOException 
{
  
	 String file="resources/ont";
	 System.out.println(System.getProperty("java.class.path"));
	 ModularizationTest MA=new ModularizationTest(file);
	  MA.read(file);
	
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
		//this part can be used in case of java 7
	/*  File folder = new File(file);
	   	File[] listOfFiles = folder.listFiles();

	   	for (File file1 : listOfFiles) {
	   	    if (file1.isFile()) {
	   	        filesInFolder.add(file1);
	   	    }
	   	}*/
	System.out.println("the total number number of owl files:\t"+filesInFolder.size());
	Controller con;
	ModuleEvaluation ME;
	double start=0, end=0;
	String content = "Ontology\t  No. of modules \t Time \t HOMO \t HEMO \t rel. Size";
	File Ofile = new File("src/resources/merge/analysis.txt");
	File pfile=Ofile.getParentFile();
	  if(!pfile.exists())
	  {
		   pfile.mkdir();
	  }
	FileWriter fw = new FileWriter(Ofile.getAbsoluteFile());
	 BufferedWriter bw = new BufferedWriter(fw);
	 bw.write(content);
	for(int i=0;i<filesInFolder.size();i++)
	{
		File f=filesInFolder.get(i);
		String path=f.getPath();
		System.out.println(i+"\t the file:\t"+path);
		start=System.currentTimeMillis();
		con=new Controller(path);
		models.add(con.getOntModel());
		FindOptimalCluster OP=new FindOptimalCluster(con.MB);
		int NumCH =OP.FindOptimalClusterFunc();
		Coordinator.KNumCH = NumCH;
		//con.runPartition();
		con.InitialRun_API("SeeCOnt",Coordinator.KNumCH);
		//ArrayList<OntModel> modules=Coordinator.getModules();
		end=(System.currentTimeMillis()-start)*.001;
		content="\n"+f.getName()+"\t"+NumCH+"\t"+end;
		 ME=new ModuleEvaluation(con.getModelBuild(),con.getClusters());
		 ME.Eval_SeeCont();
		 content="\n"+f.getName()+"\t"+NumCH+"\t"+end+ "\t"+ME.getHoMO()+"\t"+ME.getHEMo()+"\t"+ME.getRS();
		 //stream.write(content.getBytes());
		 bw.write(content); 
        //System.out.println(ME.getHEMo()+"\t number of clusters---"+ME.getHoMO()+"\t"+ME.getRS());
		System.out.println(NumCH+"\t the file:\t"+f.getName()+"\t time \t"+ end);
		
	}
	//stream.close();
	bw.close();
	//System.out.println("the number of models:\t"+models.size());
}


public void modularize() throws IOException
{
	  GeneralAnalysis GA;
	  String content = "Ontology\t No. calss\t No. of total class \t No. of sub \t No. of Prop"+"\t No. of object Pro. \t No. of data prop";
	  File file = new File("src/resources/results/analysis.txt");
	  File pfile=file.getParentFile();
	  if(!pfile.exists())
	  {
		   pfile.mkdir();
	  }
	  FileWriter fw = new FileWriter(file.getAbsoluteFile());
	  BufferedWriter bw = new BufferedWriter(fw);
	  bw.write(content);
	  
	  for(int i=0;i<models.size();i++)
	  {
		  File f=filesInFolder.get(i);
	   	  OntModel om=models.get(i);
		  GA=new GeneralAnalysis(om);
		  GA.computeStatics();
		  content="\n"+f.getName()+"\t"+GA.getNumClass()+"\t"+GA.getTotnumClass()+"\t"+GA.getNumClass()+"\t"+GA.getNumProp()+"\t"+GA.getNumObectPro()+"\t"+GA.getNumDataPro();
		  bw.write(content);
	  }
	  bw.close();
}
 
   

 public OntModel getOntModel1()
 {
     return models.get(0);
 }
  
 

}
