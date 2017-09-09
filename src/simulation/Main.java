package simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import dynamicGraph.Forest;

public class Main {
	static FileWriter fw = null;
	public static BufferedWriter bw = null;
	
	private static void initLog(){	
		String fileName = "C:\\Users\\amitaydr\\Desktop\\ProjectRepo\\DynamicForestSimulation\\" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm")) + ".log";
		try {
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void closeLogger() {
		try {
			if (bw != null){
				bw.close();
			}
			if (fw != null){
				fw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void log(String s){
		try {
			Main.bw.append(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println("starting");
		initLog();
		int size = 20;
		int iterations = 1000000000;
		Forest f = new Forest(size);
		for (int i = 0; i<size-1;i++){
			f.addSomeEdge();
		}
		for (int i = 0; i<iterations; i++){
			//System.out.println("****iteration number "+i+"****");
			if(i%10 == 0){
				f.printGraph();
			}
			f.removeSomeEdge();
			f.addSomeEdge();
		}

		closeLogger();
		System.out.println("done!");

		 
	}



}
