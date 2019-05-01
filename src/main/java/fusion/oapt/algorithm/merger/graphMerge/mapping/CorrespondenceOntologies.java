package fusion.oapt.algorithm.merger.graphMerge.mapping;

import java.util.LinkedList;
import java.util.List;

import fusion.oapt.algorithm.matcher.string.ISub;
import fusion.oapt.algorithm.matcher.string.WordNetMatcher;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;


public class CorrespondenceOntologies 
{
	
	private List <Node> commCC,  diffCC  ;
	private List <List>  equivCC,semanticCC;
	
	Graph o1, o2 ;
	GraGra gramO1 , gramO2 ;
	List <Node> listNCO2, listNCO1;
	
	public CorrespondenceOntologies (GraGra gramO1, GraGra gramO2)
	{
		this.gramO1= gramO1;
		this.gramO2=gramO2;
		o1= gramO1.getGraph(0);
		o2= gramO2.getGraph(0);
	}
	
	
	public String removeQuotNodeName(String nodeIRI )	
	{
		if(nodeIRI.endsWith("\""))    		
			nodeIRI= (nodeIRI.substring(1, nodeIRI.length()-1));
	 
		return nodeIRI;
	}
	
		
	public void commonConcepts (String typeNode)
	{
		listNCO1= o1.getNodes(gramO1.getTypeSet().getTypeByName(typeNode));	 
		listNCO2= o2.getNodes(gramO2.getTypeSet().getTypeByName(typeNode));	
	    
		
		commCC= new LinkedList();		
		equivCC= new LinkedList();	
		semanticCC= new LinkedList();
		diffCC=o1.getNodes(gramO1.getTypeSet().getTypeByName(typeNode));
		
		// get the list of commmon concepts
		if(listNCO1 != null && listNCO1.size()!=0)
		{
			for( Node nCO1 : listNCO1 )
			{		
				String clsO1 = (String) nCO1.getAttribute().getValueAt("name");	 
				if( listNCO2!=null)
				for( Node nCO2: listNCO2 )
				{	  
					String clsO2 = (String) nCO2.getAttribute().getValueAt("name");	
					if (clsO1.equalsIgnoreCase(clsO2))
					{						
						commCC.add(nCO1);
			        	diffCC.remove(nCO1);		            
			            break ;
			        }
			   	}		   	
			}
		}
		
		
		listNCO1 =diffCC;
		
		// get the list of the equivalent concepts by using the Levenshtein distance
		if( listNCO1!=null)
			for(int i=0; i< listNCO1.size(); i++)		
		   	{	 	
				Node nCO1= listNCO1.get(i);
				String clsO1 = (String) nCO1.getAttribute().getValueAt("name");	
				if( listNCO2!=null)
				for( Node nCO2 : listNCO2 )
				{		
					String clsO2 = (String) nCO2.getAttribute().getValueAt("name");
			        int tailleMin = Math.min( clsO1.length(), clsO2.length());
			        double sim=ISub.getSimilarity(clsO1.toLowerCase(),  clsO2.toLowerCase());
			       //Levenshtein l = new Levenshtein( clsO1.toLowerCase(),  clsO2.toLowerCase());
			       if(sim>0.9)// if(l.getSimilarity()<3) // we should specify the threshold similarity
			        {
			        	List <Node> equiv = new LinkedList();
			        	equiv.add(nCO1);
			        	equiv.add(nCO2);
			        	equivCC.add(equiv);	
			      
			        	diffCC.remove(nCO1);		        		
			        	break ;
			        }			    
			   	}
		   	}
	
		listNCO1 =diffCC;
		
		// get the list of the similar concepts by using the Wordnet ontology
		if( listNCO1!=null)
			for(int i=0; i< listNCO1.size(); i++)		
		   	{	 	
				Node nCO1= listNCO1.get(i);
				
				String clsO1 = (String) nCO1.getAttribute().getValueAt("name");	
				WordNet ritaW= new WordNet();
				WordNetMatcher WM=new WordNetMatcher();
				if( listNCO2!=null)		
				for( Node nCO2 : listNCO2 )
				{		
					String clsO2 = (String) nCO2.getAttribute().getValueAt("name");
			   		//boolean simSem= ritaW.getSimilarity(clsO1.toLowerCase(), clsO2.toLowerCase());
			        double sim=WM.compute(clsO1.toLowerCase(), clsO2.toLowerCase());
			        if(sim>0.8)//(simSem==true)
			        {
			        	List <Node> seman= new LinkedList();
			        	seman.add(nCO1);
			        	seman.add(nCO2);
			        	semanticCC.add(seman);	
			        	diffCC.remove(nCO1);		        	
			        	break ;
			        }			    
			   	}
		   	}				
	}


	public List<Node> getCommCC() {		
		return commCC;
	}


	public List<List> getEquivCC() {		
		return equivCC;
	}


	public List<Node> getDiffCC() {		
		return diffCC;
	}


	public List<List> getSemanticCC() {
		return semanticCC;
	}	
}
