
package fusion.oapt.algorithm.merger.graphMerge.mapping;


import java.io.*;

import org.jdom.*;
import org.jdom.output.*;

import java.util.LinkedList;
import java.util.List;

import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;

public class MappingFile 
{
	static Element racine = new Element("MappingOntology");
	static org.jdom.Document document = new Document(racine);	
	String nomFichier;
	private List <Node> commCC,  diffCC  ;
	private List <List>  equivCC,semanticCC;
	private String mappingFile;
	Graph o1, o2 ;
	GraGra gramO1 , gramO2 ;
	List <Node> listNCO2, listNCO1;
	int noMaps=0;

	// Method allow to add elements in the mapping file according to a specific model
	private void addElement(Element elt, String nomElement,String att2,String nomAtt3, String att3, String att4,String nomAtt4 )
	{
	    Element element = new Element(nomElement);
	    elt.addContent(element);
	   
	    Attribute valueSyn= new Attribute("value",att2);
	    Attribute roleSyn= new Attribute(nomAtt3,att3);
	    Attribute equiv= new Attribute(nomAtt4,att4);
	  
	    element.setAttribute(valueSyn);
	    element.setAttribute(roleSyn);
	    element.setAttribute(equiv);
	}

	public  MappingFile (GraGra gramO1, GraGra gramO2)
	{		
		this.gramO1= gramO1;
		this.gramO2=gramO2;
		o1= gramO1.getGraph(0);
		o2= gramO2.getGraph(0);
		
	    nomFichier="resources/merge/result/Mapping.xml";
	
	    File mapping= new File(nomFichier);
	    if(mapping.exists());
	    {
	        mapping.delete();
	        mapping= new File(nomFichier);
	    }
	    mappingFile=nomFichier;
	    save(nomFichier);
	   
	}
	
	public String getMappingFileName()
	{
		return mappingFile;
	}
	
	static void save(String fichier)
	{
	    try
	    {
	        XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
	        sortie.output(document, new FileOutputStream(fichier));
	    }
	    catch (java.io.IOException e){}
	}
	
	
	// create the mapping file
	public void createMappingFile() throws IOException
	{	
	 
	    CorrespondenceOntologies ontoCoress=  new CorrespondenceOntologies(gramO1, gramO2);
	    ontoCoress.commonConcepts("Class");
	    List <Node> comCC = ontoCoress.getCommCC(); 	    
	    List <List> equivCC= ontoCoress.getEquivCC();
	    List <List> semanCC= ontoCoress.getSemanticCC();	    	
	   
	    ontoCoress.commonConcepts("ObjectProperty");
	    List <Node> comOB = ontoCoress.getCommCC(); 
	    List <List> equivOB= ontoCoress.getEquivCC();
	    List <List> semanOB= ontoCoress.getSemanticCC();
	    	
	    ontoCoress.commonConcepts("DataProperty");
	    List <Node> comDP = ontoCoress.getCommCC(); 
	    List <List> equivDP= ontoCoress.getEquivCC();
	    List <List> semanDP= ontoCoress.getSemanticCC();	    	
	    	
	    ontoCoress.commonConcepts("Individual");
	    List <Node> comInd= ontoCoress.getCommCC(); 
	    List <List> equivInd= ontoCoress.getEquivCC();
	    List <List> semanInd= ontoCoress.getSemanticCC();	    
	    
	    List <Node> comNode = new LinkedList();    	
	    comNode.addAll(comCC); comNode.addAll(comOB );comNode.addAll(comDP);comNode.addAll(comInd);
	    
	    // adding the common terms
	    Element enregistrement = new Element("CommonTerms");	
	    racine.addContent(enregistrement );	 
	    
	    if(comNode.size()!=0 )
	    for( Node nn : comNode )
	    {
	    	String nodeName= (String) nn.getAttribute().getValueAt("name");
	    	Element terme = new Element("term");
	 	    Attribute value = new Attribute("value",nodeName);
	 	    Attribute type = new Attribute("type",nn.getType().getName()); 	       
	 	
	 	    terme.setAttribute(value);
	 	    terme.setAttribute(type);	
            enregistrement.addContent(terme);	    
	    }
	
	    // adding the equivalent terms
	    Element equivTermes = new Element("EquivalentTerms");	
	    racine.addContent(equivTermes );	
	    
	    // List of equivalent terms
	    List <List> equivalentNode= new LinkedList();     	
	    equivalentNode.addAll(equivCC); equivalentNode.addAll(equivOB); equivalentNode.addAll(equivDP);     equivalentNode.addAll(equivInd); 	


	    for(List <Node> equivNode : equivalentNode)
	    { 
	     	//We assume that the mapping is 1:1 			
		   String typeNode = ((Node)(equivNode.toArray()[0])).getType().getStringRepr();			
		   String CC1= (String)((Node)(equivNode.toArray()[0])).getAttribute().getValueAt("name");
		   String CC2= (String)((Node)(equivNode.toArray()[1])).getAttribute().getValueAt("name");				
		
		   addElement(equivTermes, "term",CC1,"type", typeNode,  CC2, "equivalent");        
	    }
	    
	    // adding the synonym terms
	    Element synTermes = new Element("SynonymTerms");	
	    racine.addContent(synTermes );	
	    
	    // List of synonyms terms
	    List <List> synonymNode= new LinkedList();     	
	    synonymNode.addAll(semanCC); synonymNode.addAll(semanOB); synonymNode.addAll(semanDP);  synonymNode.addAll(semanInd); 
	    
	    for(List <Node> synNode : synonymNode)
	    { 
	     	//We assume that the mapping is 1:1 			
		   String typeNode = ((Node) (synNode.toArray()[0])).getType().getStringRepr();			
		   String CC1= (String)((Node)(synNode.toArray()[0])).getAttribute().getValueAt("name");
		   String CC2= (String)((Node)(synNode.toArray()[1])).getAttribute().getValueAt("name");				
		
		   addElement(synTermes, "term",CC1,"type", typeNode,  CC2, "synonym");        
	    }
	  	  
	    //save the mapping File
	    save(nomFichier);
	    noMaps=comNode.size()+equivalentNode.size()+synonymNode.size();
	  }

    public int getNoMaps()
    {
    	return noMaps;
    }
   
}