package fusion.oapt.algorithm.merger.graphMerge.mapping;

import rita.wordnet.RiWordnet;

public class WordNet 
{
    
    public boolean getSimilarity(String terme1, String terme2)
    {
    	
    	RiWordnet wordnet  = new RiWordnet();
    	
       // In wordnet, a synset can have several role: noun, verb, adjective ==> the method getBestPos() estimates the best role of the synset
       String pos1 = wordnet.getBestPos(terme1);    
       if(pos1!=null)
       {
    	   String[] synonyms = wordnet.getAllSynonyms(terme1, pos1,20);
    	   
    	   if(synonyms!=null &&synonyms.length!=0)   
    		   
           		for(int i=0; i< synonyms.length; i++)  
           		{
           			if(synonyms[i].equals(terme2)) 
           			{
           				return true;
           			}
           		}
       		}
       
       String pos2 = wordnet.getBestPos(terme2);    
       if(pos2!=null)
       {
    	   String[] synonyms = wordnet.getAllSynonyms(terme2, pos2,20);
    	   
    	   if(synonyms!=null &&synonyms.length!=0)   
    		   
           		for(int i=0; i< synonyms.length; i++)  
           		{
           			if(synonyms[i].equals(terme1)) 
           			{
           				return true;
           			}
           		}
       		}    	
       return false;    	
    }
}
