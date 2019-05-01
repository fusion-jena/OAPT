package fusion.oapt.algorithm.merger.matchingMerge;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

public class MatchingMerge {
	  
	static List<File> filesInFolder;
	public MatchingMerge(List<File> files)
	{
		this.filesInFolder=files;
	}
     
	public MatchingMerge()
	{
		
	}
      public static void main(String[] args) 
      {
    	  MatchingMerge MR=new MatchingMerge();
    	  MR.run();
      }
      
     public OWLOntology run()
     {
        
    	 int size=filesInFolder.size();
    	 File file1=filesInFolder.get(0);
    	 OWLOntologyManager man = OWLManager.createOWLOntologyManager();
    	 OWLOntology ontologyA = null,ontologyC = null;
		try {
			ontologyA = man.loadOntologyFromOntologyDocument(file1);
			 ontologyC= man.createOntology();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 
    	
    	 for(int i=1;i<size;i++)
    	 {
    		 File file2=filesInFolder.get(i);
    		 try {
              OWLOntology ontologyB = man.loadOntologyFromOntologyDocument(file2);// 
              ontologyC = man.createOntology(IRI.create("http://www.test.pl/ontC"+i+".owl"));
              OntologyIntegrator oi = new OntologyIntegrator(ontologyA, ontologyB, ontologyC);
             oi.combineOntologies();
             ontologyA=ontologyC;
          } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        }
    }
    	 OWLReasonerFactory reasonerFactory = new Reasoner.ReasonerFactory();
         Optional<IRI> ontCIRI = ontologyC.getOntologyID().getOntologyIRI();//.get();
         DefaultPrefixManager pmC = new DefaultPrefixManager(ontCIRI.toString() + "#");

         OWLReasoner reasonerC = reasonerFactory.createReasoner(ontologyC);//
         reasonerC.precomputeInferences();
         Node<OWLClass> topNode = reasonerC.getTopClassNode();
         OntologyIntegrator.printClassTree(topNode, reasonerC, 0, pmC);

        // man.saveOntology(ontologyC, new RDFXMLOntologyFormat(), IRI.create("file:/home/boing/ontologies/dziedzinowe/testy/01-wynik.owl"));
    	 return ontologyC;
    }
}

