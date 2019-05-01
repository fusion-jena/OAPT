package fusion.oapt.algorithm.merger.graphMerge;


import java.util.BitSet;
import java.util.Vector;

import agg.cons.AtomConstraint;
import agg.util.Pair;
import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.Match;
import agg.xt_basis.MorphCompletionStrategy;
import agg.xt_basis.Morphism;
import agg.xt_basis.OrdinaryMorphism;
import agg.xt_basis.Rule;
import agg.xt_basis.StaticStep;
import agg.xt_basis.TypeException;
import agg.xt_basis.TypeSet;
import agg.xt_basis.csp.CompletionPropertyBits;
import agg.xt_basis.csp.Completion_CSP;


/*********************************************************
 * 
 * Each execution of this class is an application of a SPO 
 * 
 *********************************************************/ 

public class TransformationGraph 
{
	private String mergedFile;
	
	public TransformationGraph (GraGra graphGrammar,Rule rule)
	{	
		graphGrammar.getTypeSet().setLevelOfTypeGraphCheck(TypeSet.ENABLED_MAX_MIN);
			
		Pair<Object, String> pair = graphGrammar.isReadyToTransform(true);
		Object test = null;
			
		if (pair != null)
			test = pair.first;
			
		if (test != null) 
		{
			if (test instanceof Graph)
			{
				System.out.println("Grammar  " + graphGrammar.getName()
							+ "  graph: " + graphGrammar.getGraph().getName()
							+ "  is not ready for transform");
			} 
			else if (test instanceof AtomConstraint)
			{
				String s0 = "Atomic graph constraint  \""
							+ ((AtomConstraint) test).getAtomicName()
							+ "\" is not valid. "
							+ "\nPlease check: "
							+ "\n  - graph morphism ( injective and total )  "
							+ "\n  - attribute context ( variable and condition declarations ).";
				//System.out.println(s0);
					
			} 
			else if (test instanceof Rule) 
			{
				String s0 = "Rule  \""
							+ ((Rule) test).getName()
							+ "\" : "
							+ ((Rule) test).getErrorMsg()
							+ "\nPlease check: \n  - attribute settings of the new objects of the RHS \n  - attribute context ( variable and condition declarations ) of this rule.\nThe grammar is not ready to transform.";
				//System.out.println(s0);
			}				
		}
		
		//System.out.println("Grammar  " + graphGrammar.getName() + "  is ready to transform");



		Vector<String> gratraOptions = new Vector<String>();
		gratraOptions.add("CSP");
		//gratraOptions.add("injective");
		//gratraOptions.add("dangling");
		gratraOptions.add("identification");
		gratraOptions.add("NACs");
		gratraOptions.add("PACs");
		gratraOptions.add("GACs");
		gratraOptions.add("consistency");
		graphGrammar.setGraTraOptions(gratraOptions);
		
		 MorphCompletionStrategy mcs = new Completion_CSP(true);
		 BitSet properties = new BitSet();
         properties.set(CompletionPropertyBits.DANGLING);
         properties.set(CompletionPropertyBits.INJECTIVE, false);
         mcs.setProperty("DANGLING");
      
         graphGrammar.setGraTraOptions(mcs);

         Vector<String> newOptions = graphGrammar.getGraTraOptions();
        
		Match match = null;

	
		System.out.println("Try to apply  rule1: " + rule.getName());
			
		// Create an empty match; check if the match is complete
		//System.out.println("Rule  " + rule.getName() + "    >> create match");
		
		match = graphGrammar.createMatch(rule);
		
					
			if(match.nextCompletion())
			{

				if (match.isValid()) 
				{
					//Step step = new Step();
					try 
					{
						Morphism co = StaticStep.execute(match);
						System.out.println("Rule  " + match.getRule().getName() + " : step is done");
						((OrdinaryMorphism)co).dispose();					
					} 
					catch (TypeException ex)
					{
						ex.printStackTrace();
						graphGrammar.destroyMatch(match);
						System.out.println("Rule " + match.getRule().getName() + " : match failed : " + ex.getMessage());
					}
				} else
						System.out.println("Rule  " + match.getRule().getName()
								+ " : match is not valid");
			}
					
		graphGrammar.destroyMatch(match);
		// match.clear();			*/
			
		// Set file name and save grammar
		String fn = "resources/merge/MergeResult.ggx";
		graphGrammar.save(fn);				 
				
		//System.out.println("Grammar "+graphGrammar.getName() +" saved in "+fn);	
      
		graphGrammar.save(fn);
		mergedFile=fn;
		System.out.println("After appling rule  graphGrammar  saved in  "+ fn);	
		System.out.println("\n ////////////////   A new Transformation process ////////  \n");
	}
	
	public String getMergedFile()
	{
		return mergedFile;
	}
}
