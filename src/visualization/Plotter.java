package visualization;

import java.util.LinkedList;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.view.Viewer;


public class Plotter {

//	private static int minLengthForGraphPrint = 12;
	static Graph g;
	static MyNode[] myNodes;

	public static void plotOld(){
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


	}
	
	public static void plot(LinkedList<String> logList, int n, int c){
		g = new SingleGraph("tree1");
		Viewer v = g.display();

		myNodes = new MyNode[n];
		for(int i=0; i<n; i++){
			myNodes[i] = new MyNode(c,g,i);
		}
		
		String currLog = logList.remove();
		while(!logList.isEmpty() && !currLog.startsWith("=")){
			currLog = logList.remove();
		}
		initGraph(currLog);
		v.disableAutoLayout();
		while(!logList.isEmpty()){
			currLog = logList.remove();
			int[] tuple = getFirstTuple(currLog);
			if(currLog.startsWith("+")){
				myNodes[tuple[0]].AddNeighbor(myNodes[tuple[1]]);
			}
			else if(currLog.startsWith("-")){
				myNodes[tuple[0]].RemoveNeighbor(myNodes[tuple[1]],true);
			}
		}
		
	}

	private static int[] getFirstTuple(String s) {
		int[] ans = new int[2];
		int start = s.indexOf('(');
		int sep = s.indexOf(',');
		int end = s.indexOf(')');
		ans[0] = Integer.parseInt(s.substring(start+1, sep));
		ans[1] = Integer.parseInt(s.substring(sep+1,end));
		return ans;
	}


	private static void initGraph(String s) {
		System.out.println("\n\ninitializing display:");
		while(s.length()>5){
			int[] tuple = getFirstTuple(s);
			System.out.print("(" + tuple[0] + "," + tuple[1] + ")");
			myNodes[tuple[0]].AddNeighbor(myNodes[tuple[1]]);
			s = s.substring(s.indexOf(')')+1);
			
		}
		System.out.println();
		
		
	}
}
