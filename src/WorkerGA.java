import java.util.Random;
import cl.niclabs.skandium.muscles.Execute;


class WorkerGA implements Execute<Population, Population> {
	
	public double pselect;
	public double pcross;
	public double pmutate;
	public int selectionmode;
	public int iterations;
	
	
	public WorkerGA (double selectPercent, double corssPercent, double mutationPercent,  int iterations, int selmode ){
		pselect = selectPercent;
		pcross = corssPercent;
		pmutate = mutationPercent;
		this.iterations = iterations;
		selectionmode = selmode;			
	}

	
	/*The method implements the genetic algorithm iteration process*/
	public Population execute(Population subpopulation) throws Exception {		
		//long init = System.currentTimeMillis(); 									
		Random r = new Random(6);
		int k = iterations; 		
		do {
			
			/** Step 1: fitness computation ***/						
			subpopulation.compute_fitness();
			
			
			/*** Step 2: selection ****/			
			if(selectionmode == 0)
				subpopulation.SelectByRank(pselect) ;
			else 
				subpopulation.RouletteSelection(pselect, r) ;

			/*** Stage 3: evolution ****/			
			subpopulation.evolution(pcross, pmutate, selectionmode, r);			
			
			k --;
		}
		while (k > 0);
				
		//long time = System.currentTimeMillis() - init; 
		//System.err.println("Worker " + Thread.currentThread().getId() + " " + time * 0.001 + " [s]");
		return subpopulation;	
		
	}
			
}
