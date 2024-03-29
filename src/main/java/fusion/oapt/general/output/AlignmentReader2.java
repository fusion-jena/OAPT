
package fusion.oapt.general.output;

import java.io.File;
import java.util.Iterator;
import fusion.oapt.model.Node;
import fusion.oapt.model.modelImpl.NodeImpl;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import org.apache.jena.rdf.model.*;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModelSpec;
public class AlignmentReader2
{
    private OntModel model1 = null;
    private OntModel model2 = null;
    private String alignpath = null;

    public AlignmentReader2(String op1, String op2, String ap)
    {
        OntDocumentManager mgr = new OntDocumentManager();
        mgr.setProcessImports(false);
        OntModelSpec spec = new OntModelSpec(OntModelSpec.OWL_MEM);
        spec.setDocumentManager(mgr);
        model1 = ModelFactory.createOntologyModel(spec, null);
        model2 = ModelFactory.createOntologyModel(spec, null);
        model1.read(op1);
        model2.read(op2);
        alignpath = ap;
    }

    public AlignmentReader2(OntModel m1, OntModel m2, String ap)
    {
        model1 = m1;
        model2 = m2;
        alignpath = ap;
    }

    public Alignment read()
    {
        try {
            File file = new File(alignpath);
            SAXReader reader = new SAXReader();
            Document doc = reader.read(file);
            Element root = doc.getRootElement();
            Element align = root.element("Alignment");
            Iterator<?> map = align.elementIterator("map");
            if (!map.hasNext()) {
                return null;
            }
            Alignment alignment = new Alignment();
            while (map.hasNext()) {
                Element cell = ((Element) map.next()).element("Cell");
                if (cell == null) {
                    continue;
                }
                String s1 = cell.element("entity1").attributeValue("resource");
                String s2 = cell.element("entity2").attributeValue("resource");
                Resource e1 = model1.getResource(s1);
                Resource e2 = model2.getResource(s2);
                if (e1 == null || e2 == null) {
                    System.err.println("readError: Cannot find such entity.");
                    continue;
                }
                Node n1 = new NodeImpl(e1), n2 = new NodeImpl(e2);
                double sim = Double.parseDouble(cell.elementText("measure"));
                String rel = cell.elementText("relation");
                Mapping mapping = new Mapping(n1, n2, sim, rel);
                alignment.addMapping(mapping);
            }
            return alignment;
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        }
    }
}
