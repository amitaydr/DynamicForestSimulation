package dynamicGraph;

import simulation.Main;

public class Forest {
	private Vertex[] nodes;
	private int numNodes;
	private int numEdges;
	private int countSuc;
	
	public Forest(int numNodes){
		this.numNodes=numNodes;
		this.nodes = new Vertex[numNodes];
		for(int i=0; i<numNodes; i++){
			nodes[i]=new Vertex(i,numNodes);
		}
		this.numEdges = 0;
		this.countSuc = 0;
	}
	
	public int addEdge(int i, int j){
		if(nodes[j].getTreeManagerId() == nodes[i].getTreeManagerId()){
			throw new RuntimeException("Trying to add an edge between 2 nodes in the same tree");
		}
		int numFlips = nodes[i].AddNeighbor(nodes[j]);
		nodes[j].UpdateTreeManager(nodes[i].getTreeManagerId());
		numEdges++;
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
		
		int indexOfNeighborToRemove = (int) (Math.random()*iNextEmpty);
		Vertex vj = vi.getNeighbor(indexOfNeighborToRemove);
		vi.removeNeighbor(indexOfNeighborToRemove);
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
		int j = (int) (Math.random()*numNodes);
		while(i==j || nodes[i].getTreeManagerId()==nodes[j].getTreeManagerId()){
			 i = (int) (Math.random()*numNodes);
			 j = (int) (Math.random()*numNodes);
		}
		int itt = addEdge(i,j);
		Main.log("+(" + i + ","+ j + ")"+itt +"\n");
		if(itt == numNodes-2){
			countSuc++;
			if(countSuc == numNodes-2){
				Main.log("bingo\n");
				System.out.println("bingo");
			}
		}
		else{
			countSuc = 0;
		}

	}
	
	public void removeSomeEdge(){
		int i = (int) (Math.random()*numNodes);
		if (nodes[i].getNextEmpty()!=0){
			int j = removeEdge(i);
			Main.log("-(" + i +"," + j + ")\n");

		}
		else{
			removeSomeEdge();
		}
	}

	public void printGraph() {
		for(int i=0; i<numNodes; i++){
			nodes[i].printNode();
		}
		Main.log("\n");;
		
	}
}
