package  fusion.oapt.general.ontoTograph;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import agg.xt_basis.GraGra;
import agg.xt_basis.Graph;
import agg.xt_basis.TypeGraph;


import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.*;
import org.apache.jena.ontology.OntDocumentManager;

public class Test 
{	
	public static void main(String[] args) throws Exception
	{
		 GraGra graphGrammar;
				  
		/*********** Load ontology  *************/	
		 
		String name="D:/result/cmt.owl";
		String fileName=new File(name).getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0) {
		    fileName = fileName.substring(0, pos);
		}
		InputStream   in1 = new FileInputStream(new File( name));
		OntDocumentManager mgr = new OntDocumentManager();
    	OntModel model = ModelFactory.createOntologyModel();
 	    model.read(in1,null);	
 		model.prepare();
         if(model!=null)
        	 System.out.println("this is the model\t"+model.toString());
        /********* Load the meta-model **********/
        String fmtatOnto= "resources/metamodelOwl.ggx";  
     	graphGrammar =  new GraGra(true);		
     	graphGrammar.setFileName("/temp/"+name);		
     	graphGrammar.load(fmtatOnto);       
     	
     	if (graphGrammar == null) 			
     		System.out.println("Grammar:  " + fmtatOnto + "   inexistant! ");  
     	
     	/********* Start process  of transformation ********/
     	long begin = System.currentTimeMillis();
     	
     	Graph typeGrap=graphGrammar.getTypeGraph();
     	OntologyToGraph og=new OntologyToGraph(model,typeGrap,fileName);
     	  	
     	long end = System.currentTimeMillis();
     	float time = ((float) (end-begin)) / 1000f;
     	System.out.println("\nExecution time of transformation is : "+ time);	
     	
	}
}
