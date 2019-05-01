package fusion.oapt.generalTest;


import java.io.InputStream;
import java.util.*;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

//import opt.general.model.impl.NodeImpl;
//import opt.general.model.impl.RBGModelImpl;

import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
//import com.wcohen.ss.*;
import com.hp.hpl.jena.util.FileManager;

import fusion.oapt.algorithm.partitioner.hierClust.Cluster;
import fusion.oapt.algorithm.partitioner.hierClust.ClusterGenerator;
import fusion.oapt.algorithm.partitioner.hierClust.Dendrogram;
import fusion.oapt.algorithm.partitioner.hierClust.HierarchicalAlgorithm;
import fusion.oapt.algorithm.partitioner.hierClust.LinkGenerator;


/**
 * Created by IntelliJ IDEA.
 * User: algergawy
 * Date: 26.10.2015
 * Time: 11:13:36
 * To change this template use File | Settings | File Templates.
 */
public class TestHierCluster {

    public static void main(String[] args) {
        //Define data
        String source="D:/sources/bco.owl";
        String target="D:/sources/gfo.owl";
        InputStream testFileIn1 = null,testFileIn2=null;
        try 
        {
      
       testFileIn1 = FileManager.get().open(source);
       testFileIn2 = FileManager.get().open(target);
       } catch(IllegalArgumentException ex) {
          System.out.println( "File not found");
          System.exit(0);
       }
     
     //try to use OWL API
        OWLOntology ont1=null, ont2=null;
        OWLOntologyManager m = OWLManager.createOWLOntologyManager();
        try {
			 ont1 = m.loadOntologyFromOntologyDocument(testFileIn1);
			 ont2=m.loadOntologyFromOntologyDocument(testFileIn2);
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       partition(ont1,ont2);  
             
     }

    public static void partition(OWLOntology ont1, OWLOntology ont2)
    {
    	System.out.println(ont2.getClassesInSignature().size()+"\tthe number of classes \t"+ont1.getClassesInSignature().size());
    	HierarchicalAlgorithm ha=new HierarchicalAlgorithm(ont1);
    	Dendrogram dn1=ha.cluster();
    	//dn1.print(dn1.getBestLevel());
    	 LinkGenerator LG=new LinkGenerator(ont1);
    	 HashMap links=LG.getLinks();
    	 System.out.println("the numbr of links\t"+links.size());
    	 ClusterGenerator CG=new  ClusterGenerator();
    	 CG.init(LG.getNodes(), LG.getSortedLink(), links);
    	 Dendrogram dn=CG.cluster();
    	 List<Cluster> list=dn1.getClustersForLevel(dn1.getBestLevel());
    	 for(int i=0;i<list.size();i++)
    		 System.out.println(i+"\t the number of elements in each cluster \t"+list.get(i).size());
    	 //dn.print(dn.getTopLevel()-2);
    	 System.out.println("the numbr of partitions\t"+dn.getClustersForLevel(dn.getTopLevel()-1).size());
    }



   }
