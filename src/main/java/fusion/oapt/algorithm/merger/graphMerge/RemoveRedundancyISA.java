package fusion.oapt.algorithm.merger.graphMerge;



import fusion.oapt.general.ontoTograph.GraphConstruction;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import agg.xt_basis.Rule;

public class RemoveRedundancyISA
{

	private Rule rule;
	
	public  RemoveRedundancyISA (GraGra ruleGrammar) throws Exception
	{

			GraphConstruction graphCon= new GraphConstruction();
				
			// Create rule
			rule = ruleGrammar.createRule();
			rule.setName("RemoveISARedundancy");
			
			
			/** LHS */
			Graph Left= rule.getLeft();						
			Node NLC1= graphCon.createNode (Left, ruleGrammar.getTypeSet().getTypeByName("Class"));
			Node NLC2= graphCon.createNode (Left,ruleGrammar.getTypeSet().getTypeByName("Class"));
			Node NLC3= graphCon.createNode (Left,ruleGrammar.getTypeSet().getTypeByName("Class"));
			graphCon.createArc(Left, ruleGrammar.getTypeSet().getTypeByName("subClassOf"), NLC3,NLC2);	
			graphCon.createArc(Left, ruleGrammar.getTypeSet().getTypeByName("subClassOf"), NLC2,NLC1);	
			graphCon.createArc(Left, ruleGrammar.getTypeSet().getTypeByName("subClassOf"), NLC3,NLC1);	
			
		
			/** RHS */			
			Graph right = rule.getRight();	
			Node NRC1= graphCon.createNode (right, ruleGrammar.getTypeSet().getTypeByName("Class") );		
			Node NRC2= graphCon.createNode (right, ruleGrammar.getTypeSet().getTypeByName("Class"));
			Node NRC3= graphCon.createNode (right, ruleGrammar.getTypeSet().getTypeByName("Class"));
			graphCon.createArc(right, ruleGrammar.getTypeSet().getTypeByName("subClassOf"), NRC3,NRC2);	
			graphCon.createArc(right, ruleGrammar.getTypeSet().getTypeByName("subClassOf"), NRC2,NRC1);	
		
		
			rule.addMapping(NLC1, NRC1);
			rule.addMapping(NLC2, NRC2);
			rule.addMapping(NLC3, NRC3);
		}
			
			
		public Rule getRule() 
		{
			return rule;
		}
	}
