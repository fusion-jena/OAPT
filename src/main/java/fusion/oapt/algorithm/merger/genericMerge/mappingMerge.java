package fusion.oapt.algorithm.merger.genericMerge;

//Alignment API classes
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;

//Alignment API implementation classes
import fr.inrialpes.exmo.align.impl.method.StringDistAlignment;
import fr.inrialpes.exmo.align.impl.renderer.OWLAxiomsRendererVisitor;

//OWL API
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;

//HermiT
import org.semanticweb.HermiT.Reasoner;

import java.util.List;
//Java standard classes

import java.util.Properties;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;

public class mappingMerge {

	static List<File> filesInFolder;
	
	public mappingMerge()
	{
		
	}
	public mappingMerge(List<File> files)
	{
		this.filesInFolder=files;
	}
 public static void main( String[] args ) {
	try {
	    new mappingMerge().run( );
	} catch ( Exception ex ) {
	    ex.printStackTrace();
	}
 }

 public OWLOntology run( ) 
 {
	
	Alignment al = null;
	URI uri1 = null;
	URI uri2 = null;
	
	//String u1 = "file:ontology1.owl";
	//String u2 = "file:ontology2.owl";
	//String method = "fr.inrialpes.exmo.align.impl.method.StringDistAlignment";
	Properties params = new Properties();
	System.out.println( "\n\n ########## MATCHING ########## " );
	int size=filesInFolder.size();
	File file1=filesInFolder.get(0);
	uri1=URI.create(file1.toURI().toString());
	System.out.println(file1+"\t the files:\t"+uri1.toString());
	AlignmentProcess ap = new StringDistAlignment();
	PrintWriter writer = null;
	File merged = null;
	 for(int i=1;i<size;i++)
	 {
		 File file2=filesInFolder.get(i);
		 uri2=URI.create(file2.toURI().toString());
		 System.out.println(file2+"\t the files:\t"+uri2.toString());
		 try 
		 {
			ap.init( uri1, uri2 );
			params.setProperty("stringFunction","smoaDistance");
			params.setProperty("noinst","1");
			ap.align( (Alignment)null, params );
			al = ap;
			 merged = File.createTempFile( "MyApp-results",".owl");
			 //merged.deleteOnExit();
			 writer = new PrintWriter ( new FileWriter( merged, false ), true );
			  AlignmentVisitor renderer = new OWLAxiomsRendererVisitor( writer );
			  al.render(renderer);
			
			    } catch (AlignmentException ae) { ae.printStackTrace(); } catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	  uri1=merged.toURI();
	    
	}
	System.out.println( " Matched ontologies in "+al+" containing "+al.nbCells()+" correspondences" );
	
	System.out.println( " ***** Merging ontologies ***** " );
	
	// If merged is empty then destroy the file + exit
	System.out.println( "Merged file in "+merged );

	System.out.println( " ***** Testing consistency (and coherency) with HermiT ***** " );
	OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	OWLReasoner reasoner = null;
	OWLOntology ontology = null;

	// Load the ontology 
	try {
	    ontology = manager.loadOntology( IRI.create( "file:"+merged.getPath() ) );
	    reasoner = new Reasoner( null,ontology );
	} catch (OWLOntologyCreationException ooce) {
	    ooce.printStackTrace(); 
	}

	if ( reasoner.isConsistent() ) {
	    System.err.println( "The aligned ontologies are consistent" );
	} else {
	    System.err.println( "The aligned ontologies are inconsistent" );
	}

     return ontology;
	}

}
