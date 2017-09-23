package dynamicGraph;

import simulation.Main;

public class Vertex {
	private int id;
	private int C;
	private int treeManagerId;
	private Vertex[] neighbors;
	private Vertex[] incomingNeighbors;
	private int nextEmpty;
	private int incomingNextEmpty;
	



	public Vertex(int id, int graphSize, int c){
		this.C = c;
		this.neighbors = new Vertex[c];
		this.id = id;	
		this.setTreeManagerId(id);
		this.nextEmpty = 0;
		this.incomingNeighbors = new Vertex[graphSize];
		this.incomingNextEmpty = 0;
	}
	
	
	
	/***
	 * adding a new neighbor to this vertex.
	 * if neighbors[] is at full capacity- recursively changes the direction of all outgoing edges before adding the new one. 
	 * @param nei the neighbor to add
	 * @return the number of recursive calls to AddNeighbor
	 */
	public int AddNeighbor(Vertex nei){
		int nCalls = 0;
		if (nextEmpty == C){
			for (int i=C-1; i>-1; i--){
				nCalls++;
				neighbors[i].removeIncomingNighbor(this);
				nCalls += neighbors[i].AddNeighbor(this);
				this.neighbors[i] = null;
				nextEmpty --;						
			}
		}
		this.neighbors[nextEmpty] = nei;
		nextEmpty++;		
		nei.addIncomingNeighbor(this);
		return nCalls;
	}


	private void addIncomingNeighbor(Vertex v) {
		incomingNeighbors[incomingNextEmpty]=v;
		incomingNextEmpty++;	
	}



/***
 * removes a specified neighbor from the neighbors array and shifts the rest of the neighbors to fill the empty spot.
 * @param i index o neighbor to remove
 */
	public void removeNeighbor(int i) {
		if(i>=nextEmpty) {
			throw new RuntimeException("trying to remove neighbor that doesn't exist");
		}
		
		Vertex nei = neighbors[i];
		for(;i<nextEmpty-1;i++){
			this.neighbors[i]=this.neighbors[i+1];
		}
		if(i<C){
			this.neighbors[i] = null;
		}
		nextEmpty--;
		nei.removeIncomingNighbor(this);
		
	}



	private void removeIncomingNighbor(Vertex v) {
		int i=0;
		while(incomingNeighbors[i]!=v){
			i++;
		}
		
		while(i<incomingNextEmpty){
			incomingNeighbors[i] = incomingNeighbors[i+1];
			i++;
		}
		incomingNextEmpty--;
	
	}



	public int getId() {
		return id;
	}



	public int getTreeManagerId() {
		return treeManagerId;
	}

	public int getNextEmpty() {
		return nextEmpty;
	}

	public void setTreeManagerId(int treeManagerId) {
		this.treeManagerId = treeManagerId;
	}



	public void UpdateTreeManager(int newTreeManagerId) {
		if(treeManagerId == newTreeManagerId){
			return;
		}
		treeManagerId = newTreeManagerId;
		for (int i=0; i<nextEmpty; i++){
			neighbors[i].UpdateTreeManager(newTreeManagerId);
		}
		for (int i=0; i<incomingNextEmpty; i++){
			incomingNeighbors[i].UpdateTreeManager(newTreeManagerId);
		}
		
	}



	public Vertex getNeighbor(int index) {
		if(index>=nextEmpty){
			throw new RuntimeException("Trying to get neighbor in index higher than the number of neighbors");
		}
		
		return neighbors[index];
	}



	public void printNode() {
		for(int i=0; i<nextEmpty;i++){
			Main.log("("+this.id+","+neighbors[i].id+") ");
		}
		
	}
	

}
