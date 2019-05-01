package fusion.oapt.algorithm.merger.graphMerge;

import java.util.List;

import agg.util.Pair;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import agg.xt_basis.Rule;

public class AddGraphS2 
{
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
	
	
	Rule rule;
	
	public 	AddGraphS2(GraGra ruleGrammar, Graph LHS, Graph RHS) throws Exception
	{	
			// Create  the rule
			rule = ruleGrammar.createRule();
			rule.setName("AddGraph");

			/** LHS */
			Graph Left= rule.getLeft();
			Left.addCopyOfGraph(LHS, false);	
			
		
			/** RHS */
			Graph right= rule.getRight();	
			right.addCopyOfGraph(RHS, false);	
			
		
			List <Node> nodeLeft= Left.getNodesList();
			List <Node> nodeRight= right.getNodesList();
			
			
			for(Node NL : nodeLeft)
			{
				String iriNL = getLoclName(NL.getAttribute().getMemberAt(0).toString());
				for( Node NR : nodeRight)
				{
					String iriNR = getLoclName(NR.getAttribute().getMemberAt(0).toString());
					if( iriNL.equals(iriNR))
					{
						rule.addMapping(NL, NR);
						break;
					}
				}				
			}
		
			
			// Add the Rule 			
			ruleGrammar.addRule(rule);
			Pair<Object, String> pair = ruleGrammar.isReadyToTransform();
		
		}
			
			
		public Rule getRule() 
		{
			return rule;
		}

}
