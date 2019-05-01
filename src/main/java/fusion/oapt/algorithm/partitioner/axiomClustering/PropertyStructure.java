
package fusion.oapt.algorithm.partitioner.axiomClustering;



import fusion.oapt.model.Node;
import fusion.oapt.model.NodeList;


public class PropertyStructure
{
    private Node property_name = null;
    private boolean tag_Cluster = false;
    private int cluster_id = 0;
    private Node domain_name = null;
    private Node range_name = null;
    private NodeList subProperty_list = null;
    private NodeList inverseOf_List = null;
    
    
    public PropertyStructure()
    {
    	subProperty_list = new NodeList();
    	inverseOf_List = new NodeList();        
    }
    
    public void setPropertyName(Node iName)
    {
    	this.property_name = iName;
    }
    
    public void setTagCluster(boolean tag)
    {
    	this.tag_Cluster = tag;
    }
    
    public void setClusterId(int index)
    {
    	this.cluster_id = index;
    }
    
    public void setDomainName(Node iName)
    {
    	this.domain_name = iName;
    }
    
    public void setRangeName(Node iName)
    {
    	this.range_name = iName;
    }
    
    public void setSubPropertyList (NodeList iList)
    {
        this.subProperty_list = iList;
    }
    
    public void setinverseOf_list(NodeList iList)
    {
        this.inverseOf_List = iList;
    }
        
    public Node getName()
    {
    	return property_name;
    }

    public boolean getTag()
    {
    	return tag_Cluster;
    }
    
    public int getClusterId()
    {
        return cluster_id;
    }


    public Node getDomainName()
    {
    	return domain_name;
    }
    
    public Node getRangeName()
    {
    	return range_name;
    }
    
    public NodeList getSubPropertyList()
    {
        return subProperty_list;
        //return subClass_list.iterator();
    }
    
    public NodeList getinverseOfList()
    {
        return inverseOf_List;
    }
    
    
    }
