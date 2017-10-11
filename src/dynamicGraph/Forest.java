package dynamicGraph;

import java.util.ArrayList;
import java.util.LinkedList;

import simulation.Main;

public class Forest {
	private Vertex[] nodes;
	private LinkedList<Vertex> fullNodes;
	private int numNodes;
	private int numEdges;
	private int c;
	private int firstChunkCounter;
	private int currentChunkScore;
	private LinkedList<Integer> chunkFlipNums;
	private int bestScoreYet;
//	private int lastRemovedI;
//	private int lastRemovedJ;
	private ArrayList<Integer> permutation;
	
	public Forest(int numNodes, int c, int chunkSize){
		this.numNodes=numNodes;
		this.nodes = new Vertex[numNodes];
		this.permutation = new ArrayList<Integer>();
		for(int i=0; i<numNodes; i++){
			nodes[i]=new Vertex(i,numNodes,c);
			permutation.add(i);
		}
		this.fullNodes = new LinkedList<Vertex>();
		this.numEdges = 0;
		this.firstChunkCounter = chunkSize;
		this.chunkFlipNums = new LinkedList<Integer>();
		this.currentChunkScore = 0;
		this.bestScoreYet = -1;
		this.c = c;
//		this.lastRemovedI = -1;
//		this.lastRemovedJ = -1;
	}
	
	public int addEdge(int i, int j){
		if(nodes[j].getTreeManagerId() == nodes[i].getTreeManagerId()){
			throw new RuntimeException("Trying to add an edge between 2 nodes in the same tree");
		}
		int numFlips = nodes[i].AddNeighbor(nodes[j]);
		if(nodes[i].getNextEmpty()==c && !fullNodes.contains(nodes[i])){
			fullNodes.add(nodes[i]);
		}
		nodes[j].UpdateTreeManager(nodes[i].getTreeManagerId());
		numEdges++;
		
		chunkFlipNums.add(numFlips);
		currentChunkScore += numFlips;
		if(firstChunkCounter==0){
			currentChunkScore -= chunkFlipNums.remove();
		} else {
			firstChunkCounter--;
		}
		return numFlips;
	}
	/***
	 * remove some edge that starts in vertex i. update the tree metadata while doing so.
	 * @param i origin vertex of the edge to remove. has to have at least 1 outgoing edge.
	 * @return the id of the vertex that was removed from i's neighbors 
	 */
	public int removeEdge(int i){
		Vertex vi = nodes[i];
		int iNextEmpty = vi.getNextEmpty();
		if(iNextEmpty == 0){
			throw new RuntimeException("Trying to remove an edge from a node with no neighbors");
		}
		else if(iNextEmpty == c){
			fullNodes.remove(vi);
		}
		int indexOfNeighborToRemove = (int) (Math.random()*iNextEmpty);
		Vertex vj = vi.getNeighbor(indexOfNeighborToRemove);
		vi.removeNeighbor(indexOfNeighborToRemove);
//		lastRemovedI = vi.getId();
//		lastRemovedJ = vj.getId();
		vj.UpdateTreeManager(vj.getId());
		vi.UpdateTreeManager(vi.getId());
		numEdges--;
		return vj.getId();
	}
	
	public void addSomeEdge(){
		if(numEdges == numNodes - 1){
			throw new RuntimeException("cannot ass an edge, graph is already connected");
		}
		int i = (int) (Math.random()*numNodes);
		//this was in order to make it more likely for full nodes to be assigned
//		if(!fullNodes.isEmpty()){
//			i = fullNodes.get((int) (Math.random()*fullNodes.size())).getId();
//		}
		java.util.Collections.shuffle(permutation);
		
		int j = permutation.get(0);
		int idx = 1;
		while(i==j || nodes[i].getTreeManagerId()==nodes[j].getTreeManagerId()){
			j = permutation.get(idx); 
			idx++ ;
		}
		int itt = addEdge(i,j);
		Main.log("+(" + i + ","+ j + ")"+itt +"\n");
		if(currentChunkScore > bestScoreYet){
			bestScoreYet = currentChunkScore;
			Main.saveToBestChunkLog(bestScoreYet);
		}

	}
	
	public void removeSomeEdge(){
		java.util.Collections.shuffle(permutation);
		int i = permutation.get(0);
		int idx = 1;
		while(nodes[i].getNextEmpty()==0){
			i = permutation.get(idx);
			idx++;
		}
		int j = removeEdge(i);
		Main.log("-(" + i +"," + j + ")\n");
	}

	public void printGraph() {
		StringBuilder acc = new StringBuilder();
		acc.append("=");
		for(int i=0; i<numNodes; i++){
			acc.append(nodes[i].printNode());
		}
		acc.append('\n');
		Main.log(acc.toString());;
		
	}
	
	public int getNumEdges(){
		return numEdges;
	}
}
