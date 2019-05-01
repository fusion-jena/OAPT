
package fusion.oapt.algorithm.partitioner.axiomClustering;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Iterator;
import java.util.LinkedHashMap;


import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.cc.Coordinator;
import fusion.oapt.model.ext.sentence.RDFSentence;
import fusion.oapt.model.ext.sentence.RDFSentenceGraph;
import fusion.oapt.model.ext.sentence.filter.OntologyHeaderFilter;
import fusion.oapt.model.ext.sentence.filter.PureSchemaFilter;
import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;
import fusion.oapt.model.RBGModel;


import org.apache.jena.ontology.OntModel;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.Statement;

import java.io.BufferedReader;
import java.io.FileReader;




public class AxiomClustering {
	public static LinkedHashMap<Integer, AxiomCluster> clusters = null;
	public static LinkedHashMap<Integer, AxiomCluster> finalCluster = null;
	
	private static RBGModel jmodelRbg = null;	
	private static String ontName = null;
	private int maxSize = Integer.MAX_VALUE;
	public static String tempDir = null;
	public static ArrayList<OntModel> models = null ;
	
	public static ArrayList<ClassStructure> Class_info = null;
	public static ArrayList<PropertyStructure> Property_info = null;
	private LinkedHashMap<String, Integer> Class_index ;
	private LinkedHashMap<String, Integer> Property_index;
	public static  int [][] NumLickConcept=null;
	public static int [] columnAllMax;
	public static int [] rowAllMax;
	
	
 public  AxiomClustering ( RBGModel jmodel, String name, int ms, String td){
	 this.jmodelRbg = jmodel;
	 this.ontName = name;
     maxSize = ms; 
     this.tempDir = td;
	}

 public NodeList getEntities()
 {
     return BuildModel.entities;
 }


 public LinkedHashMap<Integer, AxiomCluster> getClusters() 
 {
   return clusters;
 }

 public ArrayList<OntModel> getOntModels()
 {
     return models;
 }
	
public void  StepsCClustering (){

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	createStructure_Phase();										       /////////////////////////////////////////////////////////////////////////
	initialClustering_Phase();
	mergCluster_Phase();
	CreateOutput_Phase();										       /////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
}


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////createStructure_Phase /////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private void createStructure_Phase(){

	//Create class info
	NodeList modelcalsslist= BuildModel.rbgmModel.getClassNodes();
	Class_info = new ArrayList <ClassStructure> (modelcalsslist.size());
	Class_index = new LinkedHashMap<String, Integer>();
	Property_index = new LinkedHashMap<String, Integer>();
	int index=0;
	for (int i=0; i< modelcalsslist.size(); i++){
		if (modelcalsslist.get(i).getLocalName() != null){ // it is not blank node
			ClassStructure e = new ClassStructure();
			e.setClassName(modelcalsslist.get(i));
			e.setSubClassList(modelcalsslist.get(i).getNamedSubs());
			e.setSuperClass_list(modelcalsslist.get(i).getNamedSupers());
			//e add disjointclass;
			//e add equvalentclass;
			Class_info.add(e);
			Class_index.put(modelcalsslist.get(i).toString(),index);
			index++;
		}
	}
	
	//create property info
	NodeList modelPropertylist= BuildModel.rbgmModel.getPropertyNodes();
	Property_info = new ArrayList <PropertyStructure> (modelPropertylist.size());
	index=0;
	for (int i=0; i< modelPropertylist.size(); i++){
		PropertyStructure e = new PropertyStructure();
		e.setPropertyName(modelPropertylist.get(i));		
		Property_info.add(e);
		Property_index.put(modelPropertylist.get(i).toString(),index);
		index++;
	}
	//Processing all statements to add data in Property_info
	for (Iterator<Node> i = BuildModel.rbgmModel.listStmtNodes(); i.hasNext();) {
		Node sentence = i.next();
		Node iSubject= sentence.getSubject();
		Node iObject = sentence.getObject();
		Node iPredicate = sentence.getPredicate();
		NodeList iPredicate2 = sentence.getPointedNodes();
		int ix=0;
		switch (iPredicate.getLocalName()){
		case ("domain"):
			ix= Property_index.get(iSubject.toString());
			Property_info.get(ix).setDomainName(iObject);
			break;
		
		case ("range"):
			ix= Property_index.get(iSubject.toString());
			Property_info.get(ix).setRangeName(iObject);
			break;
		
		case ("inverseOf"):
			//ix= Property_index.get(iSubject.toString);
			//Property_info.get(ix).setinverseOf_list(iObject);
			break;
		
		} //end switch case
		
	}
	
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public void initialClustering_Phase(){
	
	clusters = new LinkedHashMap<Integer, AxiomCluster>(BuildModel.NumEntity+1);
	int Clusterid = 0; Integer idx=0; 
		
	for (int ix=0; ix <Class_info.size(); ix++){
		AxiomCluster tempcluster = new AxiomCluster(Clusterid);
		if (Class_info.get(ix).getTag() == false){ //add this class and all related info
			tempcluster.putElement(Class_info.get(ix).getName().toString(), Class_info.get(ix).getName());
			Class_info.get(ix).setTagCluster(true);
			Class_info.get(ix).setClusterId(Clusterid);
			
			//add subclass
			if (Class_info.get(ix).getSubClassList() != null){
				for (int is=0; is<Class_info.get(ix).getSubClassList().size(); is++){
					idx= Class_index.get(Class_info.get(ix).getSubClassList().get(is).toString());
					if (idx != null) {
						if (Class_info.get(idx).getTag() == false){
							tempcluster.putElement(Class_info.get(ix).getSubClassList().get(is).toString(), Class_info.get(ix).getSubClassList().get(is));
							Class_info.get(idx).setTagCluster(true);
							Class_info.get(idx).setClusterId(Clusterid);
							//add properties of subclass
							NodeList ipp= getProperty_iclass(Class_info.get(ix).getSubClassList().get(is));
							for (int p=0; p<ipp.size(); p++){
								idx = Property_index.get(ipp.get(p).toString());
								if (Property_info.get(idx).getTag() == false){
									tempcluster.putElement(Property_info.get(idx).getName().toString(),Property_info.get(idx).getName());
									Property_info.get(idx).setTagCluster(true);
									Property_info.get(idx).setClusterId(Clusterid);
								}
							}
						}//end if gettag=false
					}
				}
			}
			
			//add superclass
			if (Class_info.get(ix).getSuperClassList() != null) {
				for (int is=0; is<Class_info.get(ix).getSuperClassList().size(); is++){
					idx= Class_index.get(Class_info.get(ix).getSuperClassList().get(is).toString());
					if (idx != null){
						if (Class_info.get(idx).getTag() == false) {
							tempcluster.putElement(Class_info.get(ix).getSuperClassList().get(is).toString(), Class_info.get(ix).getSuperClassList().get(is));
							Class_info.get(idx).setTagCluster(true);
							Class_info.get(idx).setClusterId(Clusterid);
							//add properties of superclass
							NodeList ipp= getProperty_iclass(Class_info.get(ix).getSuperClassList().get(is));
							for (int p=0; p<ipp.size(); p++){
								idx = Property_index.get(ipp.get(p).toString());
								if (Property_info.get(idx).getTag() == false){
									tempcluster.putElement(Property_info.get(idx).getName().toString(),Property_info.get(idx).getName());
									Property_info.get(idx).setTagCluster(true);
									Property_info.get(idx).setClusterId(Clusterid);
								}
							}
						}
					}
				}
			}
			
			
			//add related properties
			for (int k=0; k<Property_info.size(); k++){
				if (Class_info.get(ix).getName() == Property_info.get(k).getDomainName()){ // add its property, domain, range to list
					if (Property_info.get(k).getTag() == false) {
						tempcluster.putElement(Property_info.get(k).getName().toString(), Property_info.get(k).getName()); //add its property
						Property_info.get(k).setTagCluster(true);
						Property_info.get(k).setClusterId(Clusterid);
					}
					
					idx= Class_index.get(Property_info.get(k).getDomainName().toString());
					if (idx != null) {
						if (Class_info.get(idx).getTag() == false) {
							tempcluster.putElement(Property_info.get(k).getDomainName().toString(), Property_info.get(k).getDomainName()); // add its domain name class
							Class_info.get(idx).setTagCluster(true);
							Class_info.get(idx).setClusterId(Clusterid);
							//Since we add one class we should add its properties
							NodeList ipp= getProperty_iclass(Property_info.get(k).getDomainName());
							for (int p=0; p<ipp.size(); p++){
								idx = Property_index.get(ipp.get(p).toString());
								if (Property_info.get(idx).getTag() == false){
									tempcluster.putElement(Property_info.get(idx).getName().toString(),Property_info.get(idx).getName());
									Property_info.get(idx).setTagCluster(true);
									Property_info.get(idx).setClusterId(Clusterid);
								}
							}
			
						}
					}
					
					if (Property_info.get(k).getRangeName() != null){
						idx= Class_index.get(Property_info.get(k).getRangeName().toString());
						if (idx != null) {
							if (Class_info.get(idx).getTag() == false) {
								tempcluster.putElement(Property_info.get(k).getRangeName().toString(), Property_info.get(k).getRangeName()); // add its range name class
								Class_info.get(idx).setTagCluster(true);
								Class_info.get(idx).setClusterId(Clusterid);
								//Since we add one class we should add its properties
								NodeList ipp= getProperty_iclass(Property_info.get(k).getRangeName());
								for (int p=0; p<ipp.size(); p++){
									idx = Property_index.get(ipp.get(p).toString());
									if (Property_info.get(idx).getTag() == false){
										tempcluster.putElement(Property_info.get(idx).getName().toString(),Property_info.get(idx).getName());
										Property_info.get(idx).setTagCluster(true);
										Property_info.get(idx).setClusterId(Clusterid);
									}
								}
							}
						}//end if idx
					}
				}
				
				if (Class_info.get(ix).getName() == Property_info.get(k).getRangeName()){ // add its property, domain, range to list
					if (Property_info.get(k).getTag() == false) {
						tempcluster.putElement(Property_info.get(k).getName().toString(), Property_info.get(k).getName());  //add its property
						Property_info.get(k).setTagCluster(true);
						Property_info.get(k).setClusterId(Clusterid);
					}
					
					if (Property_info.get(k).getDomainName() != null){
						idx= Class_index.get(Property_info.get(k).getDomainName().toString());
						if (idx != null) {
							if (Class_info.get(idx).getTag() == false){
								tempcluster.putElement(Property_info.get(k).getDomainName().toString(), Property_info.get(k).getDomainName()); //add its domain name class
								Class_info.get(idx).setTagCluster(true);
								Class_info.get(idx).setClusterId(Clusterid);
								//Since we add one class we should add its properties
								NodeList ipp= getProperty_iclass(Property_info.get(k).getDomainName());
								for (int p=0; p<ipp.size(); p++){
									idx = Property_index.get(ipp.get(p).toString());
									if (Property_info.get(idx).getTag() == false){
										tempcluster.putElement(Property_info.get(idx).getName().toString(),Property_info.get(idx).getName());
										Property_info.get(idx).setTagCluster(true);
										Property_info.get(idx).setClusterId(Clusterid);
									}
								}
							}
						}//end if idx
					}
					
					idx= Class_index.get(Property_info.get(k).getRangeName().toString());
					if (idx != null) {
						if (Class_info.get(idx).getTag() == false) {
							tempcluster.putElement(Property_info.get(k).getRangeName().toString(), Property_info.get(k).getRangeName());  // add its range name class
							Class_info.get(idx).setTagCluster(true);
							Class_info.get(idx).setClusterId(Clusterid);
							//Since we add one class we should add its properties
							NodeList ipp= getProperty_iclass(Property_info.get(k).getDomainName());
							for (int p=0; p<ipp.size(); p++){
								idx = Property_index.get(ipp.get(p).toString());
								if (Property_info.get(idx).getTag() == false){
									tempcluster.putElement(Property_info.get(idx).getName().toString(),Property_info.get(idx).getName());
									Property_info.get(idx).setTagCluster(true);
									Property_info.get(idx).setClusterId(Clusterid);
								}
							}
						}
					}
				}
			}//end for
			
			clusters.put(Clusterid, tempcluster);	
			Clusterid++;
			
			//add added-subproperty or not
		}

	} //end while
	
	Coordinator.KNumCH = Clusterid;
	/*
	// Print the Clusters with their elements
	int indx=-1;
	int [] sizc = new int [Clusterid];
	System.out.println(" Printing Generated Clusters with their elements");
	for (Iterator<AxiomCluster> i = clusters.values().iterator(); i.hasNext();) {
	    indx++; 
	    System.out.println('\n'+ " *** Cluster "+ indx+ " ***");
		AxiomCluster cluster = i.next();
	    int NumElement = 0;
		for (Iterator<Node> iter = cluster.listElements(); iter.hasNext();) {
	        NumElement ++;
	        //Node ccc= iter.next(); 
			System.out.println(" --- " +NumElement + ": " + iter.next().getLocalName());
		}
		sizc[indx] = NumElement;
	}
	
	System.out.println(" Size: ");
	for (int ii=0; ii<Coordinator.KNumCH; ii++){
		System.out.print("   " + sizc[ii]);
	}
	System.out.println("------------------------------------");
*/	

}	

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static void mergCluster_Phase(){
	
	
	
	//do bellow work as a iterative process till ..? //now i only say  do it 3times
	int condition = 0;
	while (condition < 5){
		finalCluster = new LinkedHashMap<Integer, AxiomCluster>(Coordinator.KNumCH);
		Integer ixc=0;
		
		// find the similarity values between clusters
		Iterator<AxiomCluster> iCL= clusters.values().iterator();
		int simArray [][] = new int [clusters.size()+1] [clusters.size()+1];
		int i=-1;
		
		while (iCL.hasNext()){
			i++;
			AxiomCluster icluster = iCL.next();
			Iterator<AxiomCluster> jCL= clusters.values().iterator();
			int j = -1;
			while (jCL.hasNext()){
				j++;
				if (i == j ){
					simArray[i][j] = -1; //it means the cluster compare with itself, which it has the maximum similarity, so we ignore it since we need the max similarity between two separated cluster
				}else{
					AxiomCluster jcluster = jCL.next();
					int sim= findShareKnowledge(icluster,jcluster);
					simArray[i][j] = sim;	
				}
				
			}
		}
		
		ArrayList<String> pairSim = findCandidate(simArray);
		
		//find threshold
		int threshold= median(columnAllMax); //To Do: decide we should determine threshold according to rowAllMax or columnallMax
		
		//merge
		
		boolean []tag_merge = new boolean [clusters.size()+1];
		for (int q=columnAllMax.length-1; q>0; q--){
			int imax= columnAllMax[q];
			for (int t=0; t<pairSim.size(); t++){
				String ps = pairSim.get(t);
				String []psArray = ps.split("\\&");
				if (Integer.parseInt(psArray[2]) >= threshold && Integer.parseInt(psArray[2]) == imax && tag_merge[Integer.parseInt(psArray[0])] == false && tag_merge[Integer.parseInt(psArray[1])] == false){
					// merge them
					AxiomCluster templist = new AxiomCluster (ixc); 
					templist = mergeCopy(clusters.get(Integer.parseInt(psArray[0])), clusters.get(Integer.parseInt(psArray[1])), ixc); 
					tag_merge[Integer.parseInt(psArray[0])] = true;  tag_merge[Integer.parseInt(psArray[1])] = true;
					templist.setClusterID(ixc);
					finalCluster.put(ixc, templist);
					ixc++;
				}
	
				}
			
		}
			
		// add those cluster that do not merge with any in our new list
		for (int e=0; e<clusters.size(); e++){
			if (tag_merge[e] == false){
				clusters.get(e).setClusterID(ixc);
				finalCluster.put(ixc, clusters.get(e));
				ixc++;
			}
		}
		Coordinator.KNumCH = ixc;
		//copy finalcluster to clusters till it can be done as a iterative process
		clusters.clear();
		clusters = new LinkedHashMap<Integer, AxiomCluster>(Coordinator.KNumCH);
		clusters.putAll(finalCluster);
		condition++;
	} //end while condition <3
	
	
	int tttt=0;
	
	

}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
public static ArrayList<OntModel> CreateOutput_Phase(){
	
	 NumLickConcept = new int [Coordinator.KNumCH][BuildModel.NumEntity+1]; // This array store the number of link for each element in each cluster
	  
	 ArrayList<AxiomCluster> list = new ArrayList<AxiomCluster>();
	 for (Iterator<AxiomCluster> i = finalCluster.values().iterator(); i.hasNext();) {
	     list.add(i.next());
	 }
	 LinkedHashMap<String, Integer> uriToClusterID = new LinkedHashMap<String, Integer>();
	 for (int i = 0, n = list.size(); i < n; i++) {
	     AxiomCluster cluster = list.get(i);
	     int clusterID = cluster.getClusterID();
	     for (Iterator<Node> iter = cluster.listElements(); iter.hasNext();) {
	         Node inode= iter.next();
	    	 String uri = inode.toString(); 
	         uriToClusterID.put(uri, clusterID);
	     }
	 }
	 
	 RDFSentenceGraph sg = new RDFSentenceGraph(jmodelRbg.getOntModel());
	 sg.build(); //build with list statement of original ontology model
	 ArrayList <String> iii= sg.getOntologyURIs();
	 sg.filter(new OntologyHeaderFilter(sg.getOntologyURIs()));
	 sg.filter(new PureSchemaFilter());
	 //Creating one model for each partition to store them as separated files
	 models = new ArrayList<OntModel>(list.size()); // create models (Array list with OntModel type with number of partition (NumCH))
	 LinkedHashMap<Integer, Integer> clusterIDToOntModelID = new LinkedHashMap<Integer, Integer>();
	 for (int i = 0, n = list.size(); i < n; i++) {
	     models.add(ModelFactory.createOntologyModel()); //Create as RDF format
	     int cid = list.get(i).getClusterID();
	     clusterIDToOntModelID.put(cid, i);
	 }
	 for (int i = 0, n = sg.getRDFSentences().size(); i < n; i++) {
	     RDFSentence sentence = sg.getRDFSentence(i);     
	     ArrayList<String> uris = sentence.getSubjectDomainVocabularyURIs(); 
	     LinkedHashMap<Integer, Object> uniqueURIs = new LinkedHashMap<Integer, Object>();
	     for (int j = 0, m = uris.size(); j < m; j++) {
	         Integer clusterID = uriToClusterID.get(uris.get(j));
	         if (clusterID != null) {
	             uniqueURIs.put(clusterID, null);
	         }
	     }
	     if (uniqueURIs.size() == 1) {
	         Integer cid = uniqueURIs.keySet().iterator().next();
	         Integer mid = clusterIDToOntModelID.get(cid);
	         OntModel block = models.get(mid); //mid is the cluster index
	         ArrayList<Statement> statements = sentence.getStatements();
	         
	         for (int j = 0, m = statements.size(); j < m; j++) {
	        	 block.add(statements.get(j));   //////////////////////////////////********************** this line add statement to model
	        	 // if one statement add to the file, we should for its subject-object save the number of  link
	        	 RDFNode ObjectURI = statements.get(j).getObject();
	             RDFNode SubjectURI = statements.get(j).getSubject();
	             RDFNode PropertyURI = statements.get(j).getPredicate();  
	             if (ObjectURI.isURIResource() && SubjectURI.isURIResource() ){
		             String[] iProperty = PropertyURI.toString().split("\\#");
		       		 String[] iSubject = SubjectURI.toString().split("\\#");
		       		 String[] iObject = ObjectURI.toString().split("\\#");
		       		 if(iProperty!=null && iSubject!=null && iObject!=null)
		       		 {
		       		 if (iProperty.length>1){
			       		 if (iProperty[1].toLowerCase().equals("subclassof") || iProperty[1].toLowerCase().equals("haspropoerty")) { // TO DO: we should those acceptable property in this line such as SubclassOf 
				         	 int indexSubjectName =0;
				         	 if(iSubject.length >1) indexSubjectName=BuildModel.findIndex(iSubject[1]); 
					         if (indexSubjectName >0 )
					             {NumLickConcept[mid][indexSubjectName] = NumLickConcept[mid][indexSubjectName] +1; }  // mid is the index of CH
					      	 int indexObjectName =0;
					      	 if(iObject.length>1) BuildModel.findIndex(iObject[1]); 
					      	 if (indexObjectName >0 )
					         	 {NumLickConcept[mid][indexObjectName] = NumLickConcept[mid][indexObjectName] +1; }
				          }
		       		 }
		       	  }
	            }
	         }
	     }
	 }
	 


	 
	 // adding root 1- for Root concept (RootTag=false) ,  2- for those element with numLink=1
	 	//First phase (1-for Root concept (RootTag=false))
		//if the node does not have superNode, we suppose it is Root and it is alone, so we call addRoot() function for it
		for (int ia=0; ia<BuildModel.NumEntity+1; ia++){
			 if (BuildModel.entities.get(ia).getNamedSupers() == null){
				 Node alone_element= BuildModel.entities.get(ia); 
				 if (uriToClusterID.get(BuildModel.entities.get(ia).toString()) != null){
					 int indexCH_aloneElement = uriToClusterID.get(BuildModel.entities.get(ia).toString());
					 addRoot(alone_element.toString(),indexCH_aloneElement ); //add this element in the ch block
					 //since we add one link in the file (alone_elemenet, subClassOf, "Thing"), so, we should count one link for alone_element in the NumLickConcept array 
					 NumLickConcept[indexCH_aloneElement][indexCH_aloneElement] = NumLickConcept[indexCH_aloneElement][indexCH_aloneElement] +2; //in the next lines, if this array home==1, then it thinks it is alone and does not link to Thing class, so, we add (+2) till it does not be equal 1
				 }
			}
		}
	 	//Second phase (2- for those element with numLink=1)		
		for (int i=0; i<Coordinator.KNumCH; i++){
			for (int j=0; j<BuildModel.NumEntity; j++){
				if (NumLickConcept[i][j] == 1) {
					addRoot(BuildModel.entities.get(j).toString(), i);
				}
			}
		}
	 
	 
	 
	 
	 //Creating Files in Temp folder
	 for (int i = 0, n = models.size(); i < n; i++) {
	     int cid = list.get(i).getClusterID();
	     String filepath = tempDir + ontName + "_block_" + cid + ".owl";
	     File file = new File(filepath);
	     if (file.exists()) {
	         file.delete();
	     } 
	     OntModel block = models.get(i); // Write one block as one model in owl file
	     try {
	         FileOutputStream fos = new FileOutputStream(filepath);
	         BufferedOutputStream bos = new BufferedOutputStream(fos);
	         block.write(bos, "RDF/XML-ABBREV"); //XML format
	         //block.write(bos, "Turtle"); //compact and more readable
	         bos.close();
	         fos.close();
	     } catch (IOException e) {
	         e.printStackTrace();
	     }
	     block.close();
	 }

	 for (int i = 0, n = models.size(); i < n; i++) {
	 	System.out.println(); System.out.println();
	 	System.out.println("Partition  "+ i); 
	 	int cid = list.get(i).getClusterID();
	     String filepath2 = tempDir + ontName + "_block_" + cid + ".owl";
	     try (BufferedReader br = new BufferedReader(new FileReader(filepath2))) {
	     	   String line = null;
	     	   while ((line = br.readLine()) != null) {
	     		   System.out.println(line);
	        	   }
	     	} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}      
	 }
	 return models;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////////////////AddingRoot_Phase //////////////////////////////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public static void addRoot(String concept, int NumCluster){
		
		OntModel block = models.get(NumCluster);
		String[] Sp = BuildModel.entities.get(1).toString().split("\\#");	String URItext = Sp [0] + "#"; //Extract URI
		Resource subjectNew =  ResourceFactory.createResource(concept); 
		Property predicateNew  = ResourceFactory.createProperty("http://www.w3.org/2000/01/rdf-schema#subClassOf");
		// TO DO ::: Between the next three lines, we should select one of them (their result is the same, but which one is fast?)
		//Resource objectNew = block.getResource( URItext + "Thing" );
		//Resource objectNew= block.getProperty(URItext+ "Thing");
		RDFNode objectNew = ResourceFactory.createResource("http://www.w3.org/2002/07/owl#Thing");
		Statement statementNew = block.createStatement(subjectNew,  predicateNew, objectNew);
		block.add(statementNew);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public static NodeList getProperty_iclass(Node iclass){
	NodeList ipropertyList = new NodeList();
	
	for (int k=0; k<Property_info.size(); k++){
		Node b= Property_info.get(k).getDomainName();
		Node c= Property_info.get(k).getRangeName();
		if (iclass == b ){ 
			ipropertyList.add(Property_info.get(k).getName());
		}
		if (iclass == c){
			ipropertyList.add(Property_info.get(k).getName());
		}
	}
	
	return ipropertyList;
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static int findShareKnowledge (AxiomCluster icluster, AxiomCluster jcluster){
	int sim=0;
	for (Iterator<Node> iter = icluster.listElements(); iter.hasNext();) {
		Node inode=iter.next();
		NodeList isub = inode.getNamedSubs();
		NodeList isuper = inode.getNamedSupers();
		NodeList ipro = getProperty_iclass(inode);
		
		for (Iterator<Node> jter = jcluster.listElements(); jter.hasNext();) {
			Node jnode=jter.next();
			NodeList jsub = jnode.getNamedSubs();
			NodeList jsuper = jnode.getNamedSupers();
			NodeList jpro = getProperty_iclass(jnode);
			sim = sim + countSameElement (isub,jsub);
			sim = sim + countSameElement (isuper, jsuper);
			sim = sim + countSameElement (ipro, jpro);
			
		}
	}
	
	return sim;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static int countSameElement (NodeList iList, NodeList jList){
	int countSame=0;
	if (iList == null || jList == null) {
		return 0;
	}else {
		for (int i=0; i<iList.size(); i++) {
			for (int j=0; j<jList.size(); j++){
				if (iList.get(i) == jList.get(j))
					countSame ++ ;
			}
		}
	}
	return countSame;
}

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static ArrayList<Integer> findMax (int [] iarray){


	ArrayList <Integer> idxArray = new ArrayList<Integer>();
	int max = iarray[0];
	
	for (int i=1; i<iarray.length; i++){
		if (iarray[i] >= max){
			max = iarray[i];
		}
	}

	//return those element that are equal to max (maybe we have several equla value)
	for (int j=0; j<iarray.length; j++){
		if (iarray [j] == max){
			idxArray.add(j);
		}
	}
	return idxArray;
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static AxiomCluster mergeCopy (AxiomCluster firstCluster, AxiomCluster secondCluster, int k){
	AxiomCluster copyCLuster =new AxiomCluster (k);
	copyCLuster = firstCluster;
	if (secondCluster != null){
		Iterator<Node> list= secondCluster.listElements();
		while (list.hasNext()){
			Node inode = list.next();
			copyCLuster.putElement(inode.toString(), inode);
		}
	}
	
	return copyCLuster;
}
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
private static ArrayList<String> findCandidate(int [][] iarray ) {

	int rowMax = 0, columnMax = 0;
	rowAllMax = new int [iarray.length];
	columnAllMax = new int [iarray.length];
	ArrayList<String> strMax= new ArrayList<String>();
	
	for (int i=0; i<iarray.length; i++){
		for (int j=0; j<iarray.length; j++){
			if (iarray[i][j] > rowMax ) rowMax = iarray[i][j];
		}
		rowAllMax[i] = rowMax ; 
		rowMax = 0;
	}
	//Maybe in each row we have several equal number which they are max
	for (int i=0; i<iarray.length; i++){
		for (int j=0; j<iarray.length; j++){
			if (rowAllMax[i] >0 ){
				//if (iarray[i][j] == rowAllMax[i] ) strMax.add((i + "&" + j).toString());
				if (iarray[i][j] == rowAllMax[i] ) strMax.add((i + "&" + j + "&" + rowAllMax[i]).toString());
			}
		}
	}
	
	//for column
	for (int i=0; i<iarray.length; i++){
		for (int j=0; j<iarray.length; j++){
			if (iarray[j][i] > columnMax ) columnMax = iarray[j][i];
		}
		columnAllMax[i] = columnMax ; 
		columnMax = 0; 
	}
	//Maybe in each row we have several equal number which they are max
	for (int i=0; i<iarray.length; i++){
		for (int j=0; j<iarray.length; j++){
			if (columnAllMax[i] > 0){
				//if (iarray[j][i] == columnAllMax[i] ) strMax.add((j + "&" + i).toString());
				if (iarray[j][i] == columnAllMax[i] ) strMax.add((j + "&" + i + "&" + columnAllMax[i]).toString());
			}
		}
	}
	int tt=0;
	
	int t=0;
return strMax;
	
}
/////////////////////////////////////////////////////////////
public static int median(int [] m) {
    Arrays.sort(m);
	int middle = m.length/2;
    if (m.length%2 == 1) {
        return m[middle];
    } else {
        return (m[middle-1] + m[middle]) / 2;
    }
}
}