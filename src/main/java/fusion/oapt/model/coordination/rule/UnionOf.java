
package fusion.oapt.model.coordination.rule;

import fusion.oapt.model.coordination.Coordinator;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;



public class UnionOf implements Coordinator
{
    
    public Model coordinate(Model model)
    {
        ArrayList<Statement> removedStmt = new ArrayList<Statement>();
        ArrayList<Statement> addedStmt = new ArrayList<Statement>();

        String querystr = " PREFIX owl: <http://www.w3.org/2002/07/owl#AllDifferent> " 
        				+ " SELECT ?x ?y WHERE {?x owl:unionOf ?y} ";

        Query query = QueryFactory.create(querystr);
        QueryExecution qe = QueryExecutionFactory.create(query,  model);
        ResultSet results = qe.execSelect();

        for (Iterator<?> iter = results; iter.hasNext();) {
            QuerySolution res = (QuerySolution) iter.next();
            Resource x = (Resource) res.get("x");
            Resource y = (Resource) res.get("y");
            removedStmt.add(model.createStatement(x, OWL.unionOf, y));
            addedStmt.add(model.createStatement(x, RDF.type, OWL.Class));
            Resource rest = y;
            Resource first = null;
            Resource tempRest = y;
            boolean ended = false;
            while (!ended) {
                first = model.getProperty(rest, RDF.first).getResource();
                tempRest = model.getProperty(rest, RDF.rest).getResource();
                if (first == null || rest == null) {
                    ended = true;
                    continue;
                }
                removedStmt.add(model.createStatement(rest, RDF.first, first));
                removedStmt.add(model.createStatement(rest, RDF.rest, tempRest));
                if (!first.equals(RDF.nil)) {
                    addedStmt.add(model.createStatement(first, RDFS.subClassOf, x));
                }
                rest = tempRest;
                if (rest.equals(RDF.nil)) {
                    ended = true;
                }
            }
        }

        for (int i = 0; i < removedStmt.size(); i++) {
            model.remove(removedStmt.get(i));
        }
        for (int i = 0; i < addedStmt.size(); i++) {
            model.add(addedStmt.get(i));
        }
        return model;
    }
}
