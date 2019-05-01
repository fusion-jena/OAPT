package fusion.oapt.algorithm.merger.graphMerge.mapping;



import agg.xt_basis.GraGra;

public class Test 
{		
	public static void main (String [] args) throws Exception
	{	
		
		//*********** Load the graph of the ontology 1		
		System.out.println("Enter the path of the graph of your ontology 1");	
		//Scanner sc1  = new Scanner(System.in);
		String fileName1="ontologyOnGGX.ggx";
		//fileName1= sc1.nextLine();	
				
		GraGra gramO1 =  new GraGra(true);		
		gramO1.setFileName("fileName");		
		gramO1.load(fileName1);
		gramO1.setName("Grammar");
							
		if (gramO1 == null) 			
			System.out.println("Grammar:  " + fileName1 + "   inexistant! ");		
		
		
		//*********** Load the graph of the ontology 2		
		System.out.println( "Enter the path of the graph of your ontology 2 ");	
		//Scanner sc2  = new Scanner(System.in);
		String fileName2="ontologyOnGGX.ggx";
		//fileName2= sc2.nextLine();	
				
		GraGra gramO2 = new GraGra(true);		
		gramO2.setFileName("fileName");		
		gramO2.load(fileName2);
		gramO2.setName("Grammar");
							
		if (gramO2 == null) 			
			System.out.println("Grammar:  " + fileName2 + "   inexistant! ");		
	 
	     	//begin of the composition process
	     	long beginC = System.currentTimeMillis();
	     	
	        
	        MappingFile  fichCorresp= new  MappingFile(gramO1,gramO2);
            fichCorresp.createMappingFile()  ;
            
            long endC = System.currentTimeMillis();
	        float timeC = ((float) (endC-beginC)) / 1000f;
            
            System.out.println( "\nExecution time of the mapping process is : "+ timeC);
		}
	}
