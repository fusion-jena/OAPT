
package fusion.oapt.general.analysis.comparing;

import org.apache.jena.ontology.OntModel;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;

/*import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;*/


import fusion.oapt.general.analysis.ConsistencyAnalysis;
import fusion.oapt.general.analysis.GeneralAnalysis;
import fusion.oapt.general.analysis.RichnessAnalysis;
import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.gui.AnalysisPanel;
import fusion.oapt.model.RBGModel;
import fusion.oapt.model.RBGModelFactory;

public class ComparingTest
{
   

    public static void compareFunc (String nameOnt1, String nameOnt2){
    	
    	
    	OntModel model1 ;   OntModel model2 ;
    	RBGModel rbgmModel ;    RBGModel rbgm2 ;
    	
    	//create model1 and model2
    	BuildModel.BuildModelOnt(nameOnt1);
    	model1=BuildModel.OntModel;
    	rbgmModel=BuildModel.rbgmModel;
    	         
         //call general analyze separately for each model
    	GeneralAnalysis GA=new GeneralAnalysis(model1);
   	 	GA.computeStatics();
   	 	GA.GeneralTest();
   	 
   	 	    	
    	   	 
	   	 //information of second tab: Consistency check
	   	 try {
	   		  double start=System.currentTimeMillis();
	   		  ConsistencyAnalysis CC=new ConsistencyAnalysis();
	   		  CC.ConsistencyTest1();
	   		  //ConsistencyAnalysis.ConsistencyTest1();
	   		  double end1=System.currentTimeMillis();
	   		  AnalysisPanel.ConsistencyTextArea1.append("\n"+ "The reasoning time: "+(end1-start)*.001 +"\t sec");
	   		} catch (OWLOntologyCreationException e) {
	   			e.printStackTrace();
	   		}
	 
	   	 RichnessAnalysis RA=new RichnessAnalysis(model1,rbgmModel);
	   	 RA.RichnessTest1();
	   	 
	  
         BuildModel.BuildModelOnt(nameOnt2);
         model2=BuildModel.OntModel;
         rbgm2=BuildModel.rbgmModel;
         //CmpGeneralAnalyzing1 GA2=new  CmpGeneralAnalyzing1(model2);
         GA=new GeneralAnalysis(model2);
    	 GA.computeStatics();
    	 System.out.println(GA.getNumProp()+"\t Test \t"+GA.getNumClass());
    	 GA.GeneralTest2();
         
    	 try {
	   		  double start=System.currentTimeMillis();
	   		  ConsistencyAnalysis.ConsistencyTest2();
	   		  double end1=System.currentTimeMillis();
	   		  AnalysisPanel.ConsistencyTextArea2.append("\n"+ "The reasoning time: "+(end1-start)*.001 +"\t sec");
	   		} catch (OWLOntologyCreationException e) {
	   			e.printStackTrace();
	   		}
    	  RA=new RichnessAnalysis(model2,rbgm2);
	   	  RA.RichnessTest2();
    	 
	   	 // information of third tab: Richness tab
	   	 
	   	 BuildModel.OntModel = model1; BuildModel.rbgmModel = rbgmModel;
	   	    	
    }

}
