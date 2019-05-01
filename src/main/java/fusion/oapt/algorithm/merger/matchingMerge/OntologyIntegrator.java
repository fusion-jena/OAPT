package fusion.oapt.algorithm.merger.matchingMerge;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;



import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.data.Synset;
import net.didion.jwnl.dictionary.Dictionary;




import org.semanticweb.HermiT.Reasoner;
import org.semanticweb.owlapi.reasoner.*;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.RDFXMLOntologyFormat;
import org.semanticweb.owlapi.model.AddImport;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLImportsDeclaration;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.ConsoleProgressMonitor;
import org.semanticweb.owlapi.reasoner.Node;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerConfiguration;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;
import org.semanticweb.owlapi.search.EntitySearcher;
//import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.vocab.OWLRDFVocabulary;


public class OntologyIntegrator {

    //manager keeping all ontologies
    private OWLOntologyManager man;
    //factory for getting/creating elements of ontologies
    private OWLDataFactory factory;
    //input ontology A
    private OWLOntology ontA;
    //input ontology B
    private OWLOntology ontB;
    //output ontology C
    private OWLOntology ontC;
    //prefix managers for ontologies A, B and C respectively
    private DefaultPrefixManager pmA;
    private DefaultPrefixManager pmB;
    private DefaultPrefixManager pmC;
    //container for owl:Thing class
    private OWLClass thing;
    //container for owl:Nothing class
    private OWLClass nothing;
    //reasoners keeped for full hierarchy of ontologies A, B and C respectively
    private OWLReasoner reasonerA;
    private OWLReasoner reasonerB;
    private OWLReasoner reasonerC;
    //dictionary for accessing WordNet
    private Dictionary dict;
    //similarity finder between two classes
    private SimilarityFinder sf;
    //reasoner factory
    private OWLReasonerFactory reasonerFactory;
    //and its configuration
    private OWLReasonerConfiguration config;
    private double equalScore = 0.7;

    public OntologyIntegrator(OWLOntology ontologyA, OWLOntology ontologyB, OWLOntology ontologyC) throws Exception {//String ontologyFile1, String ontologyFile2, String targetFile, String targetIRI) throws OWLOntologyCreationException, Exception {
    	InputStream is = new FileInputStream("resources/file_properties.xml");
    	//String propsFile = "config/file_properties.xml";
        JWNL.initialize(is);//getClass().getClassLoader().getResourceAsStream(propsFile));
        
        man = OWLManager.createOWLOntologyManager();
        factory = man.getOWLDataFactory();

        this.ontA = ontologyA;
        this.ontB = ontologyB;

        pmA = new DefaultPrefixManager(ontA.getOntologyID().getOntologyIRI() + "#");//"http://www.test.pl/ontA.owl#");
        pmB = new DefaultPrefixManager(ontB.getOntologyID().getOntologyIRI() + "#");//"http://www.test.pl/ontB.owl#");

        reasonerFactory = new Reasoner.ReasonerFactory();
        ConsoleProgressMonitor progressMonitor = new ConsoleProgressMonitor();
        config = new SimpleConfiguration(progressMonitor);

        reasonerA = reasonerFactory.createReasoner(ontA);//, config
        reasonerA.precomputeInferences();
        Node<OWLClass> topNode = reasonerA.getTopClassNode();
        printClassTree(topNode, reasonerA, 0, pmA);
        System.out.println("reasonerA initiated and realized");

        reasonerB = reasonerFactory.createReasoner(ontB);//, config
        reasonerB.precomputeInferences();
        topNode = reasonerB.getTopClassNode();
        printClassTree(topNode, reasonerB, 0, pmB);
        System.out.println("reasonerB initiated and realized");

        thing = factory.getOWLThing();

        Optional<IRI> ontCIRI = ontologyC.getOntologyID().getOntologyIRI();//.get();
        pmC = new DefaultPrefixManager(ontCIRI.toString() + "#");

       
       // OWLImportsDeclaration importA = man.getOWLDataFactory().getOWLImportsDeclaration(ontA.getOntologyID().getOntologyIRI());//man.getOntologyDocumentIRI(ontA));
       // OWLImportsDeclaration importB = man.getOWLDataFactory().getOWLImportsDeclaration(ontB.getOntologyID().getOntologyIRI());//man.getOntologyDocumentIRI(ontB));

        
        ontC = ontologyC;//man.createOntology(ontCIRI);

       // man.applyChange(new AddImport(ontC, importA));
       // man.applyChange(new AddImport(ontC, importB));

        dict = Dictionary.getInstance();
        sf = new SimilarityFinder("shef.nlp.wordnet.similarity.Lin");
        System.out.println();
    }

    public void go() throws JWNLException, Exception {
        combineOntologies();

//      printing the resulting ontology
        reasonerC = reasonerFactory.createReasoner(ontC);//, config
        reasonerC.precomputeInferences();
        Node<OWLClass> topNode = reasonerC.getTopClassNode();
        printClassTree(topNode, reasonerC, 0, pmC);
//        reasonerC = PelletReasonerFactory.getInstance().createReasoner(ontC);
//        reasonerC.getKB().realize();
//        reasonerC.getKB().printClassTree();

    }

    public OWLOntology combineOntologies() throws JWNLException, Exception {
        combineSubTrees(thing, thing, thing, reasonerA, reasonerB);
        return ontC;
    }

    private OWLClass mergeConcepts(OWLClass cls_A, OWLClass cls_B, OWLClass root_Ca, OWLClass root_Cb, OWLReasoner clsAReasoner, OWLReasoner clsBReasoner) throws JWNLException, Exception {
        System.out.println(cls_A.getIRI().getFragment() + " is equal to " + cls_B.getIRI().getFragment());
        //merge and copy consepts to the new location
        //currently identical concepts are being copied into
        //target intology and connected with equivalent property
        OWLAxiom axiom = factory.getOWLSubClassOfAxiom(cls_A, root_Ca);
        man.addAxiom(ontC, axiom);
        axiom = factory.getOWLSubClassOfAxiom(cls_B, root_Cb);
        man.addAxiom(ontC, axiom);
        axiom = factory.getOWLEquivalentClassesAxiom(cls_A, cls_B);
        man.addAxiom(ontC, axiom);

        NodeSet<OWLNamedIndividual> individualsOfA = clsAReasoner.getInstances(cls_A, true);
        NodeSet<OWLNamedIndividual> individualsOfB = clsBReasoner.getInstances(cls_B, true);

        checkAndMergeIndividuals(individualsOfA, individualsOfB);

        return null;
    }

    /**
     * Checks whether two given sets of individuals,, coming from two different ontologies,
     * contains similar individuals. If such pair is found than an OWLSameIndividualsAxiom
     * is created and added to output ontology.
        */
    private void checkAndMergeIndividuals(NodeSet<OWLNamedIndividual> individualsOfA, NodeSet<OWLNamedIndividual> individualsOfB) throws JWNLException, Exception {
//        Iterator itA = individualsOfA.iterator();
//        while (itA.hasNext()) {
        for (Node<OWLNamedIndividual> sameIndOfA : individualsOfA) {
//            Node<OWLNamedIndividual> sameIndOfA = (Node<OWLNamedIndividual>) itA.next();
            OWLNamedIndividual indA = sameIndOfA.getRepresentativeElement();
            String nameOfA = indA.getIRI().getFragment();

//            Iterator itB = individualsOfB.iterator();
//            while (itB.hasNext()) {
//                Node<OWLNamedIndividual> sameIndOfB = (Node<OWLNamedIndividual>) itB.next();
            for (Node<OWLNamedIndividual> sameIndOfB : individualsOfB) {
                OWLNamedIndividual indB = sameIndOfB.getRepresentativeElement();
                String nameOfB = indB.getIRI().getFragment();

                //tokenize individuals names
                String[] aStrings = (String[]) JWNLUtils.splitStringBySpaces(
                        JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(nameOfA)));
                String[] bStrings = (String[]) JWNLUtils.splitStringBySpaces(
                        JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(nameOfB)));

                double indSim = JWNLUtils.compareTexts(aStrings, bStrings);

                System.out.println("Individual score: " + indSim + " " + nameOfA + " and " + nameOfB);

                if (indSim == 1.0) {
                    createSameIndividualsAxiom(indA, indB);
                } else if (indSim >= equalScore) {

                    //if compared terms are single words
                    if ((aStrings.length == 1) && (bStrings.length == 1)) {

                        //we calculate best semantic similarity once again
                        BestSimValueContainer bsvc = sf.computeBestSimilarity(aStrings[0], bStrings[0], POS.NOUN);

                        //is meanings for both terms were found in WordNet we check
                        //whether terms are siblings or in parent-child relation
                        if ((bsvc.getBestSenseOfFirst() != -1) && (bsvc.getBestSenseOfSecond() != -1)) {  //found in WordNet
                            Synset start = dict.getIndexWord(POS.NOUN, aStrings[0]).getSense(bsvc.getBestSenseOfFirst());
                            Synset end = dict.getIndexWord(POS.NOUN, bStrings[0]).getSense(bsvc.getBestSenseOfSecond());
                            System.out.println("Individual score >= " + equalScore + " (" + JWNLUtils.checkRelationsBetweenSynsets(start, end) + "): "
                                    + nameOfA + " and " + nameOfB);
                            if (JWNLUtils.checkRelationsBetweenSynsets(start, end) == JWNLUtils.EQUAL) {
                                createSameIndividualsAxiom(indA, indB);
                            }
                        } else {
                            createSameIndividualsAxiom(indA, indB);
                        }
                    } else {
                        createSameIndividualsAxiom(indA, indB);
                    }
                }
            }
        }
    }

    /**
     * Helper method that creates OWLSameIndividualsAxiom for given two individuals and adds it to output ontology
     * @param indA - first of the two individuals
     * @param indB - second of the two individuals
     */
    private void createSameIndividualsAxiom(OWLIndividual indA, OWLIndividual indB) {
        System.out.println("Merging individuals " + indA.asOWLNamedIndividual().getIRI().getFragment() + " and " + indA.asOWLNamedIndividual().getIRI().getFragment());
        Set<OWLIndividual> set = new HashSet<OWLIndividual>();
        set.add(indA);
        set.add(indB);
        OWLAxiom axiom = factory.getOWLSameIndividualAxiom(set);
        man.addAxiom(ontC, axiom);
    }

    /**
     * Compares two given classes
     * @param clsA - first class to be compared
     * @param clsB - second class to be compared
     * @return result of comparison
     */
    private int compareConcepts(OWLClass clsA, OWLClass clsB) throws JWNLException, Exception {

        String lemma_A = clsA.getIRI().getFragment();
        String lemma_B = clsB.getIRI().getFragment();

        //tokenize class names
        String[] aStrings = (String[]) JWNLUtils.splitStringBySpaces(
                JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(lemma_A)));
        String[] bStrings = (String[]) JWNLUtils.splitStringBySpaces(
                JWNLUtils.replaceConncetors(JWNLUtils.normalizeCasing(lemma_B)));

        //compute similarity between classes
        double score = JWNLUtils.compareTexts(aStrings, bStrings);

        if (score == 1.0) {
            //synonyms
            System.out.println("Score 1.0: " + lemma_A + " and " + lemma_B);
            return JWNLUtils.EQUAL;
        } else if (score >= equalScore) {

            //if compared terms are single words
            if ((aStrings.length == 1) && (bStrings.length == 1)) {

                //we calculate best semantic similarity once again
                BestSimValueContainer bsvc = sf.computeBestSimilarity(aStrings[0], bStrings[0], POS.NOUN);

                //is meanings for both terms were found in WordNet we check
                //whether terms are siblings or in parent-child relation
                if ((bsvc.getBestSenseOfFirst() != -1) && (bsvc.getBestSenseOfSecond() != -1)) {  //found in WordNet
                    Synset start = dict.lookupIndexWord(POS.NOUN, aStrings[0]).getSense(bsvc.getBestSenseOfFirst());
                    Synset end = dict.lookupIndexWord(POS.NOUN, bStrings[0]).getSense(bsvc.getBestSenseOfSecond());
                    System.out.println("Score " + score + " >= " + equalScore + " (" + JWNLUtils.checkRelationsBetweenSynsets(start, end) + "): " + lemma_A + " and " + lemma_B);
                    return JWNLUtils.checkRelationsBetweenSynsets(start, end);
                } else {
                    //no meaning for at least one of the words means Levenstein similarity
                    System.out.println("Score " + score + " >= " + equalScore + " (EQUAL): " + lemma_A + " and " + lemma_B);
                    return JWNLUtils.EQUAL;
                }
            } else {
                //term composed of multiple words, no parent-child relation can be calculated
                System.out.println("Score " + score + " >= " + equalScore + " (EQUAL2): " + lemma_A + " and " + lemma_B);
                return JWNLUtils.EQUAL;
            }
        } else {
            //terms are not similar or similarity cannot be calculated
            //checks of comments and relations need to be done
            System.out.println("Score " + score + " < " + equalScore + " (DIFFERENT): " + lemma_A + " and " + lemma_B);
            System.out.println("Calculating comments and structural similarity");


            //reading comments
            Iterator<OWLAnnotation> commentsOfA = EntitySearcher.getAnnotationObjects(clsA, ontA).iterator();//clsA.getAnnotations(ontA, factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()));//EntitySearcher.getAnnotationObjects(clsA, ontA);//
            Iterator<OWLAnnotation> commmentsOfB = EntitySearcher.getAnnotationObjects(clsB, ontB).iterator();//clsB.getAnnotations(ontB, factory.getOWLAnnotationProperty(OWLRDFVocabulary.RDFS_COMMENT.getIRI()));//EntitySearcher.getAnnotationObjects(clsB, ontB);//

            //printing comments
//            System.out.println("Class " + clsA.getIRI().getFragment() + " has " + commentsOfA.size() + " comments:");
//            for (OWLAnnotation oAn : commentsOfA) {
//                System.out.println(((OWLLiteral) oAn.getValue()).getLiteral());
//            }

//            System.out.println("Class " + clsB.getIRI().getFragment() + " has " + commmentsOfB.size() + " comments:");
//            for (OWLAnnotation oAn : commmentsOfB) {
//                System.out.println(((OWLLiteral) oAn.getValue()).getLiteral());
//            }
              Set<OWLAnnotation> setA=new HashSet<OWLAnnotation>();
              while(commentsOfA.hasNext())
              {
            	  setA.add(commentsOfA.next());
              }
              Set<OWLAnnotation> setB=new HashSet<OWLAnnotation>();
              while(commmentsOfB.hasNext())
              {
            	  setB.add(commmentsOfB.next());
              }
              double commentSim = 0;

            //calculating maximum similarity by comparing every comment from both concepts
            for (OWLAnnotation commentOfA : setA) {
                for (OWLAnnotation commentOfB : setB) {
                    String aString = ((OWLLiteral) commentOfA.getValue()).getLiteral();
                    String bString = ((OWLLiteral) commentOfB.getValue()).getLiteral();
                    double sim = JWNLUtils.compareTexts(aString, bString);
                    if (sim > commentSim) {
                        commentSim = sim;
                    }
                }
            }

            System.out.println("commentSim " + commentSim);


            //number of relations connected to class A
            int subclassesOfA = EntitySearcher.getSubClasses(clsA, ontA).toArray().length;//clsA.getSubClasses(ontA).size();
            int superclassesOfA = EntitySearcher.getSuperClasses(clsA, ontA).toArray().length;//clsA.getSuperClasses(ontA).size();
            int disjointsOfA =   EntitySearcher.getDisjointClasses(clsA, ontA).toArray().length;//clsA.getDisjointClasses(ontA).size();
            int equivalentsOfA = EntitySearcher.getEquivalentClasses(clsA, ontA).toArray().length;//clsA.getEquivalentClasses(ontA).size();
            int individualsOfA =  EntitySearcher.getIndividuals(clsA, ontA).toArray().length;//clsA.getIndividuals(ontA).size();
            
            //number of relations connected to class B
            int subclassesOfB = EntitySearcher.getSubClasses(clsB, ontB).toArray().length;//clsB.getSubClasses(ontB).size();
            int superclassesOfB = EntitySearcher.getSuperClasses(clsB, ontB).toArray().length;//clsB.getSuperClasses(ontB).size();
            int disjointsOfB = EntitySearcher.getEquivalentClasses(clsB, ontB).toArray().length;//
            int equivalentsOfB = EntitySearcher.getEquivalentClasses(clsB, ontB).toArray().length;
            int individualsOfB = EntitySearcher.getIndividuals(clsB, ontB).toArray().length;

            //structural similarity
            double structSim = 0;

            //listing of other relations
            //TODO: check what relations the ontologies have
//            for (OWLAxiom axiom : clsA.getReferencingAxioms(ontA, true)) {
//                if (axiom instanceof OWLObjectPropertyRangeAxiom) {
//
//                }
//            }

            int minSum = Math.min(subclassesOfA, subclassesOfB)
                    + Math.min(superclassesOfA, superclassesOfB)
                    + Math.min(disjointsOfA, disjointsOfB)
                    + Math.min(equivalentsOfA, equivalentsOfB)
                    + Math.min(individualsOfA, individualsOfB);
            int maxSum = Math.max(subclassesOfA, subclassesOfB)
                    + Math.max(superclassesOfA, superclassesOfB)
                    + Math.max(disjointsOfA, disjointsOfB)
                    + Math.max(equivalentsOfA, equivalentsOfB)
                    + Math.max(individualsOfA, individualsOfB);

            //calculating similarity based on comments and structural similarity
            if (maxSum != 0) {
                structSim = 1.0 * minSum / maxSum;
            } else {
                //just to be shure
                structSim = 0;
            }

            System.out.println("structSim " + structSim);

            double secondScore = 0.7 * commentSim + 0.3 * structSim;

            if (secondScore >= equalScore) {
                System.out.println("Second score " + secondScore + " >= " + equalScore + " (EQUAL): " + lemma_A + " and " + lemma_B);
                return JWNLUtils.EQUAL;
            }

            System.out.println("Second score " + secondScore + " < " + equalScore + " (DIFFERENT): " + lemma_A + " and " + lemma_B);
            return JWNLUtils.DIFFERENT;
        }
    }

    /**
     * Adds class cls with all its children as a subclass of class clsC
     * @param cls class which will be added as a subclass
     * @param clsC class to which cls will be added as a subclass
     * @param reasoner reasoner used for cls class
     */
    private void addChildWithSubTree(OWLClass cls, OWLClass clsC, OWLReasoner reasoner) {

        if (cls.isOWLNothing() || clsC.isOWLNothing()) {
            return;
        }

//        OWLClass newCls = factory.getOWLClass(cls.getIRI().getFragment(), pmC);
        Set<OWLAxiom> axioms = new HashSet();
//        axioms.add(factory.getOWLSubClassOfAxiom(newCls, clsC));
        axioms.add(factory.getOWLSubClassOfAxiom(cls, clsC));
//        System.out.println("DodaÅ‚em wraz z poddrzewem klasÄ™ " + cls + " do " + clsC);

//        NodeSet<OWLClass> subClasses = reasoner.getSubClasses(cls, true);
//
//        for (Node<OWLClass> clsNode : subClasses) {
////            addChildWithSubTree(clsNode.getRepresentativeElement(), newCls, reasoner);
//
//            addChildWithSubTree(clsNode.getRepresentativeElement(), cls, reasoner);
//        }

        man.addAxioms(ontC, axioms);
    }

    /**
     * Combines subtrees of root_A and root_B classes and inserts it as a subtree of root_C class
     * @param root_A root of first subtree
     * @param root_B root of second subtree
     * @param root_C root to which combined subtrees should be attached
     */
    public void combineSubTrees(OWLClass root_A, OWLClass root_B, OWLClass root_C, OWLReasoner rootAReasoner, OWLReasoner rootBReasoner) throws JWNLException, Exception {
//        reasonerA = reasonerFactory.createReasoner(ontA);//, config
//        reasonerA.precomputeInferences();
//        reasonerB = reasonerFactory.createReasoner(ontB);//, config
//        reasonerB.precomputeInferences();
        NodeSet<OWLClass> subClasses_A = rootAReasoner.getSubClasses(root_A, true);
        NodeSet<OWLClass> subClasses_B = rootBReasoner.getSubClasses(root_B, true);

        for (Node<OWLClass> clsNode_A : subClasses_A) {
            Set<OWLClass> classesInNode_A = clsNode_A.getEntities();
            for (OWLClass cls_A : classesInNode_A) {
//            OWLClass cls_A = clsNode_A.getRepresentativeElement();
//            System.out.println("Class A: " + clsA.getIRI());
                for (Node<OWLClass> clsNode_B : subClasses_B) {
                    Set<OWLClass> classesInNode_B = clsNode_B.getEntities();
                    for (OWLClass cls_B : classesInNode_B) {
//                    OWLClass cls_B = clsNode_B.getRepresentativeElement();
//                System.out.println("Class B: " + clsB.getIRI());

                        //jezeli ktorys z laczonych wezlow nie ma podklas
                        if (cls_A.isOWLNothing()) {
                            addChildWithSubTree(cls_B, root_B, rootBReasoner);
                        } else if (cls_B.isOWLNothing()) {
                            addChildWithSubTree(cls_A, root_A, rootAReasoner);
                        } else {
                            int result = compareConcepts(cls_A, cls_B);

                            if (result == JWNLUtils.EQUAL) {
                                //zastanowic sie
                                mergeConcepts(cls_A, cls_B, root_A, root_B, rootAReasoner, rootBReasoner);
                                combineSubTrees(cls_A, cls_B, cls_A, rootAReasoner, rootBReasoner);
                            } else if ((result == JWNLUtils.DIFFERENT) || (result == JWNLUtils.SIBLINGS)) {
                                addChildWithSubTree(cls_A, root_A, rootAReasoner);
                                addChildWithSubTree(cls_B, root_B, rootBReasoner);
                            } else if (result == JWNLUtils.FIRST_MORE_GENERAL_THAN_SECOND) {
//                    OWLClass cls_C = factory.getOWLClass(cls_A.getIRI().getFragment(), pmC);
//                    OWLAxiom axiom = factory.getOWLSubClassOfAxiom(cls_C, root_C);
                                OWLAxiom axiom = factory.getOWLSubClassOfAxiom(cls_A, root_C);
                                //TODO: how to merge individuals here? For now I'm for leaving it as it is
                                man.addAxiom(ontC, axiom);
                                placeNodeInSubTreeOfRoot(cls_B, cls_A, rootBReasoner, rootAReasoner);
                            } else if (result == JWNLUtils.SECOND_MORE_GENERAL_THAN_FIRST) {
//                    OWLClass cls_C = factory.getOWLClass(cls_B.getIRI().getFragment(), pmC);
//                    OWLAxiom axiom = factory.getOWLSubClassOfAxiom(cls_C, root_C);
                                OWLAxiom axiom = factory.getOWLSubClassOfAxiom(cls_B, root_C);
                                //TODO: how to merge individuals here? For now I'm for leaving it as it is
                                man.addAxiom(ontC, axiom);

                                placeNodeInSubTreeOfRoot(cls_A, cls_B, rootAReasoner, rootBReasoner);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Finds best place in subtree of root for class node
     * @param node class for which placement in root's subtree should be find
     * @param root class in which subtree place for node should be found
     * @param nodeReasoner reasoner responsible for inferred hierarchy of node class
     * @param rootReasoner reasoner responsible for inferred hierarchy of root class
     */
    private void placeNodeInSubTreeOfRoot(OWLClass node, OWLClass root, OWLReasoner nodeReasoner, OWLReasoner rootReasoner) throws JWNLException, Exception {

//        OWLClass node_C = factory.getOWLClass(node.getIRI().getFragment(), pmC);
//        OWLClass root_C = factory.getOWLClass(root.getIRI().getFragment(), pmC);

        NodeSet<OWLClass> children = rootReasoner.getSubClasses(root, true);

        System.out.println("Dodaje do: " + root);

        for (Node<OWLClass> childNode : children) {
            OWLClass child = childNode.getRepresentativeElement();
            System.out.println("Rozwazam: " + node + " oraz " + child);

            if (root.isOWLNothing()) {
                return;
            }

            //brak potomkow, dodajemy po prostu kalse jako podklase
            if (child.isOWLNothing()) {
                addChildWithSubTree(node, root, nodeReasoner);
            } else {

                int result = compareConcepts(node, child);

                if (result == JWNLUtils.EQUAL) {
                    mergeConcepts(node, child, root, root, nodeReasoner, rootReasoner);
                    //temu sie przyjzec, tu moze byc jakis babol
                    combineSubTrees(node, child, root, nodeReasoner, rootReasoner);
                } else if ((result == JWNLUtils.DIFFERENT) || (result == JWNLUtils.SIBLINGS)) {
                    addChildWithSubTree(node, root, nodeReasoner);
                    addChildWithSubTree(child, root, rootReasoner);
                } else if (result == JWNLUtils.FIRST_MORE_GENERAL_THAN_SECOND) {
                    OWLAxiom axiom = factory.getOWLSubClassOfAxiom(node, root);
                    man.addAxiom(ontC, axiom);

                    placeNodeInSubTreeOfRoot(child, node, rootReasoner, nodeReasoner);
                } else if (result == JWNLUtils.SECOND_MORE_GENERAL_THAN_FIRST) {
//                OWLClass child_C = factory.getOWLClass(child.getIRI().getFragment(), pmC);
                    OWLAxiom axiom = factory.getOWLSubClassOfAxiom(child, root);
                    man.addAxiom(ontC, axiom);

                    placeNodeInSubTreeOfRoot(node, child, nodeReasoner, rootReasoner);
                }
            }
        }
    }

    public static void printClassTree(Node<OWLClass> parent, OWLReasoner reasoner, int depth, DefaultPrefixManager pm) {
        // We don't want to printClassTree out the bottom node (containing owl:Nothing and unsatisfiable classes)
        // because this would appear as a leaf node everywhere
        if (parent.isBottomNode()) {
            return;
        }
        // Print an indent to denote parent-child relationships
        printIndent(depth);
        // Now printClassTree the node (containing the child classes)
        printNode(parent, pm);
        for (Node<OWLClass> child : reasoner.getSubClasses(parent.getRepresentativeElement(), true)) {
            // Recurse to do the children.  Note that we don't have to worry about cycles as there
            // are non in the inferred class hierarchy graph - a cycle gets collapsed into a single
            // node since each class in the cycle is equivalent.
            printClassTree(child, reasoner, depth + 1, pm);
        }
    }

    private static void printIndent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }
    }

    private static void printNode(Node<OWLClass> node, DefaultPrefixManager pm) {
        // Print out a node as a list of class names in curly brackets
        System.out.print("{");
        for (Iterator<OWLClass> it = node.getEntities().iterator(); it.hasNext();) {
            OWLClass cls = it.next();
            // User a prefix manager to provide a slightly nicer shorter name
            System.out.print(pm.getShortForm(cls));
            if (it.hasNext()) {
                System.out.print(" ");
            }
        }
        System.out.println("}");
    }

    public void testCos() {

        NodeSet<OWLClass> subClasses_A = reasonerA.getSubClasses(thing, true);
        NodeSet<OWLClass> subClasses_B = reasonerB.getSubClasses(thing, true);

        for (Node<OWLClass> clsNode_A : subClasses_A) {
            OWLClass cls_A = clsNode_A.getRepresentativeElement();
            System.out.println("Class A: " + cls_A.getIRI());

            if (cls_A.isOWLNothing()) {
                System.out.println("Nothing");
            } else {
                NodeSet<OWLClass> subsubClasses_A = reasonerA.getSubClasses(cls_A, true);

                for (Node<OWLClass> subClsNode_A : subsubClasses_A) {
                    OWLClass subCls_A = subClsNode_A.getRepresentativeElement();
                    System.out.println("SubClass A: " + subCls_A.getIRI());

                    NodeSet<OWLClass> subsubsubClasses_A = reasonerA.getSubClasses(subCls_A, true);

                    for (Node<OWLClass> subSubClsNode_A : subsubsubClasses_A) {
                        OWLClass subsubCls_A = subSubClsNode_A.getRepresentativeElement();
                        System.out.println("SubSubClass A: " + subsubCls_A.getIRI());
                    }
                }
            }
        }

        for (Node<OWLClass> clsNode_B : subClasses_B) {
            OWLClass cls_B = clsNode_B.getRepresentativeElement();
            System.out.println("Class B: " + cls_B.getIRI());

            if (cls_B.isOWLNothing()) {
                System.out.println("Nothing");
            } else {
                NodeSet<OWLClass> subsubClasses_B = reasonerB.getSubClasses(cls_B, true);

                for (Node<OWLClass> subClsNode_B : subsubClasses_B) {
                    OWLClass subCls_B = subClsNode_B.getRepresentativeElement();
                    System.out.println("SubClass B: " + subCls_B.getIRI());

                    NodeSet<OWLClass> subsubsubClasses_B = reasonerA.getSubClasses(subCls_B, true);

                    for (Node<OWLClass> subSubClsNode_B : subsubsubClasses_B) {
                        OWLClass subsubCls_B = subSubClsNode_B.getRepresentativeElement();
                        System.out.println("SubSubClass B: " + subsubCls_B.getIRI());
                    }
                }
            }
        }
    }
}

