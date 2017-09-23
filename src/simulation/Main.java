package simulation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import dynamicGraph.Forest;

public class Main {
	private static int size = 6;
	private static int C = 2;
	private static int printFreq = 10;
	
	private static LinkedList<String> smallLogger;
	private static int smallLoggerSize;
	static FileWriter fw = null;
	static BufferedWriter bw = null;
	private static String logdir = "C:\\Users\\amitaydr\\Desktop\\ProjectRepo\\DynamicForestSimulation\\logs\\";
	private static boolean doLog = false;

	
	private static void initLog(){	
		
		if (doLog) {
			String fileName = logdir
					+ LocalDateTime.now().format(
							DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm"))
					+ ".log";
			try {
				fw = new FileWriter(fileName);
				bw = new BufferedWriter(fw);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			smallLogger = new LinkedList<String>(); 
			smallLoggerSize = (1+(C/printFreq))*printFreq*2;
		}
	}
	
	private static void closeLogger() {
		System.out.println("closing log");
		if (doLog) {
			try {
				if (bw != null) {
					bw.close();
				}
				if (fw != null) {
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void log(String s){
		if(doLog){
			try {
				Main.bw.append(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			if(smallLogger.size()==smallLoggerSize){
				smallLogger.remove();
			}
			smallLogger.add(s);
		}
	}
	
	public synchronized static void printSmallLog(){
		if(!doLog){
			System.out.println("Printing small log");
			for(String s : smallLogger){
				System.out.print(s);
			}
		}
	}
	
	public static void main(String[] args) {
		System.out.println("starting");
		initLog();
		long iterations = 1999999999;
		Forest f = new Forest(size,C);
		for (int i = 0; i<size-1;i++){
			f.addSomeEdge();
		}
		for (int i = 0; i<iterations; i++){
			if(i%printFreq == 0){
				f.printGraph();
			}
			f.removeSomeEdge();
			f.addSomeEdge();
		}

		closeLogger();
		System.out.println("done!");

		 
	}



}
