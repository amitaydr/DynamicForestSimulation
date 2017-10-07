package visualization;


import org.graphstream.graph.*;

public class MyNode {
	
	private MyNode[] neighbors;
	private Node me;
	private int myIdx;
	private Graph g;
	private int nextEmptySlot;
	public static int sleepTime = 500;

	
	public MyNode (int numOfNeighbors, Graph g, int idx){
		neighbors = new MyNode[numOfNeighbors];
		nextEmptySlot = 0;
		this.g = g;
		this.myIdx = idx;
		me = g.addNode(Integer.toString(idx));
		me.addAttribute("ui.label", Integer.toString(idx));
	}
	
	public void RemoveNeighbor(MyNode nei, boolean doPause){
		for (int i=0; i<nextEmptySlot; i++){
			if(neighbors[i]==nei){
				RemoveNeighborIdx(i,doPause);
				return;
			}
		}
	}
	
	public void RemoveNeighborIdx(int idx, boolean doPause){
		MyNode nei = neighbors[idx];
		for (int i=idx; i<nextEmptySlot - 1; i++){
				neighbors[i] = neighbors[i+1];
		}
		nextEmptySlot--;
		if(doPause) pause();
		g.removeEdge(me,nei.getNode());

	}
	
	public void AddNeighbor(MyNode nei){
		if (nextEmptySlot < neighbors.length)
		{
			neighbors[nextEmptySlot] = nei;
			pause();
			g.addEdge(myIdx + "-" + nei.getIdx(), me, nei.getNode(),true);
			nextEmptySlot++;
		}
		else
		{
			g.addEdge(myIdx + "-" + nei.getIdx(), me, nei.getNode(),true);
			g.addAttribute("ui.stylesheet", "edge#\"" + this.myIdx+"-" + nei.getIdx() + "\" { fill-color: green; }");
			for(int i = neighbors.length - 1; i > -1; i--){
				MyNode curr = neighbors[i];
				g.addAttribute("ui.stylesheet", "edge#\"" + this.myIdx+"-" + curr.getIdx() + "\" { fill-color: red; }");
				pause();
				curr.AddNeighbor(this);
				RemoveNeighborIdx(i,false);

			}
			neighbors[nextEmptySlot] = nei;
			pause();
			g.addAttribute("ui.stylesheet", "edge#\"" + this.myIdx+"-" + nei.getIdx() + "\" { fill-color: black; }");
			nextEmptySlot++;
		}
	}
	
	public void SwitchDirection (int neighborIndex){
		MyNode nei = neighbors[neighborIndex];
		g.removeEdge(me, nei.getNode());
		for (int i = neighborIndex; i< neighbors.length; i++){
			neighbors[i] = neighbors[i+1];
		}
		nei.AddNeighbor(this);
	}
	
	public Node getNode(){
		return me;
	}
	
	public int getIdx(){
		return myIdx;
	}
	
	public void pause(){
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
