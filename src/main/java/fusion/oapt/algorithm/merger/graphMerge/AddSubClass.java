package fusion.oapt.algorithm.merger.graphMerge;


//import graphConstruction.GraphConstruction;
import fusion.oapt.general.ontoTograph.GraphConstruction;
import agg.attribute.AttrInstance;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Node;
import agg.xt_basis.OrdinaryMorphism;
import agg.xt_basis.Rule;


public class AddSubClass 
{

	private Rule rule;
	
	// A method which extract localName from the iri of the node
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
			
	public  AddSubClass(GraGra ruleGrammar, String iriC1, String iriC2) throws Exception
	{
			String nameC1= getLoclName(iriC1);
			String nameC2=getLoclName(iriC2);
			
			GraphConstruction graphCon= new GraphConstruction();
				
			// Create rule
			rule = ruleGrammar.createRule();
			rule.setName("AddSubClass");
			
			
			/** LHS */
			Graph Left= rule.getLeft();						
			Node NLC1= graphCon.createNode (Left, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC1);
			Node NLC2= graphCon.createNode (Left,ruleGrammar.getTypeSet().getTypeByName("Class"), iriC2);
		
			/** RHS */			
			Graph right = rule.getRight();	
			Node NRC1= graphCon.createNode (right, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC1, nameC1);		
			Node NRC2= graphCon.createNode (right, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC2, nameC2);
			graphCon.createArc(right, ruleGrammar.getTypeSet().getTypeByName("subClassOf"), NRC1,NRC2);	
			
			// add a rule morphism
			if (NLC1!= null  && NRC1 != null &&  NLC2!= null  && NRC2 != null ) 
			{
				rule.addMapping(NLC1, NRC1);
				rule.addMapping(NLC2, NRC2);			
			}
				
			/** NAC */
			// The different axioms which must satisfied for the application of the AddSubClass
			
			// Nac 1
			OrdinaryMorphism nac1 = rule.createNAC();
			nac1.setName("notDisjoint");
		
			Graph nac1Graph = nac1.getTarget();				
			Node  NNac1C1= graphCon.createNode (nac1Graph, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC1);
			Node NNac1C2= graphCon.createNode (nac1Graph, ruleGrammar.getTypeSet().getTypeByName( "Class"), iriC2);
			graphCon.createArc(nac1Graph, ruleGrammar.getTypeSet().getTypeByName("disjointWith"),  NNac1C1, NNac1C2);
			
			nac1.addMapping(NLC1, NNac1C1);
			nac1.addMapping(NLC2, NNac1C2);
			
			// Nac 2
			OrdinaryMorphism nac2 = rule.createNAC();
			nac2.setName("notDisjoint");
				
			Graph nac2Graph = nac2.getTarget();				
			Node  NNac2C1= graphCon.createNode (nac2Graph,ruleGrammar.getTypeSet().getTypeByName("Class"), iriC1);
			Node NNac2C2= graphCon.createNode (nac2Graph, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC2);
			graphCon.createArc(nac2Graph, ruleGrammar.getTypeSet().getTypeByName( "disjointWith"), NNac2C2, NNac2C1);
					
			nac2.addMapping(NLC1, NNac2C1);
			nac2.addMapping(NLC2, NNac2C2);		
			
			// Nac 3			
			OrdinaryMorphism nac3 = rule.createNAC();
			nac3.setName("notExist");
							
			Graph nac3Graph = nac3.getTarget();				
			Node  NNac3C1= graphCon.createNode (nac3Graph, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC1);
			Node NNac3C2= graphCon.createNode (nac3Graph, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC2);
			graphCon.createArc(nac3Graph, ruleGrammar.getTypeSet().getTypeByName( "subClassOf"),  NNac3C1, NNac3C2);
								
			nac3.addMapping(NLC1, NNac3C1);
			nac3.addMapping(NLC2, NNac3C2);	
			
			// Nac 4			
			OrdinaryMorphism nac4 = rule.createNAC();
			nac4.setName("notRedundance");
										
			Graph nac4Graph = nac4.getTarget();				
			Node  NNac4C1= graphCon.createNode (nac4Graph, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC1);
			Node NNac4C2= graphCon.createNode (nac4Graph, ruleGrammar.getTypeSet().getTypeByName("Class"), iriC2);
			
			Node NNac4Cls= graphCon.createNode (nac4Graph, ruleGrammar.getTypeSet().getTypeByName("Class"));
			String varName_x = "x";
			AttrInstance attrInstance = NNac4Cls.getAttribute();
			attrInstance.setExprAt(varName_x, "iri");			
			
			graphCon.createArc(nac4Graph, ruleGrammar.getTypeSet().getTypeByName( "subClassOf"),  NNac4C1, NNac4Cls);
			graphCon.createArc(nac4Graph, ruleGrammar.getTypeSet().getTypeByName("subClassOf"),  NNac4Cls,  NNac4C2);
											
			nac4.addMapping(NLC1, NNac4C1);
			nac4.addMapping(NLC2, NNac4C2);
			
		}
			
			
		public Rule getRule() 
		{
			return rule;
		}
	}
