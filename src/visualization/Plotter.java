package visualization;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;


public class Plotter {

	public static void plot(){
		Graph g = new SingleGraph("tree1");
		Viewer v = g.display();
		int n = 20;
		int c = 1; //numOfNeighbors
		MyNode[] myNodes = new MyNode[n];
		for(int i=0; i<n; i++){
			myNodes[i] = new MyNode(c,g,i);
		}
		
		for(int i=1;i<n;i++){
			myNodes[i-1].AddNeighbor(myNodes[i]);
		}
		
		v.disableAutoLayout();
		
		myNodes[0].RemoveNeighbor(myNodes[1],true);
		myNodes[1].AddNeighbor(myNodes[0]);

		
//		for(int i=0; i<n; i++){
//			if(i>0){
//				myNodes[i-1].RemoveNeighbor(myNodes[i]);
//			}
//		}
	}
}
