
package fusion.oapt.algorithm.partitioner.axiomClustering;

import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;


public class ClassStructure
{
    private Node class_name = null;
    private boolean tag_Cluster = false;
    private int cluster_id = 0;
    private NodeList subClass_list = null;
    private NodeList superClass_list = null;
    private NodeList disjointClass_list = null;
    private NodeList EquivalentClass_list = null;
    
    
    public ClassStructure()
    {
    	subClass_list = new NodeList();
    	superClass_list = new NodeList();
    	disjointClass_list = new NodeList();
    	EquivalentClass_list = new NodeList();
        
    }
    
    public void setClassName(Node iName)
    {
    	this.class_name = iName;
    }
    
    public void setTagCluster(boolean tag)
    {
    	this.tag_Cluster = tag;
    }
    
    public void setClusterId(int index)
    {
    	this.cluster_id = index;
    }
    
    public void setSubClassList (NodeList iList)
    {
        this.subClass_list = iList;
    }
    
    public void setSuperClass_list(NodeList iList)
    {
        this.superClass_list = iList;
    }
    
    public void setDisjointClass_list(NodeList iList)
    {
        this.disjointClass_list = iList;
    }
    
    public void setEquivalentClass_list(NodeList iList)
    {
        this.EquivalentClass_list = iList;
    }
    
    
    public Node getName()
    {
    	return class_name;
    }

    public boolean getTag()
    {
    	return tag_Cluster;
    }

    
    public int getClusterId()
    {
        return cluster_id;
    }


    public NodeList getSubClassList()
    {
        return subClass_list;
        //return subClass_list.iterator();
    }
    
    public NodeList getSuperClassList()
    {
        return superClass_list;
    }
    
    public NodeList getDisjointClassList()
    {
        return disjointClass_list;
    }
    
    public NodeList getEquivalentClassList()
    {
        return EquivalentClass_list;
    }

    
    
    
    
    }
