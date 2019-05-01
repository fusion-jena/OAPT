package fusion.oapt.general.analysis;

import java.io.IOException;

import org.apache.jena.ontology.OntModel;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;




import fusion.oapt.general.cc.ModelBuild;
//import fusion.oapt.general.cc.BuildModel;
import fusion.oapt.general.gui.OPATgui;
import fusion.oapt.model.RBGModel;


public class Compare {
	
public static void compareFunc (String nameOnt1, String nameOnt2){
    	
    	
    	OntModel model1, model2 ;
    	RBGModel rbgmModel, rbgm2 ;
    	OWLOntology owl1, owl2;
    	    	
    	ModelBuild MB1=new  ModelBuild(nameOnt1);
    	MB1.build();
    	model1=MB1.getModel();
    	rbgmModel=MB1.getRBGModel();
    	owl1=MB1.getOWLModel();
    	
    	  	 
         //call general analyze separately for each model
    	GeneralAnalysis GA=new GeneralAnalysis(model1);
    	ConsistencyAnalysis CA=new ConsistencyAnalysis(owl1);
   	 	GA.computeStatics();
   	 	GA.GeneralTest();
   	 
   	 	    	
    	   	 
	   	 //information of second tab: Consistency check
	   	 try {
	   		  double start=System.currentTimeMillis();
	   		 CA.ConsistencyTest1();
	   		  double end1=System.currentTimeMillis();
	   		  OPATgui.ConsistencyTextArea1.append("\n"+ "The reasoning time: "+(end1-start)*.001 +"\t sec");
	   		} catch (OWLOntologyCreationException e) {
	   			e.printStackTrace();
	   		}
	 
	   	 RichnessAnalysis RA=new RichnessAnalysis(model1,rbgmModel);
	   	 RA.RichnessTest1();
	   	 
        MB1=new ModelBuild(nameOnt2);	  	
         MB1.build();
         model2=MB1.getModel();
         rbgm2=MB1.getRBGModel();
         owl2=MB1.getOWLModel();
         //CmpGeneralAnalyzing1 GA2=new  CmpGeneralAnalyzing1(model2);
         GA=new GeneralAnalysis(model2);
    	 GA.computeStatics();
    	 GA.GeneralTest2();
    	 CA=new ConsistencyAnalysis(owl2);
         try {
	   		  double start=System.currentTimeMillis();
	   		  CA.ConsistencyTest1();
	   		  double end1=System.currentTimeMillis();
	   		  OPATgui.ConsistencyTextArea2.append("\n"+ "The reasoning time: "+(end1-start)*.001 +"\t sec");
	   		} catch (OWLOntologyCreationException e) {
	   			e.printStackTrace();
	   		}
    	  RA=new RichnessAnalysis(model2,rbgm2);
	   	  RA.RichnessTest2();
    	 
	   	 // information of third tab: Richness tab
	   	 
	   	// BuildModel.OntModel = model1; BuildModel.rbgmModel = rbgmModel;
	   	    	
    }

}
