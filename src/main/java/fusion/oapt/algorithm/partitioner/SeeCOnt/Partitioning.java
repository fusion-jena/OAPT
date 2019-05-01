package fusion.oapt.algorithm.partitioner.SeeCOnt;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntProperty;

import fusion.oapt.algorithm.matcher.string.ISub;
import fusion.oapt.algorithm.partitioner.SeeCOnt.Findk.FindOptimalCluster;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.general.cc.ModelBuild;
import fusion.oapt.general.gui.SelectCHWindows;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;
import fusion.oapt.model.RBGModel;

public class Partitioning {
	
	public static LinkedHashMap<Integer, Cluster> clusters; 
	private OntModel model;
	public  NodeList entitiesCH;
	private NodeList unassignedNodes;
	private static  int NumCH;
	private boolean CheckAdd [] = null;
	private LinkedHashMap<String, Integer> uriToClusterID = null;
    public List opList=null;
	public List classList=null;
	private int numEntity;
	public static int [] sizeCluster;
	HashMap<Node,Set<Node>> nodeRankVector;
	HashMap<String,Set<String>> rankVector;
	ModelBuild MB;
	RBGModel rbgm;
	
	
	public Partitioning(String ont)
	{
		MB=new ModelBuild(ont);
		MB.build();
		this.model=MB.getModel();
		this.rbgm=MB.getRBGModel();
		NumCH=0;
		clusters = new LinkedHashMap<Integer, Cluster>();
		numEntity=rbgm.getNamedClassNodes().size();
		unassignedNodes=rbgm.getNamedClassNodes();
		entitiesCH=new NodeList();;
		nodeRankVector=new HashMap<Node, Set<Node>>();
		rankVector=new HashMap<String, Set<String>>();
		classList=model.listNamedClasses().toList();
	}
	
	public Partitioning(String ont, int K)
	{
		MB=new ModelBuild(ont);
		MB.build();
		this.model=MB.getModel();
		this.rbgm=MB.getRBGModel();
		NumCH=K;
		clusters = new LinkedHashMap<Integer, Cluster>(NumCH);
		numEntity=rbgm.getNamedClassNodes().size();
		unassignedNodes=rbgm.getNamedClassNodes();
		entitiesCH=new NodeList();
		nodeRankVector=null;//new HashMap<Node, Set<Node>>();
		rankVector= null; //new HashMap<String, Set<String>>();
		classList=model.listNamedClasses().toList();
	}
	
	public Partitioning(ModelBuild model, NodeList nodeH,  HashMap<Node, Set<Node>> vector)
	{
		this.MB=model;
		NumCH=nodeH.size();
		entitiesCH=nodeH;
		this.model=MB.getModel();
		this.rbgm=MB.getRBGModel();
		clusters = new LinkedHashMap<Integer, Cluster>(NumCH);
		numEntity=rbgm.getNamedClassNodes().size();
		unassignedNodes=rbgm.getNamedClassNodes();
		this.nodeRankVector=vector;
		classList=this.model.listNamedClasses().toList();
		rankVector=new HashMap<String, Set<String>>();
	}
	
	public Partitioning (ModelBuild Mod)
	{
		this.MB=Mod;
		NumCH=0;
		entitiesCH=new NodeList();
		this.model=MB.getModel();
		this.rbgm=MB.getRBGModel();
		clusters = new LinkedHashMap<Integer, Cluster>();
		numEntity=rbgm.getNamedClassNodes().size();
		unassignedNodes=rbgm.getNamedClassNodes();
		classList=model.listNamedClasses().toList();
		rankVector=new HashMap<String, Set<String>>();
	}
	
	public Partitioning (ModelBuild Mod, int k)
	{
		this.MB=Mod;
		NumCH=k;
		entitiesCH=new NodeList(NumCH);
		this.model=MB.getModel();
		this.rbgm=MB.getRBGModel();
		clusters = new LinkedHashMap<Integer, Cluster>();
		numEntity=rbgm.getNamedClassNodes().size();
		unassignedNodes=rbgm.getNamedClassNodes();
		classList=model.listNamedClasses().toList();
		rankVector=new HashMap<String, Set<String>>();
	}
	
	private void rank()
	{
		if(MB==null)
			System.out.print("You must input an ontology and build the model first ");
		String[] list=null;
		int size=rbgm.getNamedClassNodes().size();
		 int size1=model.listNamedClasses().toList().size();
		 size = (size1 > size) ? size : size1;
		if(nodeRankVector==null)
		{
			int d=0;
			if(size<1000)
				 d=3;
			 else if(size>1000 && size<20000) d=2;
			 else if(size>20000 && size<50000) d=1;
			 else
			    d=0; 
			rankConcept RC=new rankConcept(model,rbgm, d);
		    RC.rank();
		    nodeRankVector=RC.getNoderankVector();
		    list=RC.getRanked();
		}
		if(entitiesCH.size()<=0)
		{
			entitiesCH=new NodeList();
			for(int i=0;i<NumCH;i++)
			{
				String name=list[i];
				OntClass cls=model.getOntClass(name);  
				Node nn= rbgm.getNode(cls);
				//System.out.print("\n"+cls.getLocalName()+"\t The ontology concepts are ranked:\t "+nn.getLocalName());
				entitiesCH.add(nn);
			}
		}
	}
	
	public void selctClusterHead()
	{
		FindOptimalCluster OpC=new FindOptimalCluster(MB);
		NumCH=OpC.FindOptimalClusterFunc();
		entitiesCH=OpC.getNodeCH();
		rankVector=OpC.getRankVector();
		nodeRankVector=OpC.getNodeRankVector();
	}
	
	// to be used for the interactive cluster head selection
	public  void AddCH(){
		entitiesCH= new NodeList();
		NumCH = 0;
		for (int i=0; i<SelectCHWindows.selectedValuesList.size();i++){
			int idx =MB.findIndexName(SelectCHWindows.selectedValuesList.get(i)); 
			if (idx != -1){ //if it is -1, it means not find or it is null
				entitiesCH.add(MB.entities.get(idx));
				NumCH = NumCH +1;
			} 
		}	

		Coordinator.KNumCH = NumCH;
		Coordinator.CH = entitiesCH;
	}
	
	public void partition()
	{
		
		if(NumCH!=0) rank();
		else
			selctClusterHead();
		createIniCluster_Phase();
		direct_Partition_Phase();
		indirect_Partition_Phase();
		Coordinator.clusters = clusters;
	}
	public void partition(int k)
	{
		NumCH=k; 
		if(k==1) onePartition();
		else{
		rank();
		selctClusterHead();
		createIniCluster_Phase();
		direct_Partition_Phase();
		indirect_Partition_Phase();}
		Coordinator.clusters = clusters;
	}
	
	private void onePartition()
	{
		 Cluster cluster = new Cluster(0); 
		 NodeList nodes=rbgm.getNamedClassNodes();
		 cluster.setCH(nodes.get(0));
		 for(int i=0;i<nodes.size();i++)
		 {
			 Node nn=nodes.get(i);
			 cluster.putElement(nn.toString(), nn);
		 }
		 clusters.put(0, cluster);
	}
	
	public LinkedHashMap<Integer, Cluster> getPartitions()
	{
		if(clusters==null)
			partition();
		return clusters;
	}
	public void createIniCluster_Phase(){

		 clusters = new LinkedHashMap<Integer, Cluster>(entitiesCH.size());
		 
		 uriToClusterID = new LinkedHashMap<String, Integer>();
		 CheckAdd = new boolean [numEntity+1]; 
		 
		 for (int cid = 0, n = entitiesCH.size(); cid < n; cid++) {
		     if (cid <= entitiesCH.size()){
			     Node entity = entitiesCH.get(cid);
			     Cluster cluster = new Cluster(cid); cluster.setCH(entity);
			     cluster.putElement(entity.toString(), entity);
			     clusters.put(cid, cluster); 
			     cluster.setCH(entity);   
			     uriToClusterID.put(entity.toString(), cid);
			     int index=MB.findIndexName(entity.getLocalName());
			     if(index < numEntity && index>=0) 
			    	 CheckAdd[index] = true;
			    }
		     }
		}
	
	public void direct_Partition_Phase()
	{
		uriToClusterID = new LinkedHashMap<String, Integer>();
		Set<Node> surr=new HashSet<Node>();
		for (int i=0; i<NumCH; i++)
		{
			if ( i < entitiesCH.size())
			{
				Node ch=entitiesCH.get(i);  unassignedNodes.remove(ch);
			    Cluster c= clusters.get(i);
			    surr=getSurrounding(ch);
			    if(surr!=null){
			    for (Node s : surr) 
				{ 
					int index = MB.findIndexName(s.getLocalName());
					if (index>-1 &&index<numEntity) {
						if (CheckAdd[index] == false)
						{
				  			c.putElement(s.toString(),s );   unassignedNodes.remove(s);
							CheckAdd[index] = true;					
						}
					}
				}
			}
			}
		}
	}
	
	private Set<Node> getSurrounding(Node ch)
	{
		Set<Node> str=new HashSet<Node>();
		str= nodeRankVector.get(ch);
		return str;
	}
	
	//finnally partition unassigned nodes
	public void indirect_Partition_Phase()
	{
	    int size=unassignedNodes.size();
		int opCount=model.listObjectProperties().toList().size();   
	    int entCount=numEntity;
		float MemebershipsConcepts [] = null;
		HashMap<Integer,Double> memShipList=null;
		if(size>0){
		for (int i=0; i<size; i++)
		{
			Node iNode = unassignedNodes.get(i);
			int indxiNode= MB.findIndexName(iNode.getLocalName());	
			if (indxiNode > -1 && indxiNode <numEntity){ 
				if (CheckAdd[indxiNode] == false)
				{
					MemebershipsConcepts  = new float [NumCH];
					memShipList=new HashMap<Integer,Double>();
					for (int m=0; m<NumCH; m++)
					{
						Node CHnode = entitiesCH.get(m);
						if(opCount>0.1*entCount)
						{
							MemebershipsConcepts[m] = computMemFun_sem(iNode, CHnode); 
						    memShipList.put((Integer)m, (double) MemebershipsConcepts[m]);
						}
						else
						{
							MemebershipsConcepts[m] = computMemFun_str(iNode, CHnode);
							memShipList.put(m, (double) MemebershipsConcepts[m]);
						}
						
					}
					// memShipList=solveDuplicate(memShipList,iNode);
					MemebershipsConcepts = SolveEqualValue(MemebershipsConcepts, NumCH, entitiesCH , iNode);
					int indexCH=FindIndexCH(memShipList);
					CheckAdd [indxiNode] = true;		 
					Cluster x= clusters.get(indexCH);   
					x.putElement(iNode.toString(),iNode ); 
				}
			}
		 }}
		 sizeCluster = new int [NumCH];
		 for (int i=0; i<NumCH; i++)
		 {
		     if(clusters.get(i)!=null)
			 sizeCluster [i] = clusters.get(i).getElements().size();
		      //System.out.println("------- the cluster head\t"+clusters.get(i).getCH().getLocalName());
		 }
		 //System.out.println("The indirect spread phase is done:\n starting creating modules ======");
	}
	
   ///////////////////////////////////// compute the similarity between a node and a cluster head node exploiting
  ///////////////////////////////////   the semantic relationships between concepts

  public float computMemFun_sem(Node NodeConcept, Node NodeCH )
  
  { 
    float semSim = 0, strSim=0, sem=0;  int count=0;
    strSim = computePathNameSim(NodeConcept, NodeCH);
    semSim=computeStrSim(NodeConcept, NodeCH);
    String namespace = model.getNsPrefixURI("");
    OntClass chClass=model.getOntClass(namespace+NodeCH.getLocalName());
    OntClass iClass=model.getOntClass(namespace+NodeConcept.getLocalName());
    if(classList.contains(chClass) && classList.contains(iClass)){
    List<OntProperty> chList=chClass.listDeclaredProperties(true).toList();   
   if(chList!=null)
     {
      for(int i=0;i<chList.size();i++)
      {
        OntProperty onP=(OntProperty)chList.get(i);  
        List dClasses=onP.listDomain().toList();     
        List dcl=onP.listRange().toList();
        if(dClasses.contains(iClass)||dcl.contains(iClass)) count++;
     }
    sem=count/(float)(chList.size()) ;
  }
  }
    // Path path=OntTools.findShortestPath(model1, chClass,iClass,Filter.any);
   int size=0; 
   // if(path!=null) size=path.size();
   float memShip=0;
   if(size==0)
      memShip= (float) (0.4*semSim+0.2*sem+ 0.4*strSim);
    else
     memShip= (float) (0.3*semSim+0.2*sem+ 0.2*strSim+.03/size);
     return memShip;
  }

  public float computMemFun_str(Node NodeConcept, Node NodeCH ){
	   
	   
		 float semSim = 0, strSim=0;
		 strSim = computePathNameSim(NodeConcept, NodeCH);
		 semSim=computeStrSim(NodeConcept, NodeCH);
		 int size=0; 
		 //if(path!=null) size=path.size();
		 float memShip=0;
		 if(size==0)
		  memShip= (float) (0.6*semSim+0.4*strSim);
		 else
			 memShip= (float) (0.6*semSim+0.4*strSim+0.3/size);
	      
		 return memShip;
	}
    
   /////////////// computing the string similarity between the name path of a node and a cluster head node
   public float computePathNameSim(Node NodeConcept, Node NodeCH)  
   {
     String StrConcept = getString(NodeConcept);  
     String StrCH = getString(NodeCH);
     float sim=(float) ISub.getSimilarity(StrConcept, StrCH);
     //float sim=(float) LevenshteinDistance.getDistance(StrConcept, StrCH);
    return sim;
   }
   
   private String getString(Node node)
   {
   	Set<Node> list;
   
   	 list=nodeRankVector.get(node);
   	//list.iterator().toString();
   	String name=node.getLocalName();
   	if(list!=null)
   		name=name.concat(list.iterator().toString());
   	return name;
   }

   ////// computing the semantic similarity between a node and cluster head
   public float computeStrSim (Node iclass , Node jclass){
   	 
   	float shareNeighbor = 0;
   	Set<Node> list1,list2;
   	if(iclass.getLocalName().length()!=0)
   		list1=nodeRankVector.get(iclass);
   	else
   		list1=nodeRankVector.get(iclass);
       if(jclass.getLocalName().length()!=0)
       	list2=nodeRankVector.get(jclass);
       else
       	list2=nodeRankVector.get(jclass);
   	List<Node> list3=new ArrayList<Node>();
   	if(list1!=null && list2!=null)
   	{
   		list3.retainAll(list1);
   		if(list1.size()+list2.size()>0) shareNeighbor=list3.size()/(list1.size()+list2.size());
   	}
   	
    return shareNeighbor;
   }
   
   public  float [] SolveEqualValue(float iArray[], int NumCH ,  NodeList entitiesCH, Node iNode)
   {
      // if we have equal value we should consider another measure to know which one is better
 	  int size=entitiesCH.size();
      for (int i=0; i<size-1; i++){
         for (int j=i+1; j<size; j++){
              if (iArray[i] == iArray[j]){
                iArray[j] = iArray[j] + (float) SecCompare(entitiesCH.get(j) , iNode);
                iArray[i] = iArray[i] + (float) SecCompare(entitiesCH.get(i) , iNode);
           }}
        }
     return iArray;	
  }


 
 public double SecCompare (Node CHnode, Node iNode){
		double c =0;
		double ich= chartoint(CHnode.getLocalName()); double in= chartoint (iNode.getLocalName());
		c = (double) ((ich + in )+ (CHnode.getHierarchy() + iNode.getHierarchy()))/ 100000;
		return c;
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public double chartoint(String iname ){
		double r=0;
		iname = iname.toLowerCase(); 
		for (int i=0; i<iname.length(); i++){
			char xx= iname.charAt(i);
			r= r+ (int) xx;
		}
		r= r/iname.length();
		return r;
	}
	
   private int FindIndexCH(HashMap<Integer,Double> mem)
   {
 	 int index=0;
 	 HashMap<Integer, Double> sortedMap = // sortByValue(mem);
 		    mem.entrySet().stream()
 		    .sorted(Entry.comparingByValue())
 		    .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
 		                              (e1, e2) -> e1, LinkedHashMap::new));
 	 	
 	Set<Integer> it= sortedMap.keySet();
 	List<Integer> newIT = new ArrayList<Integer>(it);
 	for(int i=newIT.size();i>0;i--)
 	 {
 		 index=(Integer) newIT.get(i-1); 
 		 Cluster x=clusters.get(index);
 		 if(x.getSize()<1.4*numEntity/NumCH) break; 
 	 }
 	 return index;
  }
   
  
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
  
  public int FindIndexOfMax (float []XArray, int NumCH){
     double max = 0;
     int IndexOfMax = 0;
     for (int i= 0; i<NumCH; i++)
         if (XArray[i]>max) {
             max=XArray[i];
             Cluster x= clusters.get(IndexOfMax);
             if(numEntity>500)
             {
             	if(x.getSize()<numEntity/NumCH)
                     IndexOfMax = i;
             }
             else  
             IndexOfMax = i;
         }
     return IndexOfMax;
 }
  
  public static void main(String args[]) throws IOException
	 {
	     
	     double start=System.currentTimeMillis();
	     //String fp1 = "D:/alsayed_SVN/AquaDiva/rdf/study.owl"; 
	     String fp2 = "D:/owl/owl/bco.owl.gz";
	     ModelBuild model=  new ModelBuild(fp2);
	     model.build();
	     Partitioning P=new Partitioning(model,4);
	     P.partition();
	     LinkedHashMap<Integer, Cluster> clusters=P.getPartitions();
	     String content = "Ontology\t No. class \t No. rank \t No. LB \t No. UB \t time \n ";
	     File file = new File("src/resources/results/partition.txt");
	     File pfile=file.getParentFile();
	     if(!pfile.exists())
	     {
	   	   pfile.mkdir();
	     }
	     FileWriter fw = new FileWriter(file.getAbsoluteFile());
	     System.out.println(fw.toString());
	     BufferedWriter bw = new BufferedWriter(fw);
	     bw.write(content);
	     int i=0;   
	     for (Entry<Integer, Cluster> entry : clusters.entrySet()) {
	    	 Cluster x=entry.getValue();
	    	 content="\n \n cluster No. \t"+i +"\n"; i++;
	    	 bw.write(content);
	    	 x.printCluster();
	    	    System.out.println("the ranking concepts\t"+entry.getKey()+" : "+entry.getValue()+" : "+x.getSize()); 
	    	 content=x.getlistNameLabel().toString();
	    	 bw.write(content);
	    	}
	   bw.close();
	   double end=System.currentTimeMillis();
       System.out.println("The ranking time---->"+(end-start)*.001+"\t sec \t");
       CreateModule CM=new CreateModule(P.MB,P.clusters);
      CM.createOWLFiles_Phase();
      // CM.createModules_E();
      // CM.createOntModelFiles();
      
	 } 

}
