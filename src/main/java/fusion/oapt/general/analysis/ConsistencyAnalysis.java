
package fusion.oapt.general.analysis;

import java.io.File;
import java.util.Optional;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;

import fusion.oapt.general.gui.OPATgui;


public class ConsistencyAnalysis 
{
	private static OWLOntology owl;
   public ConsistencyAnalysis()
   {
	   
   }
   
   public ConsistencyAnalysis(OWLOntology mod)
   {
	  this.owl=mod; 
   }
	
	public  void ConsistencyTest1() throws OWLOntologyCreationException 
    {   		
    	//File inputfile = new File(OPATgui.NameAddressOnt1);
    	//OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); ;
    	//OWLDataFactory dataFactory = manager.getOWLDataFactory();
    	//OWLOntology yourOntology = manager.loadOntologyFromOntologyDocument(inputfile);
    	Optional<IRI> ontologyIRI = owl.getOntologyID().getOntologyIRI();//   
        
    	
    	OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
    	OWLReasoner reasoner =     (OWLReasoner) reasonerFactory.createNonBufferingReasoner(owl);
    	 OPATgui.ConsistencyTextArea1.append("============  The consistency checker ============ \n");
    	    if(reasoner.isConsistent()){
    	        OPATgui.ConsistencyTextArea1.append("\n"+ontologyIRI+ "\t PASSED the reasoning test using:\t"+reasoner.getReasonerName()+"\n");
    	    }else{
    	        OPATgui.ConsistencyTextArea1.append(OPATgui.NameAddressOnt1+"\t FAILED the consistency test");
    	    }    
    }
    
 //-----------------------------------------------------------------------------------------------------------------------------------------------------------
    public static void ConsistencyTest2() throws OWLOntologyCreationException 
    {   		
    	File inputfile = new File(OPATgui.NameAddressOnt2);
    	OWLOntologyManager manager = OWLManager.createOWLOntologyManager(); ;
    	OWLDataFactory dataFactory = manager.getOWLDataFactory();
    	OWLOntology yourOntology = manager.loadOntologyFromOntologyDocument(inputfile);
    	Optional<IRI> ontologyIRI = yourOntology.getOntologyID().getOntologyIRI();//.get();  
        
    	
    	OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
    	OWLReasoner reasoner =     (OWLReasoner) reasonerFactory.createNonBufferingReasoner(yourOntology);
    	 OPATgui.ConsistencyTextArea2.append("============  The consistency checker ============ \n");
    	    if(reasoner.isConsistent()){
    	        OPATgui.ConsistencyTextArea2.append("\n"+ontologyIRI+ "\t   PASSED the reasoning test using:\t"+reasoner.getReasonerName()+"\n");
    	    }else{
    	        OPATgui.ConsistencyTextArea2.append(OPATgui.NameAddressOnt2+"\t FAILED the consistency test");
    	    }    
    }
    
}
    