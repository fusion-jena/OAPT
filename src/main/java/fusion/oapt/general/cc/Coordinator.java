package fusion.oapt.general.cc;

import fusion.oapt.algorithm.partitioner.SeeCOnt.Cluster;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.apache.jena.ontology.OntModel;
import fusion.oapt.model.NodeList;


public class Coordinator
{
	public static boolean FinishPartitioning ;
	public static  NodeList CH;
	public static LinkedHashMap<Integer, Cluster> clusters;
	public static  int KNumCH;
	static ArrayList<OntModel> modules=new ArrayList<OntModel>();
	public static int levelDetail=1;
	public static int argVisulization;


 public Coordinator(String fp1) 
 {
	 clusters=new LinkedHashMap<Integer, Cluster>();
	 CH = new NodeList();
 }
 
 
 public  ArrayList<OntModel> getModules()
 {
 	return modules;
 }

 
 public static  LinkedHashMap<Integer, Cluster> getClusters()
 {
	return clusters; 
 }
  
   	
}
