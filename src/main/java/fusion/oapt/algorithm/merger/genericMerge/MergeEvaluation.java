package fusion.oapt.algorithm.merger.genericMerge;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.util.AutoIRIMapper;




import com.github.andrewoma.dexx.collection.HashSet;
import com.google.common.collect.Sets;

public class MergeEvaluation {
	public double [] coverage;
	public double redudency;
	OWLOntology source,target, merged;
	ArrayList<OWLOntology> owls;
	List<File> filesInFolder;
	
	public MergeEvaluation()
	{
		coverage=new double[3];
		redudency=0;
		
	}
	
	public MergeEvaluation(OWLOntology s, OWLOntology t, OWLOntology m){
		this.source=s;
		this.target=t;
		this.merged=m;
		coverage=new double[3];
		redudency=0;
	}
	
	public MergeEvaluation(List<File> files, OWLOntology m){
		this.filesInFolder=files;
		this.merged=m;
		coverage=new double[3];
		redudency=0;
	}
	
	public double[] computeCoverage() throws IOException, OWLOntologyCreationException
	{
		double covS=0, covT=0, covA=0;
		int size=filesInFolder.size();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		Set<OWLClass> Mclasses=merged.getClassesInSignature();
		File file=filesInFolder.get(0);
		File parent = file.getCanonicalFile().getParentFile();
	    manager.addIRIMapper(new AutoIRIMapper(parent, false));
	    OWLOntology source=manager.loadOntologyFromOntologyDocument(file);
	    Set<OWLClass> Sclasses=source.getClassesInSignature();
	    double tcov=0;
		for(int i=1;i<filesInFolder.size();i++) {
		      try {
		         file = filesInFolder.get(i);
		         parent = file.getCanonicalFile().getParentFile();
		        manager.addIRIMapper(new AutoIRIMapper(parent, false));
		        OWLOntology target=manager.loadOntologyFromOntologyDocument(file);
		        Set<OWLClass> Tclasses=target.getClassesInSignature();
		        Set<OWLClass> cMT=Sets.intersection(Mclasses, Tclasses);
		        tcov+=cMT.size()/(double)Tclasses.size();
		        //System.out.println(cMT.size()/(double)Tclasses.size()+"\t the coverage:\t"+tcov+"\t"+file.getName());
		      } catch (Exception e) {
		        System.out.println("ERROR: Could not load ontology at: " + file);
		        System.out.println(e.getMessage());
		        e.printStackTrace();
		      }
		    }
		Set<OWLClass> cMS=Sets.intersection(Mclasses,Sclasses);
		covS= cMS.size()/(double)Sclasses.size();
		covT= tcov;
		covA=(covS+covT)/2.0;
		coverage[0]=covS;
		coverage[1]=covT;
		coverage[2]=covA;
		System.out.println("the source covS:\t"+ covS+"\n the target:\t"+covT+"\n the average:\t"+covA);
		return coverage;
	}
	
	public double[] computeCoverage_New() throws IOException, OWLOntologyCreationException
	{
		double covS=0, covT=0, covA=0;
		int size=filesInFolder.size();
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		Set<OWLClass> Mclasses=merged.getClassesInSignature();
		double tcov=0;
		for(int i=0;i<filesInFolder.size();i++) {
		      try {
		         File file = filesInFolder.get(i);
		         File parent = file.getCanonicalFile().getParentFile();
		        manager.addIRIMapper(new AutoIRIMapper(parent, false));
		        OWLOntology owl=manager.loadOntologyFromOntologyDocument(file);
		        Set<OWLClass> Tclasses=owl.getClassesInSignature();
		        Set<OWLClass> cMT=Sets.intersection(Mclasses, Tclasses);
		        tcov+=cMT.size()/(double)Tclasses.size();
		        //System.out.println(cMT.size()+"\t the coverage:\t"+tcov+"\t"+file.getName());
		      } catch (Exception e) {
		        System.out.println("ERROR: Could not load ontology at: ");
		        System.out.println(e.getMessage());
		        e.printStackTrace();
		      }
		    }
		covT= tcov/(double)(size);
		covA=(covS+covT);
		coverage[0]=covS;
		coverage[1]=covT;
		coverage[2]=covA;
		System.out.println("the source covS:\t"+ covS+"\n the target:\t"+covT+"\n the average:\t"+covA);
		return coverage;
	}
	
	public double computeRedudency(int map)
	{
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		int mPaths=getNoPath(merged);
	    int pathes=0;
		for(int i=0;i<filesInFolder.size();i++) {
		      try {
		         File file = filesInFolder.get(i);
		         File parent = file.getCanonicalFile().getParentFile();
		        manager.addIRIMapper(new AutoIRIMapper(parent, false));
		        OWLOntology owl=manager.loadOntologyFromOntologyDocument(file);
		        pathes+=getNoPath(owl);
		        //System.out.println(pathes+"\t the coverage:\t"+"\t"+file.getName());
		      } catch (Exception e) {
		        System.out.println("ERROR: Could not load ontology at: ");
		        System.out.println(e.getMessage());
		        e.printStackTrace();
		      }
		    }
		int MLB=pathes-map;
		double red=MLB/(double)mPaths;
		System.out.println(red+"\t the source covS:\t"+ pathes+"\n the target:\t"+mPaths);
		return red;
	}
	
	private int getNoPath(OWLOntology owl)
	{
		Iterator<OWLClass> it=owl.getClassesInSignature().iterator();
		int num_path=0;
		while(it.hasNext())
		{
			OWLClass oclass=it.next();
			Iterator<OWLClassExpression> subClasses=EntitySearcher.getSubClasses(oclass, owl).iterator();// oclass.getSubClasses(owl);
			if(subClasses!=null)
			{
				Iterator<OWLClassExpression> iter=EntitySearcher.getSuperClasses(oclass, owl).iterator();
				Set<OWLClassExpression> supClasses=null;
				while(iter.hasNext())
				{
					supClasses.add(iter.next());
				}
				if(subClasses!=null)
				num_path+=supClasses.size();
			}
		}
		return num_path;
	}

}
