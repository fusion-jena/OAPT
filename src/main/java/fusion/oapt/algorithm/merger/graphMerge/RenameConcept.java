package fusion.oapt.algorithm.merger.graphMerge;

import java.util.Vector;

import fusion.oapt.general.ontoTograph.GraphConstruction;
import agg.attribute.impl.ValueMember;
import agg.util.Pair;
import agg.xt_basis.Arc;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import agg.xt_basis.OrdinaryMorphism;
import agg.xt_basis.Rule;

public class RenameConcept
{

	private Rule rule;

	
	public String getLoclName(String iri)
	{
		if( iri.contains("#"))
		{
			iri=iri.substring(iri.indexOf("#")+1, iri.length());				
			return iri;
		}
		while(iri.contains("/"))
		{			
			iri=iri.substring(iri.indexOf("/")+1, iri.length());		
		}
		
		return iri;
	}
			
	public  RenameConcept(GraGra ruleGrammar, String NodeType, String iriC1, String iriC2) throws Exception
	{
			String nameC2=getLoclName(iriC2);
			
			GraphConstruction graphCon= new GraphConstruction();			
		    Graph typeGraph= ruleGrammar.getTypeGraph();
		    GraphAnalysis  graphAna= new GraphAnalysis ();    			 
				
			// Create rule
			rule = ruleGrammar.createRule();
			rule.setName("RenameConcept");
			
			// test for ObjectProperty : it must update the different ObjectPropertyAssertion
			if( NodeType.equals("ObjectProperty"))
			{
				
				Vector <Arc> objAssertion =ruleGrammar.getGraph().getArcs(ruleGrammar.getTypeSet().getTypeByName("objectPropertyAssertion"));
				
				for( Arc  ass : objAssertion )				
					if( ((String) ass.getAttribute().getValueAt("iriObjProp")).equals(iriC1))					
						((ValueMember)(ass.getAttribute().getMemberAt(0))).setExprAsObject(iriC2);
				
			}
			
			
			/** LHS */
			Graph Left= rule.getLeft();						
			Node NLC1= graphCon.createNode (Left, graphAna.getTypNode(typeGraph, NodeType), iriC1);
			
		
			/** RHS */			
			Graph right = rule.getRight();	
			Node NRC1= graphCon.createNode (right, graphAna.getTypNode(typeGraph, NodeType), iriC2, nameC2);		
			
			
			// Add a rule morphism
			if (NLC1!= null  && NRC1 != null ) 
			{
				rule.addMapping(NLC1, NRC1);			
			}
			
				
			/** NAC */
			OrdinaryMorphism nac1 = rule.createNAC();
			nac1.setName("notDuplicate");
		
			Graph nac1Graph = nac1.getTarget();				
			graphCon.createNode (nac1Graph, ruleGrammar.getTypeSet().getTypeByName(NodeType), iriC2);	
						
			// AddRule 			
			ruleGrammar.addRule(rule);	
			Pair<Object, String> pair = ruleGrammar.isReadyToTransform();	
		}			
			
		public Rule getRule() 
		{
			return rule;
		}
	}
