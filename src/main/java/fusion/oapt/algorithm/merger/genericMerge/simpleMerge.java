
package fusion.oapt.algorithm.merger.genericMerge;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.util.OWLOntologyMerger;
import org.semanticweb.owlapi.util.AutoIRIMapper;

public class simpleMerge
{
   
	 static List<File> filesInFolder;
	 String loc;
	 List<File> newFolder;
	 String path;

	public simpleMerge()
	{
		
	}
	public simpleMerge(String pName)
	{
		filesInFolder=new ArrayList<File>();
		newFolder=new ArrayList<File>();
		this.path=pName;
	}
	
	public simpleMerge(List<File> files, String loc)
	{
		this.filesInFolder=files;
		this.loc=loc;
	}
	 
	  /**
	   * Given a list of input ontology file paths,
	   * an output file path, and an output ontology IRI,
	   * merge the inputs into the output with that IRI and write it.
	   *
	   * @param args At least three strings:
	   *   - one or more paths to the input ontology files
	   *   - the path of the merged ontology (output) file
	   *   - the IRI of the merged ontology
	   * @return the merged ontology
	   */
	  public  OWLOntology merge(List<File> args, String out) {
	    String outputIRI = out;
	    String outputPath =out;
	    return merge(args, outputPath, outputIRI);
	  }
	  
	  public  OWLOntology merge(String args, String out) {
		    String outputIRI = out;
		    String outputPath =out;
		    List<File> argsF=new ArrayList<File>();
		    String[] parts = this.path.split("\n");
		    for(int i=0;i<parts.length;i++)
		    {
		    	argsF.add(new File (parts[i]));
		    }
		    return merge(argsF, outputPath, outputIRI);
		  }

	  /**
	   * Given a list of input ontology file paths,
	   * an output file path, and an output ontology IRI,
	   * merge the inputs into the output with that IRI and write it.
	   
	   */
	  public static OWLOntology merge(List<File> paths,
	      String outputPathString, String outputIRIString) {
	    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

	   
	    for(File path: paths) {
	      try {
	        File file = path;//new File(path);
	        File parent = file.getCanonicalFile().getParentFile();
	        manager.addIRIMapper(new AutoIRIMapper(parent, false));
	        manager.loadOntologyFromOntologyDocument(file);
	      } catch (Exception e) {
	        System.out.println("ERROR: Could not load ontology at: " + path);
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	      }
	    }

	    IRI outputPath = IRI.create(new File(outputPathString));
	    IRI outputIRI = IRI.create(outputIRIString);

	    return merge(manager, outputPath, outputIRI);
	  }

	   public static OWLOntology merge(OWLOntologyManager manager,
	      IRI outputPath, IRI outputIRI) {
	    OWLOntology merged = null;
	    
	    // merge
	    try {
	      OWLOntologyMerger merger = new OWLOntologyMerger(manager);
	      merged = merger.createMergedOntology(manager, outputIRI);
	    } catch (OWLOntologyCreationException e) {
	      System.out.println("ERROR: Could not merge ontologyies");
	      System.out.println(e.getMessage());
	    }

	    // write
	    try {
	      manager.saveOntology(merged, outputPath);
	    } catch (Exception e) {
	      System.out.println("ERROR: Could not save ontology to: " + outputPath);
	    }

	    return merged;
	  }
	
}