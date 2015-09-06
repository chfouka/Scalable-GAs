import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Random;


public class SequentialMap {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub	
		
		//user parameters
		int threads = 1;
		int size = 500; 		
		int iterations = 10;
		int selectionmode = 0; // 0 for rank and 1 for roulette selection
		int subpopolations = 10;
		
		//fixed parameters
		double pselect = 5.0 / 6.0;
		double pcross = 2.0 / 5.0 ;
		double pmutate = 0.05;		
		long init;
		long time;				
		Random r = new Random(6);
		
		
		//read parameters
		try{		
			
			threads = Integer.parseInt(args[0]);
			size = Integer.parseInt(args[1]);
			iterations = Integer.parseInt(args[2]);
			selectionmode = Integer.parseInt(args[3]);
			subpopolations = Integer.parseInt(args[4]);
		
		}
		catch(Exception e){
			System.err.println("Execution mode example: " +
					"ant GAmap -Dthreads=8 -DN=10000 -DI=500 -Ds=1 -DS=200");	
			System.exit(1) ;
		}
		
		//Controls over parameters		
		if(threads < 1 ){
			System.err.println("parallelism degree cannot be less than 1");
			System.exit(1);
		}
		if (size <= 0)
			System.err.println("Population dimension cannot be less than 1 ");		
		if (iterations < 0)
			System.err.println("number of iterations cannot be less than 0");
				
		if (selectionmode != 0 && selectionmode != 1){
			System.err.println("selection mode can be 0 (rank selection)" +
					" or 1 (roulette selection)");
			System.exit(1);
		}
		if(subpopolations < 1){
			System.err.println("number of subpopulations cannot be less that 1");
			System.exit(1);
		}
		if( subpopolations > size ){
			System.err.println("number of subpopulations cannot be larger than N");
			System.exit(1);			
		}
		
		//threads are at most equal to sub-populations  
		if(subpopolations < threads)
			threads = subpopolations;
					
		
		//Generates randomly a population with individuals of max length 20	
		Individual target = new Individual("hello world");
		Population population = new Population (target);
		for(int i = 0; i<size; i++){
			Individual p = new Individual(r.nextInt(20) , r);
			population.individuals.add(p);			
		}
		
		
		SplitterGA splitter = new SplitterGA(subpopolations);
		WorkerGA worker_ga = new WorkerGA(pselect, pcross, pmutate , iterations, selectionmode);
		MergerGA merger = new MergerGA();		
		
		Population finalgeneration = null;
	
		init = System.currentTimeMillis();
		//split
		Population[] subpopulations = splitter.split(population);
		
		//genetic algorithm on each sub-population
		Population[] final_subpopulations = new Population[subpopulations.length ];
		for(int i = 0; i < subpopulations.length ; i++)
			final_subpopulations[i] = worker_ga.execute(subpopulations[i]); 
		
		//merge			
		finalgeneration = merger.merge(final_subpopulations);
		
		time = (System.currentTimeMillis() - init);
										
		System.err.println( time * 0.001 + " [s]");
		
		//write solution into file 
		try{
			PrintWriter candidates = new PrintWriter(new FileWriter("./candidates.txt")); 
			for(Individual i: finalgeneration.individuals){
				candidates.println(i.genes);
			}
			candidates.close();		
		}		
		catch(Exception e){
			System.err.println("candidates.txt cannot be created");
		}		
	}
}
