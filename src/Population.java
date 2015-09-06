import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;


public class Population{
	
	public Individual target;
	public ArrayList<Individual> individuals;
	public int tot_fitness;	
	
	private char[] chars = "abcdefghijklmnopqrstuvwxyz ".toCharArray();	
	private Comparator<Individual> cmp = new Comparator<Individual>() {
		public int compare(Individual a, Individual b) {			
            if(a.fitness == b.fitness ){
                return 0;
            }
            else if (a.fitness < b.fitness)
            return -1;
            else return 1;
        }};		        
        
	public Population(Individual target){
		this.target = target;
		tot_fitness = 0;
		individuals  = new ArrayList<Individual>();				
	}
		
	/*Evaluates the population individuals*/
	public void compute_fitness(){			
		for(int i = 0; i<individuals.size(); ++i)
			tot_fitness+= individuals.get(i).compute_fitness(target);		
	}
	
	/*Select a fraction of individuals based on their own fitness */
	public void SelectByRank(double percent){		
        Collections.sort(individuals, cmp);
        int tobeselected = (int) (percent * individuals.size());
		
        //we remove the last individuals
        for(int i = individuals.size()-1; i>=tobeselected; i--){     
        	individuals.remove(i);        	
        }      
}
	
	/*Select a fraction of individuals based on their fitness and the population fitness*/	
	 public void RouletteSelection( double percent, Random r){				
		double sum;
		double p;
		int tobeselected = (int) (percent * individuals.size());
		ArrayList<Individual> selected = new ArrayList<Individual>();						
		while (tobeselected > 0){
			p = r.nextDouble();
			sum = 0.0;		
			for (Individual i : individuals){
				sum+= ( 1.0 / (double) i.fitness ) / (double) tot_fitness;
				if ( p < sum){
					selected.add(i);
					tobeselected --;
					break;					
				}
			}		
		}
		
		//current population become the selected one
		individuals = selected;		
	} 
		
	/*Evolution steps:
	 * Cross_over: a fraction of the population generate off-springs 
	 * Mutation: a fraction of the population mutates genes  
	 * */
	public void evolution( double percentCross, double percentMutate, int mode, Random r ){
		
		int tobecrossed = (int) (percentCross * individuals.size());
		
		//if selection mode is rank, we shuffle the fraction to be crossed, since it's ranked.
		if(mode == 0){
			List<Individual> l = individuals.subList(0, tobecrossed);
			ArrayList<Individual> list = new ArrayList<Individual>(l);
			Collections.shuffle(list, r);
		}
		
		//concatenate half genes of father with half genes of mother
		for(int i = 0; i <  tobecrossed ; i = i+2){
			Individual father = individuals.get(i);
			Individual mother = individuals.get( (i + 1) % individuals.size());
			String f = father.genes.substring(0, father.genes.length()/ 2 ).concat
					(mother.genes.substring(mother.genes.length()/ 2, mother.genes.length()));			
			individuals.add(new Individual(f));
		}

		//for simplicity the first individuals are those to be mutated
		int tobemutated = (int)(percentMutate * individuals.size());			
		for(int j = 0;  j< tobemutated; j++){						 
			if(individuals.get(j).genes.length() == 0) continue;
			char c = chars[r.nextInt(chars.length)];			
			char[] str = individuals.get(j).genes.toCharArray();
			str[0] = c; //simply change the first char
			String mutated = new String(str);
			individuals.set(j, new Individual(mutated)) ;			
		}
		
		//reset population fitness after evolution
		tot_fitness = 0;
	}

}
