package fusion.oapt.algorithm.merger.graphMerge;



import java.util.List;

import fusion.oapt.general.ontoTograph.GraphConstruction;
import agg.xt_basis.Arc;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;


public class GraphComposition
{
	
	private TransformationGraph TG;
	
	public String removeQuotNodeName(String nodeIRI )	
	{
		if(nodeIRI.endsWith("\""))    		
			nodeIRI= (nodeIRI.substring(1, nodeIRI.length()-1));
		
		return nodeIRI;
	}
	
	
	public GraphComposition(GraGra gramO1, GraGra gramO2, GraGra gramOG, List <String> commenNode, List<List> equivalentNode, List<List> synonymNode, List<List> subClassNode) throws Exception
	{
		GraphAnalysis  graphAna= new GraphAnalysis();
		GraphConstruction  graphCompo= new GraphConstruction ();
		
		GraGra gramForEquivC=  new GraGra(true);			
		Graph LHSForEquivC =gramForEquivC.getGraph(0);
		
		/****************************************************************************/
		/** Add the equivalent relation ;  for example : hastopic and has_Topic*/	
		/****************************************************************************/
		
		for(List <Node> equivNode : equivalentNode)
		{ 
			if(equivNode!=null)
			{
			Node nn=(Node)(equivNode.get(0));
			if(nn!=null){
			//System.out.println(equivNode.size()+"the list\t"+(Node)(equivNode.get(0)));
			String typeNode1 = ((Node)(equivNode.toArray()[0])).getType().getStringRepr();			
			String C1= ((Node)(equivNode.toArray()[0])).getAttribute().getMemberAt(0).toString();
			String C2= ((Node)(equivNode.toArray()[1])).getAttribute().getMemberAt(0).toString();
			
			//We assume that the mapping is 1:1  
			RenameConcept adaptConcept= new RenameConcept (gramO1, typeNode1,removeQuotNodeName(C1),removeQuotNodeName(C2));  
	    	TG=new TransformationGraph (gramO1, adaptConcept.getRule());      	
	    	
	    	// Create the graph that will be the LHS of the SPO
	    	LHSForEquivC.createNode((Node)(equivNode.toArray()[0])); 	
	    	commenNode.add(removeQuotNodeName(C2));
		}}}
	
		gramOG= gramO1;
		
		GraGra gramForCommC=  new GraGra(true);			
		Graph LHSForCommC =gramForCommC.getGraph(0);
		
		// if has not common concept then, the LHS = empty set
		if( commenNode.size()==0)
		{	
			AddGraphS2 addGraph= new AddGraphS2(gramOG,LHSForEquivC, gramO2.getGraph(0));  
			TG=new TransformationGraph (gramOG, addGraph.getRule()); 
		}
			
		else
		{
			for(String commonNode : commenNode)
			{ 	
				Node node= graphAna.getNode(gramO1.getGraph(0), commonNode);
				LHSForCommC.createNode(node); 			
			}
			
			// it will be add the different arc between the common concepts: !! the node is common but be careful the arcs are not necessary commons
			for(String commonNode : commenNode)
			{ 
				Node node= graphAna.getNode(gramO1.getGraph(0), commonNode);			
				List <Arc> arcOut = node.getOutgoingArcsVec() ;				
				
				for( Arc arc : arcOut)
				{
					String nameNT = removeQuotNodeName(arc.getTarget().getAttribute().getValueAsString(0));
					Node nodeS= graphAna.getNode(gramForCommC.getGraph(0), commonNode);
					Node nodeT = graphAna.getNode(gramForCommC.getGraph(0), nameNT);
				
					
					if( commenNode.contains(nameNT) && graphAna.getArc(gramForCommC.getGraph(0), arc.getType(), nodeS, nodeT)==null) 
					{
						if(arc.attrExists())
						{
							graphCompo.createArc(LHSForCommC,arc.getType(), nodeS, nodeT,(String) arc.getAttribute().getValueAt(arc.getAttribute().getMemberAt(0).getName()));	
						}
						else
							LHSForCommC.createArc(arc.getType(), nodeS, nodeT);
					}
				}
				
				List <Arc> arcIn = node.getIncomingArcsVec() ; 
				for( Arc arc : arcIn)
				{
					String nameNS = removeQuotNodeName(arc.getSource().getAttribute().getValueAsString(0));
					Node nodeS= graphAna.getNode(gramForCommC.getGraph(0), nameNS);
					Node nodeT = graphAna.getNode(gramForCommC.getGraph(0), commonNode);
					
					//System.out.println( arc.getType().getName());
					if( commenNode.contains(nodeS) && graphAna.getArc(gramForCommC.getGraph(0),  arc.getType(), nodeS, nodeT)==null) 
					{	
						if(arc.attrExists())
						{
							graphCompo.createArc(LHSForCommC,arc.getType(), nodeS, nodeT,(String) arc.getAttribute().getValueAt(arc.getAttribute().getMemberAt(0).getName()));
						}
							
						else
							LHSForCommC.createArc(arc.getType(), nodeS, nodeT);
					}
				}
			}
			
					
			AddGraphS2 addGraph= new AddGraphS2(gramOG,LHSForCommC, gramO2.getGraph(0));  
			TG=new TransformationGraph (gramOG, addGraph.getRule()); 
		}
		
		/***************************************************************************
		/** Add the synonym relation ; for example article is equivalent to paper */
		/****************************************************************************/
		
		if( synonymNode!=null && synonymNode.size()!=0)
		for(List <Node> equivNode : synonymNode)
		{ 			
			Node nn=(Node)(equivNode.toArray()[0]);
			if(nn!=null){
			String C1= (String)((Node)(equivNode.toArray()[0])).getAttribute().getValueAt("iri");
			String C2= (String) ((Node)(equivNode.toArray()[1])).getAttribute().getValueAt("iri");
			
			//We assume that the mapping 1:1  
			AddEquivalentClass equiClass= new AddEquivalentClass(gramOG,C1,C2);  
			if (equiClass.getRule()!=null)
				TG=new TransformationGraph (gramOG, equiClass.getRule());      	
		}}
		
		
		
		/***************************************************************************
		/** Add the subsumption relation*/	
		/****************************************************************************/
		
		if( subClassNode!=null && subClassNode.size()!=0)
		for(List <String> subNode : subClassNode)
		{ 
			String C1= (String)((Node)(subNode.toArray()[0])).getAttribute().getValueAt("iri");
			String C2= (String) ((Node)(subNode.toArray()[1])).getAttribute().getValueAt("iri");
			
			//We assume that the mapping 1:1  
			AddSubClass subClass= new AddSubClass(gramOG,C1,C2);  
			if(subClass.getRule()!=null)
				TG=new TransformationGraph (gramOG, subClass.getRule());      	
		}
		
		
		/****************************************************************************/
		// remove the redundancy into the graph 
		//for example the case of the subsumption: C2 is-a C3 ; C1 is-a C2 and C1 is-A C3 ; so we should remove C1 is-a C3
		/****************************************************************************/
		int nbArcsBeforeTrans=gramOG.getGraph(0).getArcsSet().size();
		RemoveRedundancyISA removISARed= new RemoveRedundancyISA(gramOG);		
		int nbArcsAfterTrans=0;
		
		if(removISARed.getRule()!=null)
		{
			while(nbArcsBeforeTrans !=nbArcsAfterTrans)
			{
				nbArcsBeforeTrans=gramOG.getGraph(0).getArcsSet().size();
				TG=new TransformationGraph (gramOG, removISARed.getRule());  				
				nbArcsAfterTrans=gramOG.getGraph(0).getArcsSet().size();				
			}
		}		
	}
	public String getMergedFile()
	{
		return TG.getMergedFile();
	}
}
