
package fusion.oapt.model.coordination;

import org.apache.jena.rdf.model.Model;


public interface Coordinator
{
    public Model coordinate(Model model);
}
