
package fusion.oapt.model.coordination.rule;

import fusion.oapt.model.Constant;
import fusion.oapt.model.coordination.Coordinator;

import java.util.ArrayList;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;

public class Redefinition implements Coordinator
{
    public Model coordinate(Model model)
    {
        ArrayList<Statement> removedStmt = new ArrayList<Statement>();

        StmtIterator stmts = model.listStatements();
        while (stmts.hasNext()) {
            Statement stmt = stmts.nextStatement();
            Resource subject = stmt.getSubject();
            if (Constant.RDF_NS.equals(subject.getNameSpace()) ||
            		Constant.RDFS_NS.equals(subject.getNameSpace()) || 
            		Constant.OWL_NS.equals(subject.getNameSpace())) {
                removedStmt.add(stmt);
            }
        }

        for (int i = 0; i < removedStmt.size(); i++) {
            model.remove(removedStmt.get(i));
        }
        return model;
    }
}
