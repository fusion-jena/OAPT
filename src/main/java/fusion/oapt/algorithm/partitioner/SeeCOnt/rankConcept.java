package fusion.oapt.algorithm.partitioner.SeeCOnt;


import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.ConversionException;
/*import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntProperty;
import org.apache.jena.ontology.OntTools;
import org.apache.jena.ontology.OntTools.Path;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDFS;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;*/


import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;
import fusion.oapt.model.RBGModel;

//import fusion.oapt.general.cc.BuildModel;



public class rankConcept {
	
	OntModel model;
	HashMap<String,Set<String>> rankVector;
	HashMap<Node,Set<Node>> nodeRankVector;
    HashMap<String,Double> mapClass=null;
    private static float[] importantAll=null ;
	int level;
	String SortedNameOnt[];
	RBGModel rbg;
	public rankConcept()
	{
		model=null;//BuildModel.OntModel;
		rankVector=new HashMap<String,Set<String>>();
		nodeRankVector=new HashMap<Node,Set<Node>>();
		mapClass=new HashMap<String, Double>();
		level=0;
		rbg=null;
	}
	
	public rankConcept(int d)
	{
		model=null;//BuildModel.OntModel;
		rankVector=new HashMap<String,Set<String>>();
		nodeRankVector=new HashMap<Node,Set<Node>>();
		mapClass=new HashMap<String, Double>();
		level=d;
		rbg=null;	
	}
	
	public rankConcept(OntModel mo, RBGModel rmo,int d)
	{
		this.model=mo;
		rankVector=new HashMap<String,Set<String>>();
		nodeRankVector=new HashMap<Node,Set<Node>>();
		mapClass=new HashMap<String, Double>();
		level=d;
		this.rbg=rmo;  
		
	}
	
	//this method to return the set of classes without rank along with their important value
	public HashMap<String, Double> getMapClass()
	{
		if(mapClass==null)
			rank();
		return mapClass;
	}
	
	//this method returns the set of nodes and its surrounding
	public HashMap<String,Set<String>> getrankVector()
	{
		if(rankVector==null)
			rank();
		return rankVector;
	}
	
	
	//this method returns the set of nodes and its surrounding in node set
		public HashMap<Node,Set<Node>> getNoderankVector()
		{
			if(nodeRankVector==null)
				rank();
			return nodeRankVector;
		}
	
	public String[] getRanked()
	{
		if(mapClass==null)
			rank();
		 HashMap<String, Double> sortedMap = sortByValue(mapClass);
		 Iterator<String> ss=sortedMap.keySet().iterator();
		 int i=sortedMap.size()-1;
		 SortedNameOnt=new String[mapClass.size()];
		 while(ss.hasNext())
		 {
			 String s=ss.next();
			 SortedNameOnt[i]=s;  i--; 
		 }
		 return SortedNameOnt;
	}
	
		
	private static <K, V extends Comparable<? super V>> HashMap<K, V>  sortByValue( Map<K, V> map )
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
	

	
	public void rank()
	{
		Set<String> contList;
		Set<Node>  nodeList;
		NodeList NList=rbg.getNamedClassNodes();
		//if(NList.size()==0)  {NList=rbg.getNodes(); System.out.println("this\t"+NList.size());}
		importantAll = new float [NList.size()];
		int countSub=0, countSup=0, countPro=0, ecount=0;
	    double size=rbg.getNamedClassNodes().size();  
	    Iterator<Node> it = null;
	    Iterator<Node> it2 = null;
		for(int i=0;i<NList.size();i++)
	    {
	    try {
			importantAll[i]=0;
	    	Node cls=NList.get(i); 
	    	it = (cls.getNamedSubs()!=null)? cls.getNamedSubs().iterator():null;
	    	 if(it!=null && it.hasNext()==true){
	    		 it2 = (cls.getNamedSupers()!=null)? cls.getNamedSupers().iterator():null;
	    		 if(it2!=null) 
	   	    	try{
	   	    	 NodeList it3p=cls.getPointedNodes();
	   	    	 countPro=it3p.size();
	   	    	 	}
	   	    	catch(NullPointerException e){}
	    	}
	    	contList=new HashSet<String>();
	    	nodeList=new HashSet<Node>();
	    	switch(level)
			{
			case 0: 
				while(it!=null && it.hasNext())
		    	{
		    		Node cl=it.next();
		    		if(NList.contains(cl)) 
		    			{countSub++;
		    		     contList.add(cl.getLocalName());
		    		     nodeList.add(cl);
		    		   }
		       	} 
		    	
		    	while(it2!=null && it2.hasNext())
		    	{
		    		Node cl=it2.next();
		    		if(NList.contains(cl)) {countSup++; contList.add(cl.getLocalName()); nodeList.add(cl);}
		       	}
		      	rankVector.put(cls.getLocalName(), contList);
		      	nodeRankVector.put(cls, nodeList);
				break;
				
			case 1:  // this is the case when sub-sub and sup-sup are considered
				while(it!=null && it.hasNext())
		    	{
		    		//OntClass cl=(OntClass)it.next();
		    		Node cl=it.next();
		    		if(NList.contains(cl)) countSub++;
		    		Iterator<Node> ccc=(cl.getNamedSubs()!=null)? cl.getNamedSubs().iterator():null;  
		    		while(ccc!=null &&ccc.hasNext())
		    		{
		    			Node cll= ccc.next();
		    			if(NList.contains(cll)) {countSub++; contList.add(cll.getLocalName()); nodeList.add(cll);}
		    		}
		    	}
		    	
		    	while(it2!=null && it2.hasNext())
		    	{
		    		//OntClass cl=(OntClass)it2.next();
		    		Node cl=it2.next();
		    		if(NList.contains(cl)) countSup++;
		    		Iterator<Node> ccc=(cl.getNamedSupers()!=null)? cl.getNamedSupers().iterator():null;  
		    		while(ccc!=null&&ccc.hasNext())
		    		{
		    			Node cll= ccc.next(); 
		    			if(NList.contains(cll)) {countSup++;contList.add(cll.getLocalName());nodeList.add(cll);}
		    		}
		    		
		    	}
		       	rankVector.put(cls.getLocalName(), contList);
		       	nodeRankVector.put(cls, nodeList);
				break;
				
			case 2:  //this is the case when up to level 3 can be considered.
				while(it!=null && it.hasNext())
		    	{
		    		Node cl=it.next();
		    		if(NList.contains(cl)) {countSub++; contList.add(cl.getLocalName()); nodeList.add(cl);}
		    		Iterator<Node> ccc=(cl.getNamedSubs()!=null)? cl.getNamedSubs().iterator():null;  
		    		while(ccc!=null && ccc.hasNext())
		    		{
		    			Node cll=ccc.next();
		    			if(NList.contains(cll)) {countSub++; contList.add(cll.getLocalName());nodeList.add(cll);}
		    			Iterator<Node> cccc=(cll.getNamedSubs()!=null)? cll.getNamedSubs().iterator():null;
		    			while(cccc!=null &&cccc.hasNext())
		    			{
		    				Node cl2=cccc.next();
		    				if(NList.contains(cl2)) {countSub++; contList.add(cl2.getLocalName());nodeList.add(cl2);}
		    			}
		    		}
		    	}
		    	
		    	while(it2!=null && it2.hasNext())
		    	{
		    		Node cl=it2.next();
		    		if(NList.contains(cl)) {countSup++; contList.add(cl.getLocalName());nodeList.add(cl);}
		    		Iterator<Node> ccc=(cl.getNamedSupers()!=null)? cl.getNamedSupers().iterator():null;  
		    		while(ccc!=null && ccc.hasNext())
		    		{
		    			Node cll=ccc.next() ;
		    			if(NList.contains(cll)) {countSup++; contList.add(cll.getLocalName());nodeList.add(cll);}
		    			Iterator<Node> cccc=(cll.getNamedSupers()!=null)? cll.getNamedSupers().iterator():null;// .listSuperClasses().toList();
		    			while(cccc!=null && cccc.hasNext())
		    			{
		    				Node cl2=cccc.next();
		    				if(NList.contains(cl2)) {countSup++; contList.add(cl2.getLocalName());nodeList.add(cl2);}
		    			}
		    		}
		    	}
		    	rankVector.put(cls.getLocalName(), contList);
		    	nodeRankVector.put(cls, nodeList);
				break;
			case 3:  //this is the case when up to level 3 can be considered.
				while(it!=null && it.hasNext())
		    	{
		    		Node cl=it.next();
		    		if(NList.contains(cl)) {countSub++; contList.add(cl.getLocalName());nodeList.add(cl);}
		    		Iterator<Node> ccc=(cl.getNamedSubs()!=null)? cl.getNamedSubs().iterator():null; 
		    		while(ccc!=null && ccc.hasNext())
		    		{
		    			Node cll=ccc.next();
		    			if(NList.contains(cll)) {countSub++; contList.add(cll.getLocalName());nodeList.add(cll);}
		    			Iterator<Node> cccc=(cll.getNamedSubs()!=null)? cll.getNamedSubs().iterator():null;
		    			while(cccc!=null &&cccc.hasNext())
		    			{
		    				Node cl2=cccc.next();
		    				if(NList.contains(cl2)) {countSub++; contList.add(cl2.getLocalName());nodeList.add(cl2);}
		    				Iterator<Node> ccccc=(cl.getNamedSubs()!=null)? cl.getNamedSubs().iterator():null;
		    				while(ccccc!=null && ccccc.hasNext())
		    				{
		    					Node cl3=ccccc.next();
			    				if(NList.contains(cl3)) {countSub++; contList.add(cl3.getLocalName());nodeList.add(cl3);}
		    				}
		    			}
		    		}
		    	}
		    	
		    	while(it2!=null && it2.hasNext())
		    	{
		    		//OntClass cl=(OntClass)it2.next();
		    		Node cl=it2.next();
		    		if(NList.contains(cl)) {countSup++; contList.add(cl.getLocalName());nodeList.add(cl);}
		    		Iterator<Node> ccc=(cl.getNamedSupers()!=null)? cl.getNamedSupers().iterator():null;  
			    		while(ccc!=null && ccc.hasNext())
			    		{
			    			Node cll=ccc.next() ;
			    			if(NList.contains(cll)) {countSup++; contList.add(cll.getLocalName());nodeList.add(cll);}
			    			Iterator<Node> cccc=(cll.getNamedSupers()!=null)? cll.getNamedSupers().iterator():null;// .listSuperClasses().toList();
			    			while(cccc!=null && cccc.hasNext())
			    			{
			    				Node cl2=cccc.next();
			    				if(NList.contains(cl2)) {countSup++; contList.add(cl2.getLocalName());nodeList.add(cl2);}
			    				Iterator<Node> ccccc=(cl2.getNamedSupers()!=null)? cl2.getNamedSupers().iterator():null;
			    				while(ccccc!=null && ccccc.hasNext())
			    				{
			    					Node cl3=ccccc.next();
				    				if(NList.contains(cl3)) {countSup++; contList.add(cl3.getLocalName());nodeList.add(cl3);}
			    				}
			    			}
			    		}
		    			
		     		}
		    	rankVector.put(cls.getLocalName(), contList);
		    	nodeRankVector.put(cls, nodeList);
				break;
			default:
				break;
			}
	    	
	    	if(countSub!=0)
	    	{
	    		importantAll [i] = (float) (0.3*countSub+0.3*countSup+0.3*countPro+0.1*ecount)/(float)size; 
	    	}
	    	mapClass.put(cls.toString(), (Double.valueOf(importantAll[i])));  //System.out.println(cls.getLocalName()+"\t"+Double.valueOf(importantAll[i]));
	    	countSub=0; countSup=0; countPro=0;
	    }
	    catch(ConversionException  ee){
	        System.out.println(ee.getMessage());
		}
	    catch( ConcurrentModificationException ee){
	        System.out.println(ee.getMessage());
		}
	}
		
		
   	}
	 public static void main(String args[])
	 {
	     
	     double start=System.currentTimeMillis();
	     String fp1 = "D:/test_ont/test/conference.owl"; 
	     ModelBuild MB=new ModelBuild(fp1);
	     MB.build();
	     OntModel model=MB.getModel();
	     RBGModel rbg=MB.getRBGModel();
	     rankConcept RC=new rankConcept(model,rbg, 3);
	     RC.rank();
	     HashMap<String,Double> rank=RC.mapClass;
	     HashMap<String, Set<String>> rc=RC.rankVector;
	     String[] rlist=RC.getRanked();
	     System.out.println("the rank size\t"+rank.size());
	     int i=0;
	     for (HashMap.Entry<String, Double> entry : rank.entrySet()) {
	    	    System.out.println("the ranking concepts\t"+entry.getKey()+" : "+entry.getValue()+ "\t"+rlist[i]); i++;
	    	}
	     
	     for (HashMap.Entry<String, Set<String>> entry : rc.entrySet()) {
	    	    System.out.println("the ranking concepts\t"+entry.getKey()+" : "+entry.getValue()); 
	    	}
	     double end=System.currentTimeMillis();
      System.out.println("The ranking time---->"+(end-start)*.001+"\t sec \t");
	 } 
	
	}
