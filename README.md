This project implements a sequential and a parallel solution for a Genetic Algorithm that uses the map-reduce pattern. The algorithm considers an initial population of random genes represented by strings, and evolves the population to be as close as possible to the individual "hello world". The fitness function is the edit-distance to the string "hello-world" and different genetic mutations are possible.

#### Compilation
	
	ant

#### Execution

	ant [name] -Dthreads=t -DN=n -DI=i -Ds=mode -DS=sub

	name := GAmap | SequentialMap
	t : parallelism degree 
	n : population dimention
	i : number of iterations
	mode : selection mode 0 for rank selection, and 1 for roulette selection	
	sub : number of sub-populations.


#### Example
	ant GAmap -Dthreads=8 -DN=10000 -DI=500 -Ds=1 -DS=200
	ant SequentialMap -Dthreads=8 -DN=10000 -DI=500 -Ds=1 -DS=200

The results is located in candidates.txt

Warning: to run the project the skandium framework needs to be in /usr/local/Skandium/skandium-1.0b2.jar
