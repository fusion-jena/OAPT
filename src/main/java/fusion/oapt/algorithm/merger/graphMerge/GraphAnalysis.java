package fusion.oapt.algorithm.merger.graphMerge;


import java.util.Iterator;
import java.util.Vector;

import agg.xt_basis.Arc;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import agg.xt_basis.Type;

public class GraphAnalysis 
{
	
	    
	    public Node getNode(Graph graph,  String nodeName, String nameAttribute)
	    {    	
	     	
	    	Iterator <Node>nodeSet =graph.getNodesSet().iterator();
	    	
	    	while( nodeSet.hasNext())                
	    	{
	    		Node  node= nodeSet.next();
	    		if(node.getType().getStringRepr().equals(nodeName)) 
	    			if((node.getAttribute().getMemberAt(0).toString()).equals("\""+nameAttribute+"\""))	    			
	    				return node; 
	    	}
	    	
	    	return null;
	    } 
	   
	    
	    public Node getNode(Graph graph, String nameAttribute)
	    {   	
	     	
	    	Iterator <Node>nodeSet =graph.getNodesSet().iterator();
	    	
	    	while( nodeSet.hasNext())                
	    	{
	    		Node  node= nodeSet.next();	    		
	    			if((node.getAttribute().getMemberAt(0).toString()).equals("\""+nameAttribute+"\""))	    			
	    				return node; 
	    	}
	    	
	    	return null;
	    } 
	    
	    public Node getNodeWithName(Graph graph, String nameAttribute)
	    {   	
	     	
	    	Iterator <Node>nodeSet =graph.getNodesSet().iterator();
	    	
	    	while( nodeSet.hasNext())                
	    	{
	    		Node  node= nodeSet.next();	  
	    		Object nodex =node.getAttribute().getValueAt("name");
	    		if (nodex != null)	    		
	    			if(((String) nodex).equals(nameAttribute))	    			
	    				return node; 	    		
	    	}
	    	
	    	return null;
	    } 
	 
	    public Type getTypNode(Graph typeGraph, String nodeName)
	    {
	    	Iterator <Node>nodeSet =typeGraph.getNodesSet().iterator();
	    	
	    	while( nodeSet.hasNext())                
	    	{
	    		Node  node= nodeSet.next();
	    		if(node.getType().getStringRepr().equals(nodeName))    		
	    			return node.getType();
	    	}
	    	return null;
	    }   
	    
	    
	    public Arc getArc(Graph graph,  Type arcType, Node nodeS, Node nodeT)
	    {    
	     	
	    	Vector <Arc >arcs =graph.getArcs(arcType);
	    	
	    	for( Arc arc : arcs)               
	    	{
	    		if(arc.getSource().getObjectName().equals(nodeS.getObjectName()) && arc.getTarget().getObjectName().equals(nodeT.getObjectName())) 
	    				
	    				return arc; 
	    	}
	    	
	    	return null;
	    } 
	    
	    
	    public Type getArcType(Graph graph, String arcName)
	    {
	    	Iterator <Arc>arcSet =graph.getArcsSet().iterator();  
	    	while( arcSet.hasNext())                
	    	{
	    		Arc  arc= arcSet.next();
	    		if(arc.getType().getStringRepr().equals( arcName))
	    		{
	        		return arc.getType();
	    		}
	    	}
	    	return null;
	    	
	    }
}
