
package fusion.oapt.model.coordination.rule;

import fusion.oapt.model.Constant;
import fusion.oapt.model.coordination.Coordinator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

import fusion.oapt.model.coordination.rule.BinaryTreeNode;

public class List implements Coordinator
{
    public Model coordinate(Model model)
    {
        ArrayList<Statement> removedStmts = new ArrayList<Statement>();
        ArrayList<Statement> addedStmts = new ArrayList<Statement>();

        //HashMap<Resource, BinaryTreeNode> lists = new HashMap<Resource, BinaryTreeNode>();
        LinkedHashMap<Resource, BinaryTreeNode> lists = new LinkedHashMap<Resource, BinaryTreeNode>(); // new samira

        String querystr = " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " 
                		+ " SELECT ?x ?y ?z ?type WHERE " 
                		+ " {?x rdf:type ?type. ?x rdf:first ?y. ?x rdf:rest ?z.} ";

        Query query = QueryFactory.create(querystr);
        QueryExecution qe = QueryExecutionFactory.create(query,  model);
        ResultSet results = qe.execSelect();

        for (Iterator<?> iter = results; iter.hasNext();) {
            QuerySolution res = (QuerySolution) iter.next();
            Resource x = (Resource) res.get("x");
            RDFNode y = (RDFNode) res.get("y");
            Resource z = (Resource) res.get("z");
            Resource nodeType = (Resource) res.get("type");
            removedStmts.add(model.createStatement(x, RDF.type, nodeType));
            removedStmts.add(model.createStatement(x, RDF.first, y));
            removedStmts.add(model.createStatement(x, RDF.rest, z));

            BinaryTreeNode tempX = null;
            BinaryTreeNode tempZ = null;

            if (!lists.containsKey(x)) {
                tempX = new BinaryTreeNode(x);
                lists.put(x, tempX);
            } else {
                tempX = lists.get(x);
            }
            tempX.setRight(y);
            tempX.setNodeType(nodeType);
            if (z.toString().equals(Constant.RDF_NS + "nil")) {
                tempX.setLeft(null);
            } else {
                if (!lists.containsKey(z)) {
                    tempZ = new BinaryTreeNode(z);
                    lists.put(z, tempZ);
                } else {
                    tempZ = lists.get(z);
                }
                tempX.setLeft(tempZ);
                tempZ.setFather(tempX);
            }
        }
        Iterator<BinaryTreeNode> listNodes = lists.values().iterator();
        while (listNodes.hasNext()) {
            BinaryTreeNode node = listNodes.next();
            if (node.getLeft() == null) {
                ArrayList<RDFNode> members = new ArrayList<RDFNode>();
                members.add((RDFNode) node.getRight());
                while (node.getFather() != null) {
                    node = node.getFather();
                    members.add((RDFNode) node.getRight());
                }
                for (int i = 0; i < members.size(); i++) {
                    addedStmts.add(model.createStatement((Resource) node.getValue(), 
                            RDFS.member, (RDFNode) members.get(i)));
                }
                if (!node.getNodeType().toString().equals(Constant.RDF_NS + "List")) {
                    addedStmts.add(model.createStatement((Resource) node.getValue(),
                            RDF.type, (Resource) node.getNodeType()));
                }
            }
        }

        for (int i = 0; i < removedStmts.size(); i++) {
            model.remove(removedStmts.get(i));
        }
        for (int i = 0; i < addedStmts.size(); i++) {
            model.add(addedStmts.get(i));
        }
        return model;
    }

    public Model coordinate2(Model model)
    {
        ArrayList<Statement> removedStmts = new ArrayList<Statement>();
        ArrayList<Statement> addedStmts = new ArrayList<Statement>();

        HashMap<Resource, BinaryTreeNode> lists = new HashMap<Resource, BinaryTreeNode>();

        String querystr = " PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
                		+ " SELECT ?x ?y ?z ?type WHERE " 
                		+ " {?x rdf:type ?type. ?x rdf:first ?y. ?x rdf:rest ?z.} ";

        Query query = QueryFactory.create(querystr);
        QueryExecution qe = QueryExecutionFactory.create(query,  model);
        ResultSet results = qe.execSelect();

        for (Iterator<?> iter = results; iter.hasNext();) {
            QuerySolution res = (QuerySolution) iter.next();
            Resource x = (Resource) res.get("x");
            RDFNode y = (RDFNode) res.get("y");
            Resource z = (Resource) res.get("z");
            removedStmts.add(model.createStatement(x, RDF.first, y));
            removedStmts.add(model.createStatement(x, RDF.rest, z));

            BinaryTreeNode tempX = null;
            BinaryTreeNode tempZ = null;

            if (!lists.containsKey(x)) {
                tempX = new BinaryTreeNode(x);
                lists.put(x, tempX);
            } else {
                tempX = lists.get(x);
            }
            tempX.setRight(y);
            tempX.setNodeType(null);
            if (z.toString().equals(Constant.RDF_NS + "nil")) {
                tempX.setLeft(null);
            } else {
                if (!lists.containsKey(z)) {
                    tempZ = new BinaryTreeNode(z);
                    lists.put(z, tempZ);
                } else {
                    tempZ = lists.get(z);
                }
                tempX.setLeft(tempZ);
                tempZ.setFather(tempX);
            }
        }
        Iterator<BinaryTreeNode> listNodes = lists.values().iterator();
        while (listNodes.hasNext()) {
            BinaryTreeNode node = listNodes.next();
            if (node.getLeft() == null) {
                ArrayList<RDFNode> members = new ArrayList<RDFNode>();
                members.add((RDFNode) node.getRight());
                while (node.getFather() != null) {
                    node = node.getFather();
                    members.add((RDFNode) node.getRight());
                }
                for (int i = 0; i < members.size(); i++) {
                    addedStmts.add(model.createStatement((Resource) node.getValue(), 
                            RDFS.member, (RDFNode) members.get(i)));
                }
            }
        }
        for (int i = 0; i < removedStmts.size(); i++) {
            model.remove(removedStmts.get(i));
        }
        for (int i = 0; i < addedStmts.size(); i++) {
            model.add(addedStmts.get(i));
        }
        return model;
    }
}

class BinaryTreeNode
{

    private BinaryTreeNode father = null;
    private BinaryTreeNode left = null;
    private Object right = null;
    private boolean isLeaf = false;
    private Object value;
    private Object nodeType;

    public BinaryTreeNode(Object v)
    {
        value = v;
    }

    public BinaryTreeNode getFather()
    {
        return father;
    }

    public BinaryTreeNode getLeft()
    {
        return left;
    }

    public Object getRight()
    {
        return right;
    }

    public boolean isLeaf()
    {
        return isLeaf;
    }

    public Object getValue()
    {
        return value;
    }

    public Object getNodeType()
    {
        return nodeType;
    }

    public void setFather(BinaryTreeNode node)
    {
        father = node;
    }

    public void setLeft(BinaryTreeNode node)
    {
        left = node;
    }

    public void setRight(Object node)
    {
        right = node;
    }

    public void setIsLeaf(boolean leaf)
    {
        isLeaf = leaf;
    }

    public void setValue(Object v)
    {
        value = v;
    }

    public void setNodeType(Object nt)
    {
        nodeType = nt;
    }
}
