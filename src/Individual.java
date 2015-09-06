import java.util.Random;


public class Individual {
	
	public String genes;
	public int fitness;
	public boolean updated;
	
	private char[] chars = "abcdefghijklmnopqrstuvwxyz ".toCharArray();

	/*creates an individual with a given string*/
	public Individual(String s){
		genes = s;
		updated = false;
	}
	
	/*creates an individual randomly with genes of length size*/
	public Individual(int size, Random random){
		StringBuilder sb = new StringBuilder();		
		for (int i = 0; i < size; i++) {
		    char c = chars[random.nextInt(chars.length)];
		    sb.append(c);
		}
		genes = sb.toString();			
		updated = false;		
	}
	
		
	 private int minimum(int a, int b, int c) {                            
	        return Math.min(Math.min(a, b), c);                                      
	    }                       
	 	 	
	 
	 /*fitness is the edit distance between genes and target string*/ 
	 public int compute_fitness(Individual target) {				
		//compute the fitness only once
		if(updated) return fitness;		
    	
		String str1 = genes; 
        String str2 = target.genes;        
        //edit distance matrix
        int[][] distance = new int[str1.length() + 1][str2.length() + 1];        
 
        for (int i = 0; i <= str1.length(); i++)                                 
            distance[i][0] = i;                                                  
        for (int j = 1; j <= str2.length(); j++)                                 
            distance[0][j] = j;                                                  
 
        for (int i = 1; i <= str1.length(); i++)                                 
            for (int j = 1; j <= str2.length(); j++)                             
                distance[i][j] = minimum(                                        
                        distance[i - 1][j] + 1,                                  
                        distance[i][j - 1] + 1,                                  
                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
 
        fitness = distance[str1.length()][str2.length()];
        updated = true;
        return  distance[str1.length()][str2.length()];                           
    }                     
	
}
