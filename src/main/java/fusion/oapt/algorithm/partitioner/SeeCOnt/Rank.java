
package fusion.oapt.algorithm.partitioner.SeeCOnt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import java.util.Set;

//import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Findk.Sorter;
import fusion.oapt.model.Node;




import org.apache.jena.ontology.OntModel;

public class Rank {
	public static LinkedHashMap<Integer, Cluster> clusters; 
	private static OntModel model1;
	public static ArrayList<OntModel> models ;
	private static float[] importantAll=null ;
	public static  String [] SNameOnt=null;
	public static HashMap<String,Double> mapClass=null;
	private int numEntity;
	static HashMap<String,Set<String>> rankVector;
	ModelBuild MB=new ModelBuild();
	
	public  Rank (ModelBuild model)
	{
	 MB=model;
	 this.model1=MB.getModel();//BuildModel.OntModel;
     this.numEntity=MB.NumEntity;
     clusters=new LinkedHashMap<Integer, Cluster>();
     models=new ArrayList<OntModel>();
     rankVector=new HashMap<String,Set<String>>();
	}
	
	public  Rank (int ch, ModelBuild model)
	{
	 this.model1=model.getModel();//BuildModel.OntModel;
     this.numEntity=model.NumEntity;
     clusters=new LinkedHashMap<Integer, Cluster>();
	}
 

@SuppressWarnings("unchecked")
public  void rankConcepts()
{
	rankConcept RC=new rankConcept(model1,MB.getRBGModel(),3);
	mapClass=new HashMap<String, Double>();
	RC.rank();
	mapClass=RC.getMapClass();
	rankVector=RC.getrankVector();
	SNameOnt=RC.getRanked();
	MB.SortedNameOnt= SNameOnt;
	
   
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////Rank_old_Phase ////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public  void rank_old_Phase()
{
	importantAll = new float [MB.NumEntity];
	for (int i=0; i<MB.NumEntity; i++){
		ArrayList<Node> temp = MB.Connexion (MB.entities.get(i));
		if (temp.size() > 0) {
			importantAll[i] = (float) (temp.size() + chartoint(MB.entities.get(i).getLocalName()));
		}
	}
}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////Sort_old_Phase ////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public  void sort_old_Phase(){

	Sorter str = new Sorter();
	SNameOnt = new String [MB.NumEntity]; 
	SNameOnt = str.sort(importantAll, MB.entities);
	MB.SortedNameOnt= SNameOnt;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////Sort_Phase ////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public  void sort_Phase(){
	SNameOnt = new String [mapClass.size()]; 
    HashMap<String, Double> sortedMap = sortByValue(mapClass);
    //comment in case using java 7
	/*     mapClass.entrySet().stream()
	    .sorted(Entry.comparingByValue())
	    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
	                              (e1, e2) -> e1, LinkedHashMap::new));*/
	Iterator<String> ss=sortedMap.keySet().iterator();
	 int i=mapClass.size()-1;
	 while(ss.hasNext())
	 {
		 String s=ss.next();
		 SNameOnt[i]=s; i--;  
	 }
	  
	 MB.SortedNameOnt= SNameOnt;
}
  
  //to be used in case of using Java 7
  private static <K, V extends Comparable<? super V>> HashMap<K, V>  sortByValue( HashMap<K, V> map )
{
  List<Map.Entry<K, V>> list =
      new LinkedList<>( map.entrySet() );
  Collections.sort( list, new Comparator<Map.Entry<K, V>>()
  {
      @Override
      public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 )
      {
          return ( o1.getValue() ).compareTo( o2.getValue() );
      }
  } );

  HashMap<K, V> result = new LinkedHashMap<>();
  for (Map.Entry<K, V> entry : list)
  {
      result.put( entry.getKey(), entry.getValue() );
  }
  return result;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static double chartoint(String iname ){
	double r=0;
	iname = iname.toLowerCase(); 
	for (int i=0; i<iname.length(); i++){
		char xx= iname.charAt(i);
		r= r+ (int) xx;
	}
	r= r/iname.length();
	return r;
}

}