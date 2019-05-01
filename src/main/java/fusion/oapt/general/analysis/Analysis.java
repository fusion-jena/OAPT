
package fusion.oapt.general.analysis;

import org.apache.jena.ontology.OntModel;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.model.RBGModel;

public class Analysis
{	
	private  static OntModel model;
	private  static OWLOntology OwlModel;
	private  static RBGModel rbgm;
	
	public Analysis(){}
	
	public Analysis(OntModel model, OWLOntology owl, RBGModel rbgm )
	{
		Analysis.model=model;
		Analysis.OwlModel=owl;
		Analysis.rbgm=rbgm;
	}
	public static  void AnalyzingTestRun(){
	 //information of first tab : General information
	 GeneralAnalysis GA=new GeneralAnalysis(model);
	 GA.computeStatics();
	 GA.GeneralTest();
	 
	 //information of second tab: Consistency check
	 try {
		  double start=System.currentTimeMillis();
		  ConsistencyAnalysis CC=new ConsistencyAnalysis(OwlModel);
		  CC.ConsistencyTest1();
		 // ConsistencyAnalysis.ConsistencyTest1();
		  double end1=System.currentTimeMillis();
          OPATgui.ConsistencyTextArea1.append("\n"+ "---> The reasoning time: "+(end1-start)*.001 +"\t sec");
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		}
	 
	 // information of third tab: Richness tab
	 RichnessAnalysis RA=new RichnessAnalysis(model,rbgm);
	 RA.RichnessTest1();
 } 
}