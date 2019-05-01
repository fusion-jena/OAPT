package fusion.oapt.algorithm.partitioner.SeeCOnt;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.io.StreamDocumentSource;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.MissingImportHandlingStrategy;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyLoaderConfiguration;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import uk.ac.manchester.syntactic_locality.ModuleExtractorManager;
import uk.ac.manchester.syntactic_locality.utils.ReadFile;
import fusion.oapt.algorithm.matcher.string.EditDistance;




public class ModulesGenerator {
	
private OWLOntologyManager externalOntologyManager;
	
	private OWLOntology ontoToModularize;
	
	private IRI extOntoIRI;
	
	private static final String defaultModuleURI = "http://krono.act.uji.es/Links/ontologies/module.owl";
	
	private OWLOntology module;
	
	private String typeOfModule;
		
	private Set<String> signatureNames = new HashSet<String>();
	
	private HashSet<OWLEntity> matchedSignature = new HashSet<OWLEntity>();
	
	private Map<String, OWLEntity> name2entity = new HashMap<String, OWLEntity>();
	
	private Set<OWLEntity> ontologyEntities;
	
		
	//Necessary for second iterations
	//private final URI auxModuleURI = URI.create("http://krono.act.uji.es/Links/ontologies/temporalModule.owl");
	
	private IRI physicalModuleURI;
		
	private ModuleExtractorManager moduleManager;
			
	public  ModulesGenerator(String ontoURIStr, Set<String> fileSignature, String ModuleType, String outputFile)
	{
				
		//LOAD ONTOLOGY
		long init, fin;
		init=Calendar.getInstance().getTimeInMillis();
		extOntoIRI = IRI.create(ontoURIStr.replace("\\", "//"));
		if (!loadExternalOntology(ontoURIStr))
			return;
		
		fin = Calendar.getInstance().getTimeInMillis();
		System.out.println("**Time for Loading Ontology (s): " + (double)((double)fin-(double)init)/1000.0);
		String moduleURIStr=defaultModuleURI;
		
		//Type
		typeOfModule=ModuleType;
		
		this.signatureNames=fileSignature;	
		getMatchedSignature();
		init=Calendar.getInstance().getTimeInMillis();
		//getSignatureFromFile(fileSignature);
		fin = Calendar.getInstance().getTimeInMillis();
		System.out.println("**Time for getting matched signature (s): " + (double)((double)fin-(double)init)/1000.0);
				
		//CREATE MODULE MANAGER
		init=Calendar.getInstance().getTimeInMillis();
		//Parameters: ontology, ModuleType, considerImportsClosure, considerAnnotations
		moduleManager =  new ModuleExtractorManager(ontoToModularize, typeOfModule, true, false, false);
		fin = Calendar.getInstance().getTimeInMillis();
		System.out.println("**Time for creating module manager (s): " + (double)((double)fin-(double)init)/1000.0);
		
		//EXTRACT AND SAVE MODULE
		if (outputFile.startsWith("/"))
			physicalModuleURI = IRI.create("file:" + outputFile.replace("\\", "//"));
		else
			physicalModuleURI = IRI.create("file:/" + outputFile.replace("\\", "//"));
				
		init=Calendar.getInstance().getTimeInMillis();
		module=moduleManager.extractModule(matchedSignature, moduleURIStr);   
		fin = Calendar.getInstance().getTimeInMillis();
		System.out.println("**Time for extracting module (s): " + (double)((double)fin-(double)init)/1000.0);
    	
		init=Calendar.getInstance().getTimeInMillis();
		saveModuleToPhysicalURI();
    	fin = Calendar.getInstance().getTimeInMillis();
		System.out.println("**Time for saving module (s): " + (double)((double)fin-(double)init)/1000.0);
		
		
		//PRINT RESULTS
		init=Calendar.getInstance().getTimeInMillis();
		printModuleData();
		fin = Calendar.getInstance().getTimeInMillis();
		System.out.println("\n**Time for printing results (s): " + (double)((double)fin-(double)init)/1000.0);
		
	}

     public OWLOntology getModule()
     {
    	 return module;
     }
	

	private String getEntityLabel(String uriStr){
		if (uriStr.indexOf("#")>=0)
			return uriStr.split("#")[1];
		return uriStr;
	}
	
	
	
	private boolean loadExternalOntology(String onName) {
		InputStream in=null;
	 	 InputStream fileStream=null;
	 	 if(onName.endsWith(".owl"))
			try {
				in = new FileInputStream(onName);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		 else if(onName.endsWith(".gz"))
	 		{
	 			try {
	 				fileStream = new FileInputStream(new File(onName));
	 				in= new GZIPInputStream(fileStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
	 		}
	 	OWLOntologyLoaderConfiguration config = new OWLOntologyLoaderConfiguration();
	    config = config.setMissingImportHandlingStrategy(MissingImportHandlingStrategy.SILENT);
	    StreamDocumentSource documentSource = new StreamDocumentSource(in);
    	externalOntologyManager = OWLManager.createOWLOntologyManager();
    	try {
    		ontoToModularize = externalOntologyManager.loadOntologyFromOntologyDocument(documentSource, config);// .loadOntology(in);
    		ontologyEntities = ontoToModularize.getSignature();//.getReferencedEntities();//getAllReferencedEntitiesFromOntology();
    		//datafactory = externalOntologyManager.getOWLDataFactory();
    		
    		//Useful structure to get Matched signature
    		for (OWLEntity ent: ontologyEntities) {
    				name2entity.put(ent.getIRI().getShortForm().toString(), ent);   
    		}
    		
    		return true;
    		
    	}
    	catch (Exception e) {
    		System.err.println("Error loading ontology form URI: " + extOntoIRI.toString());
    		e.printStackTrace();
    		ontoToModularize = null;
    		return false;
    	}	
    }
	
	
	
	/**
	 * 
	 * @param fileSignature
	 */
	private void getSignatureFromFile(String fileSignature) {
    	String line;
    	    	    	
    	try {
	    	
			ReadFile reader = new ReadFile(fileSignature);
			line=reader.readLine();
			while (line!=null) {
				
				if (!line.startsWith("#")){
					/*if (line.contains("|")) {
						lineSig=line.split("\\|");
						System.out.println(lineSig[0] + " " + lineSig[1]);
						if (lineSig[1].equals("data")) {
							signature.add(datafactory.getOWLDataProperty(URI.create(extOntoURI.toString() +"#"+ lineSig[0])));
						}	
						else {
							signature.add(datafactory.getOWLObjectProperty(URI.create(extOntoURI.toString() +"#"+ lineSig[0])));
						}
					}
					else {
						signature.add(datafactory.getOWLClass(URI.create(extOntoURI.toString() +"#"+ line)));						
					} //end |*/
					signatureNames.add(line);
				}//end #
	        	line=reader.readLine();
				
			}
			reader.closeBuffer();
			
			//We shoukld match the signature, that is, signature must have the same URI than the entity form the external onto
			getMatchedSignature();
			
    	}
        catch (Exception e){
        	System.out.println("Error reading file: " + fileSignature + "\n" + e.getLocalizedMessage());
        	//e.printStackTrace();
          }
	}
	
	
	
	/**
	 * We need to extract the entitie with the same URI in the ontology.
	 * Notice that the ontology may be differnet to the entity URI
	 *
	 */
	private void getMatchedSignature(){
		
		Set<String> keys = name2entity.keySet();
		
        for (String entSig : signatureNames) {
        	//System.out.println(entSig+"\t entity \t"+keys);
        	if (keys.contains(entSig))        			
        		matchedSignature.add(name2entity.get(entSig));
        	else
        	{
        		Iterator<String> it=keys.iterator();
        		{
        			while(it.hasNext())
        			{
        				String str=it.next();
        				//ISub sim=new ISub();
        				EditDistance sim=new EditDistance();
        				double score=sim.getSimilarity(str,entSig);
        				if(score>0.4)
        				{	matchedSignature.add(name2entity.get(str));
        				   //System.out.println(entSig+"\t entity \t"+keys);
        				}
        			}
        		}
        	}
        	//else 
            	//System.err.println("\tThe entity '" + entSig.toString() + "' has not a correspondence in the external ontology.");
        }
	}
	
	

    private void saveModuleToPhysicalURI() {
    	//OWLOntologyManager ontologyModuleManager = OWLManager.createOWLOntologyManager();
    	
        try {
        	externalOntologyManager.saveOntology(module, new RDFXMLOntologyFormat(), physicalModuleURI);
        }
        catch (Exception e) {
        	System.err.println("Error saving module\n" + e.getLocalizedMessage());
        	e.printStackTrace();
        }
    }
	    
   
  
    private void printModuleData(){
    	
    	System.out.println("\nExtracted " + typeOfModule + " Module for a signature size of " + matchedSignature.size());
    	if (signatureNames.size()>matchedSignature.size())
    		System.err.println("Not all the entities of the signature were matched/aligned with an ontology entity.");
    	System.out.println("Number of Axioms -> Module: " + moduleManager.getNumberOfAxiomsExtractedModule() + " / Whole Ontology: " + moduleManager.getNumberOfAxiomsOntoToModularize());
    	System.out.println("Number of Classes -> Module: " + moduleManager.getNumberOfClassesExtractedModule() + " / Whole Ontology: " + moduleManager.getNumberOfClassesOntoToModularize());
    	System.out.println("Number of Roles -> Module: " + moduleManager.getNumberOfRolesExtractedModule() + " / Whole Ontology: " + moduleManager.getNumberOfRolesOntoToModularize());
    	System.out.println("Number of Individuals -> Module: " + moduleManager.getNumberOfIndividualsExtractedModule() + " / Whole Ontology: " + moduleManager.getNumberOfIndividualsOntoToModularize());
    	
    	double percentSize = (((double)moduleManager.getNumberOfAxiomsExtractedModule()*100.0/(double)(moduleManager.getNumberOfAxiomsOntoToModularize()+1)) +
    			((double)moduleManager.getNumberOfClassesExtractedModule()*100.0/(double)(moduleManager.getNumberOfClassesOntoToModularize()+0.1)) + 
    			((double)moduleManager.getNumberOfRolesExtractedModule()*100.0/(double)(moduleManager.getNumberOfRolesOntoToModularize()+0.1)))/3.0;
    	
    	
    	int decimales = 2;
    	double roundedPercentSize = Math.round(percentSize*Math.pow(10,decimales))/Math.pow(10,decimales);
    	
    	System.out.println("Relative size: " + roundedPercentSize + " % of the ontology");
    }
    
    
    public static void usage(){
    	
    	System.out.println("Usage: The method requires 4 or 5 arguments.\n" +
    			"  java -jar locality_module_extractor.jar Onto_URI_Name Signature_File ModuleType OutputFile [ModuleUri]\n" +
    			"\t Ontology URI i.e.:\n" +
    			"\t\t http://krono.act.uji.es/Links/ontologies/gale_protege.owl\n" +
    			"\t\t file:/tmp/MyOnt.owl\n" +
    			"\t\t ftp://ftp.fao.org/gi/gil/gilws/aims/kos/agrovoc_formats/owl/agrovoc_20050401.owl\n" +
    			"\t File with signature (one entity per line):\n" +
    			"\t\t Juvenile Idiopathic Arthritis\n" +
    			"\t\t Heart\n" +
    			"\t\t hasAttribute\n" +
    			"\t\t # Commented line.\n" +
    			"\t Type of module: 'UPPER_MODULE' or 'UM', 'LOWER_MODULE' or 'LM', 'LOWER_UPPER_MODULE' or 'LUM', 'DUAL_CONCEPTS_MODULE' or 'DCM', 'DUAL_ROLES_MODULE' or 'DRM\n" +
    			"\t\t See report documentation in http://krono.act.uji.es/people/Ernesto/safety-ontology-reuse for more information.\n" +
    			"\t Output Filepath: absolute path of the owl output module.\n" +  
    			"\t Module URI (optional). Default: http://krono.act.uji.es/Links/ontologies/module_Signature_File.owl");
    	
    }
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
		long init, fin;
		System.out.println("starting....");
		init=Calendar.getInstance().getTimeInMillis(); 
		String ontouri="D:/test_modularization/test/test/ado.owl.gz";
        String moduletype[]= new String[]{"UM","LM","LUM","DCM","DRM"};   
        String class4module= "temp/example.txt";//new String[]{"Paper","Review"};
        String op="D:/temp/module.owl";
        //new  ModulesGenerator(ontouri,class4module,moduletype[2],op);
       		
		fin = Calendar.getInstance().getTimeInMillis();
		System.out.println("\nTOTAL TIME (s): " + (double)((double)fin-(double)init)/1000.0);
		
	}



}
