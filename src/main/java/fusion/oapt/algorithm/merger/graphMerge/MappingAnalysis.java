package fusion.oapt.algorithm.merger.graphMerge;

import org.jdom.input.*;

import java.util.*;
import java.io.*;

import org.jdom.*;

import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;

public class MappingAnalysis 
{    
	private List <Node> commCC;
	private List <List>  equivCC,semanticCC, isaCC;
	
	public MappingAnalysis () 
	{
		
	}
	
	 public void getCorrespNodes(String fileName, Graph GO1, Graph GO2) throws JDOMException, IOException 
	 {
		org.jdom.Document document;
	    SAXBuilder sxb = new SAXBuilder();
	   
	    	document = sxb.build(new File(fileName));
	        Element racine = document.getRootElement();
	        List enregistre= racine.getChildren();  // enregistre will be contained the following elements: 
	        									    //CommonTerms, EquivalentTerms, SynonymTerms,SubsumptionTerms 
	      
	        int size=enregistre.size();
	        org.jdom.Element  CTerms=null;;  	       
	        org.jdom.Element  ETerms=null; ;
	        org.jdom.Element  STerms=null; ; 
	        org.jdom.Element  ISATerms=null;; 
	       switch(size)
	       {
	       case 1:
	    	    CTerms = (org.jdom.Element)enregistre.get(0);
	    	   break;
	       case 2:
	    	      CTerms = (org.jdom.Element)enregistre.get(0); 	       
		          ETerms = (org.jdom.Element)enregistre.get(1); 
		          break;
	       case 3:
	    	     CTerms = (org.jdom.Element)enregistre.get(0); 	       
		          ETerms = (org.jdom.Element)enregistre.get(1); 
		          STerms = (org.jdom.Element)enregistre.get(2);
		          break;
	       case 4:
	    	     CTerms = (org.jdom.Element)enregistre.get(0); 	       
		          ETerms = (org.jdom.Element)enregistre.get(1); 
		         STerms = (org.jdom.Element)enregistre.get(2); 
		         ISATerms = (org.jdom.Element)enregistre.get(3); 
		         break;
	       }
	             
	             

	        GraphAnalysis  graphAna= new GraphAnalysis ();    		
	        commCC =new LinkedList();
	        List childCC=null;; 
	        if(CTerms!=null)
	        {
	        	childCC=CTerms.getChildren(); 
	        }
	           
	        if(childCC!=null && childCC.size()!=0)
	        for(int i=0; i<childCC.size(); i++)
	        {
	        	org.jdom.Element term = ( org.jdom.Element)childCC.get(i);	
	        	commCC.add(graphAna.getNodeWithName(GO1, term.getAttribute("value").getValue()));
	        }
	        
	        equivCC =new LinkedList();
	        List childEC=null;;
	        if(ETerms!=null)
	        {
	        	 childEC=ETerms.getChildren();
	        }
	      	        
	        if(childEC!=null && childEC.size()!=0)
	        for(int i=0; i<childEC.size(); i++)
	        {
	        	List <Node> equiv = new LinkedList();	        	
	        	org.jdom.Element term = (org.jdom.Element)childEC.get(i);
	        	equiv.add(graphAna.getNodeWithName(GO1, term.getAttribute("value").getValue()));
	        	equiv.add(graphAna.getNodeWithName(GO2, term.getAttribute("equivalent").getValue()));	
	        	equivCC.add(equiv);
	        }
	        
	        
	        semanticCC =new LinkedList();
	        List childSC=null;
	        if(STerms!=null)
	        {
	        	childSC=STerms.getChildren();
	         }
	        if(childSC!=null && childSC.size()!=0)
	        for(int i=0; i<childSC.size(); i++)
	        {
	        	List <Node> syn = new LinkedList();	        	
	        	org.jdom.Element term = (org.jdom.Element)childSC.get(i);
	        	syn.add(graphAna.getNodeWithName(GO1, term.getAttribute("value").getValue()));
	        	syn.add(graphAna.getNodeWithName(GO2, term.getAttribute("synonym").getValue()));	
	        	semanticCC.add(syn);
	        }
	        
	        isaCC =new LinkedList();
	        List childIsaC=null; 
	        if(ISATerms!=null)
	        {
	        	childIsaC=ISATerms.getChildren(); 	
	        }
	        if(childIsaC!=null && childIsaC.size()!=0)
	        for(int i=0; i<childIsaC.size(); i++)
	        {
	        	
	        	List <Node> isa = new LinkedList();	        	
	        	org.jdom.Element term = (org.jdom.Element)childIsaC.get(i);
	        	
	        	if(graphAna.getNodeWithName(GO1, term.getAttribute("value").getValue())!=null )
	        	{
	        		isa.add(graphAna.getNodeWithName(GO1, term.getAttribute("value").getValue()));
	        		isa.add(graphAna.getNodeWithName(GO2, term.getAttribute("subClassOf").getValue()));	 
	        	}
	        	else
	        	{
	        		isa.add(graphAna.getNodeWithName(GO2, term.getAttribute("value").getValue()));
		        	isa.add(graphAna.getNodeWithName(GO1, term.getAttribute("subClassOf").getValue()));	 
	        	}
	        	isaCC.add(isa);
	        }
     }
	 
	 public List<Node> getCommCC() {		
			return commCC;
		}


		public List<List> getEquivCC() {		
			return equivCC;
		}
		
		public List<List> getSemanticCC() {
			return semanticCC;
		}	
		
		public List<List> getIsaCC() {
			return isaCC;
		}
}
