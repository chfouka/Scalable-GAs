import cl.niclabs.skandium.muscles.Split;

public class SplitterGA implements Split<Population, Population> {

	//number of partitions of the input task to be produced
	public int partitions;
		
	public SplitterGA(int subpopolations){
		partitions = subpopolations;	
	}
	
	public Population[] split(Population population) throws Exception {
 
		//long init = System.currentTimeMillis();		
		Population[] subpopulations = new Population[partitions];
				 
		for(int i = 0; i<partitions; i++){			
			subpopulations[i] = new Population(population.target);
		}
		
		//create balanced partitions
		int k = population.individuals.size() / partitions;
		int r = population.individuals.size() % partitions;
		int p = 0;
		int main_index = 0;
		
		//first r partitions take k+1 individuals
		while( p < r ){
			
			for(int i = 0; i<k+1; i++){				
				subpopulations[p].individuals.add((population.individuals.get(main_index)));
				main_index ++;
			}
			 p++;			
		}
		
		//last (partitions - r) partitions take k individuals
		int remained = 0;
		while(remained < partitions - r){		
			for(int i = 0; i<k; i++){
				subpopulations[p].individuals.add((population.individuals.get(main_index)));
				main_index++;
			}			
			p++;
			remained++;			
		}
		
		//long time = System.currentTimeMillis() - init ;
		//System.err.println("Splitter time " + time * 0.001 + "[s]");
		return subpopulations;		
		
		} 
}

