package fusion.oapt.algorithm.partitioner.SeeCOnt.moduleExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.StringTokenizer;











//import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAnnotationAssertionAxiom;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import fusion.oapt.algorithm.matcher.string.EditDistance;
import fusion.oapt.algorithm.matcher.string.ISub;
import fusion.oapt.algorithm.matcher.string.WordNetMatcher;
import uk.ac.manchester.cs.owl.owlapi.mansyntaxrenderer.ManchesterOWLSyntaxObjectRenderer;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;
import uk.ac.manchester.cs.owlapi.modularity.SyntacticLocalityModuleExtractor;


public class ModuleExtractor {
	private static String sigParseLog;
	private static SimpleShortFormProvider sf = new SimpleShortFormProvider();
	
    // Parse & tokenize signature file
    public static Set<OWLEntity> getSignature(OWLOntology ontology, BufferedReader file) throws IOException {
        Set<OWLEntity> sig = new HashSet<OWLEntity>();
        OWLOntologyManager man = ontology.getOWLOntologyManager();
        OWLDataFactory df = man.getOWLDataFactory();
        sigParseLog = "";

        Optional<IRI> ontIri = ontology.getOntologyID().getDefaultDocumentIRI();
        
        boolean snomed = false;
        boolean termsFound = false, notFound = false;
        String termsIri = "";
        
        // Check entity IRIs for Snomed concepts 
        loopClasses:
        for(OWLClass c : ontology.getClassesInSignature()) {
        	if(c.getIRI().toString().contains("www.ihtsdo.org")) {
        		snomed = true;
        		termsIri += c.getIRI().toString();
        		termsIri = termsIri.substring(0, termsIri.indexOf(".org/")+5);
        		break loopClasses;
        	}
        }
        
        if(ontIri.toString().contains("www.ihtsdo.org")) {
        	snomed = true;
        	termsIri = ontIri.toString().substring(0, ontIri.toString().indexOf(".org/")+5);
        }
        
        if(snomed) {
        	file.readLine();
            while (file.ready()) {
                String s = file.readLine();
                int pos = s.indexOf("|");
                if(pos < 0) throw new RuntimeException("There is no '|' in:   ");
                
                s = "SCT_" + s.substring(0,pos);
                IRI iri = IRI.create(termsIri + s);
                OWLClass cls = df.getOWLClass(iri);
                if (ontology.containsClassInSignature(iri)) {
                    sig.add(cls);
                    termsFound=true;
                }
                else {
                    sigParseLog += "\nThere is no class " + iri + " in the ontology.";
                    notFound = true;
                }
            }
            
            IRI roleGroupIRI = IRI.create(ontology.getOntologyID().getOntologyIRI() + "RoleGroup");
            OWLObjectProperty roleGroup = df.getOWLObjectProperty(roleGroupIRI);
            
            if (ontology.containsObjectPropertyInSignature(roleGroupIRI)) {
                sig.add(roleGroup);
                sigParseLog += "\nRoleGroup added.";
            }
        }
        else {	// Use StringTokenizer
	        String delimiters = "	|, ";	// Delimiters allowed
	        String strLine = "";
	        while ((strLine = file.readLine()) != null) {
	        	// Skip commented lines
	        	if(!strLine.startsWith("%")) {
	        		if(strLine.contains("%"))
	        			strLine = strLine.substring(0, strLine.indexOf("%"));
	        		
	        		StringTokenizer st = new StringTokenizer(strLine, delimiters, false);
		        	
		        	while(st.hasMoreTokens()) {
		        		String s = st.nextToken();
		        		System.out.println("Token from signature file: " + s );
		        		IRI termIri = findTermIRI(ontology, s,df);
		        		if(termIri!=null){
			            if(ontology.containsClassInSignature(termIri)) {
			            	OWLClass cls = df.getOWLClass(termIri);
			                sig.add(cls); termsFound=true;
			                System.out.println("\tOWL class. IRI: " + termIri.toString());
			            }
			            else if(ontology.containsObjectPropertyInSignature(termIri)) {
			            	OWLObjectProperty prop = df.getOWLObjectProperty(termIri);
			            	sig.add(prop); termsFound=true;
			            	System.out.println("\tOWL object property. IRI: " + termIri.toString());
			            }
			            else if(ontology.containsDataPropertyInSignature(termIri)) {
			            	OWLDataProperty prop = df.getOWLDataProperty(termIri);
			            	sig.add(prop); termsFound=true;
			            	System.out.println("\tOWL data property. IRI: " + termIri.toString());
			            }
			            else {
			                sigParseLog += "\nThere is no term " + termIri + " in the ontology.";
			                System.out.println("\t! The term " + termIri + " is not in the ontology.");
			                notFound = true;
			            }
		        	}}
	        	}
	        }
        }
        sigParseLog += "\n" + sig.size() + " term(s) found. ";
        if(!termsFound && !notFound) sigParseLog += "Signature file is empty!";
        return sig;
    }
    
    public static IRI findTermIRI(OWLOntology ont, String name, OWLDataFactory df) {
    	IRI out = null;
    	IRI tempIRI = IRI.create(name);
    	for(OWLEntity c : ont.getClassesInSignature()){ 
    		if(c.getIRI().equals(tempIRI)) {
    			out = c.getIRI();
    			break;
    		}
    		else{
    			String str=null;
    			for (OWLAnnotationProperty annotation : c.getAnnotationPropertiesInSignature() ) {  
    				  if (((OWLAnnotation) annotation).getValue() instanceof OWLLiteral) { 
    				    OWLLiteral val = (OWLLiteral) ((OWLAnnotation) annotation).getValue();
    				    str=val.getLiteral();
    				    		       
    				      }
    				   }
    			if(str==null)
    			str=getManchesterSyntax(c);
    		  ISub mes=new ISub();
    		 //EditDistance mes=new EditDistance();
    		  WordNetMatcher wn=new WordNetMatcher();
    		//double sim=  wn.compute(str, name);
    		 double sim= mes.getSimilarity(str, name);
    		//System.out.println(str+"\t"+name+"\t"+sim);
    		  if(sim>=0.6) {
    			out = c.getIRI();
    			break;
    		}}
    	}
    	//System.out.println("the URI \t"+out);
    	    	
    	return out;
    }
    
    public static String getSignatureParseLog() {
    	return sigParseLog;
    }

    public static OWLOntology extractModule(Set<OWLEntity> signature, OWLOntology o, String modName, ModuleType moduleType) 
    		throws OWLOntologyCreationException {
        SyntacticLocalityModuleExtractor extractor = new SyntacticLocalityModuleExtractor(o.getOWLOntologyManager(), o, moduleType);
        return extractor.extractAsOntology(signature, IRI.create(modName));
    }
    
	private static String getManchesterSyntax(OWLObject obj) {
		StringWriter wr = new StringWriter();
		ManchesterOWLSyntaxObjectRenderer render = new ManchesterOWLSyntaxObjectRenderer(wr, sf);
		obj.accept(render);
		String str = wr.getBuffer().toString();
		return str;
	}
	
	  // Parse & tokenize signature list
    public static Set<OWLEntity> getSignature(OWLOntology ontology, ArrayList<String> terms)  {
        Set<OWLEntity> sig = new HashSet<OWLEntity>();
        OWLOntologyManager man = ontology.getOWLOntologyManager();
        OWLDataFactory df = man.getOWLDataFactory();
        sigParseLog = "";

        Optional<IRI> ontIri = ontology.getOntologyID().getDefaultDocumentIRI();
        
        boolean snomed = false;
        boolean termsFound = false, notFound = false;
        String termsIri = "";
        
        // Check entity IRIs for Snomed concepts 
        loopClasses:
        for(OWLClass c : ontology.getClassesInSignature()) {   
        	if(c.getIRI().toString().contains("www.ihtsdo.org")) {
        		snomed = true;
        		termsIri += c.getIRI().toString();
        		termsIri = termsIri.substring(0, termsIri.indexOf(".org/")+5);
        		break loopClasses;
        	}
        }
        
      if(ontIri!=null) { if(ontIri.toString().contains("www.ihtsdo.org")) {
        	snomed = true;
        	termsIri = ontIri.toString().substring(0, ontIri.toString().indexOf(".org/")+5);
        }}
        
        if(snomed) {
        	
            for (int i=0; i<terms.size(); i++) {
                String s = terms.get(i);//file.readLine();
                int pos = s.indexOf("|");
                if(pos < 0) throw new RuntimeException("There is no '|' in:   ");
                
                s = "SCT_" + s.substring(0,pos);
                IRI iri = IRI.create(termsIri + s);
                OWLClass cls = df.getOWLClass(iri);
                if (ontology.containsClassInSignature(iri)) {
                    sig.add(cls);
                    termsFound=true;
                }
                else {
                    sigParseLog += "\nThere is no class " + iri + " in the ontology.";
                    notFound = true;
                }
            }
            
            IRI roleGroupIRI = IRI.create(ontology.getOntologyID().getOntologyIRI() + "RoleGroup");
            OWLObjectProperty roleGroup = df.getOWLObjectProperty(roleGroupIRI);
            
            if (ontology.containsObjectPropertyInSignature(roleGroupIRI)) {
                sig.add(roleGroup);
                sigParseLog += "\nRoleGroup added.";
            }
        }
        else {	// Use StringTokenizer
	        String delimiters = "	|, ";	// Delimiters allowed
	        String strLine = "";
	        for (int j=0; j<terms.size();j++) {
	        	strLine=terms.get(j);
	        	// Skip commented lines
	        	if(!strLine.startsWith("%")) {
	        		if(strLine.contains("%"))
	        			strLine = strLine.substring(0, strLine.indexOf("%"));
	        		
	        		StringTokenizer st = new StringTokenizer(strLine, delimiters, false);
		        	
		        	while(st.hasMoreTokens()) {
		        		String s = st.nextToken();
		        		//System.out.println("Token from signature file: " + s );
		        		IRI termIri = findTermIRI(ontology, s,df);
		        		if(termIri!=null){
			            if(ontology.containsClassInSignature(termIri)) {
			            	OWLClass cls = df.getOWLClass(termIri);
			                sig.add(cls); termsFound=true;
			               // System.out.println("\tOWL class. IRI: " + termIri.toString());
			            }
			            else if(ontology.containsObjectPropertyInSignature(termIri)) {
			            	OWLObjectProperty prop = df.getOWLObjectProperty(termIri);
			            	sig.add(prop); termsFound=true;
			            	//System.out.println("\tOWL object property. IRI: " + termIri.toString());
			            }
			            else if(ontology.containsDataPropertyInSignature(termIri)) {
			            	OWLDataProperty prop = df.getOWLDataProperty(termIri);
			            	sig.add(prop); termsFound=true;
			            	//System.out.println("\tOWL data property. IRI: " + termIri.toString());
			            }
			            else {
			                sigParseLog += "\nThere is no term " + termIri + " in the ontology.";
			                //System.out.println("\t! The term " + termIri + " is not in the ontology.");
			                notFound = true;
			            }
		        	}}
	        	}
	        }
        }
        sigParseLog += "\n" + sig.size() + " term(s) found. ";
        if(!termsFound && !notFound) sigParseLog += "Signature file is empty!";
        return sig;
    }
}