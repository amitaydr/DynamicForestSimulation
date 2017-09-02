package simulation;

import dynamicGraph.Forest;

public class Main {

	public static void main(String[] args) {
		System.out.println("starting");
		int size = 20;
		int iterations = 300000;
		Forest f = new Forest(size);
		for (int i = 0; i<size-1;i++){
			f.addSomeEdge();
		}
		for (int i = 0; i<iterations; i++){
			System.out.println("****iteration number "+i+"****");
			f.removeSomeEdge();
			f.addSomeEdge();
		}
		
		System.out.println("done!");

		 
	}

}
