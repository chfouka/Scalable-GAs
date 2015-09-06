import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import java.util.concurrent.Future;

import cl.niclabs.skandium.Skandium;
import cl.niclabs.skandium.Stream;
import cl.niclabs.skandium.skeletons.Map;
import cl.niclabs.skandium.skeletons.Skeleton;


public class GAmap {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
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
			
					
		//create the map skeleton
		Skeleton<Population, Population> map = new Map<Population, Population>(
				new SplitterGA(subpopolations), 
				new WorkerGA( pselect, pcross , pmutate , iterations, selectionmode), 
				new MergerGA());						
		
		//result
		Population finalgeneration = null;

		//create the Skandium environment
		Skandium skandium = new Skandium(threads);
		Stream<Population, Population> stream = skandium.newStream(map);
		
		//start measuring completion time
		init = System.currentTimeMillis();	
		
		// pass the input
		Future<Population> f = stream.input(population);								

		// get the output
		try {
			finalgeneration = f.get();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//compute completion time
		time = (System.currentTimeMillis() - init);
		
			
		skandium.shutdown();
	
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
