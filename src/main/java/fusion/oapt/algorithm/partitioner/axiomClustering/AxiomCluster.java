
package fusion.oapt.algorithm.partitioner.axiomClustering;


import java.util.Iterator;
import java.util.LinkedHashMap;

import fusion.oapt.model.Node;


public class AxiomCluster
{
    private int cid = -1;
    private double cohesion = 0;
    private LinkedHashMap<Integer, Double> couplings = null;
    private LinkedHashMap<String, Node> elements = null;
    private Node clusterCH;

    public AxiomCluster(int id)
    {
        cid = id;
        couplings = new LinkedHashMap<Integer, Double>();
        elements = new LinkedHashMap<String, Node>();
    }
    
    public AxiomCluster(int id, Node CH)
    {
    	cid = id;
    	this.clusterCH=CH;
        couplings = new LinkedHashMap<Integer, Double>();
        elements = new LinkedHashMap<String, Node>();
    }
    
    
    public void setCH(Node CH)
    {
    	this.clusterCH=CH;
    }
    
    public Node getCH()
    {
    	return clusterCH;
    }

    public int getClusterID()
    {
        return cid;
    }

    public double getCohesion()
    {
        return cohesion;
    }

    public LinkedHashMap<Integer, Double> getCouplings()
    {
        return couplings;
    }

    public Iterator<Double> listCouplings()
    {
        return couplings.values().iterator();
    }

    public LinkedHashMap<String, Node> getElements()
    {
        return elements;
    }

    public Iterator<Node> listElements()
    {
        return elements.values().iterator();
    }

    public void setClusterID(int id)
    {
        cid = id;
    }

    public void setCohesion(double c)
    {
        cohesion = c;
    }

    public void putCoupling(int id, double similarity)
    {
        couplings.put(id, similarity);
    }

    public void putElement(String key, Node node)
    {
        elements.put(key, node);
    }
}
