import cl.niclabs.skandium.muscles.Merge;



class MergerGA implements Merge<Population, Population > {
	
	public Population merge(Population[] subpopulations) throws Exception {
		
		//long init = System.currentTimeMillis();		
		Population finalgeneration = new Population(subpopulations[0].target);					
		int f = 0;
		for(Population p : subpopulations ){
			 for(Individual i: p.individuals){
	                finalgeneration.individuals.add(i);
	                f += i.fitness;
	            }
	            finalgeneration.tot_fitness = f;
	        }		
		
		//long time = System.currentTimeMillis() - init ;		
		//System.err.println("Merger time " + time * 0.001 + "[s]");
		
		return finalgeneration;

	}
}