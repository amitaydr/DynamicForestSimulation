package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import visualization.Plotter;
import dynamicGraph.Forest;

public class Main {
	private static int size = 7;
	private static int C = 2;
	private static int chunkSize = 14;
	private static long  iterations = 50000000L;

	
	private static LinkedList<String> smallLogger;
	private static LinkedList<String> bestChunkLog;
	private static int smallLoggerSize;
	static FileWriter fw = null;
	static BufferedWriter bw = null;
	public static String logdir = "C:\\logs\\"; //"C:\\Users\\amitaydr\\Desktop\\ProjectRepo\\DynamicForestSimulation\\logs\\session5\\";
	private static boolean doLogAll = false;
	private static boolean animateResult = true;
	private static int bestScore;

	
	private static void initLog(){	
		new File(logdir).mkdir();
		if (doLogAll) {
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
			smallLoggerSize = chunkSize*4;
		}
	}
	
	private static void closeLogger() {
		if (doLogAll) {
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
		if(doLogAll){
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
		if(!doLogAll){
			System.out.println("Printing small log");
			for(String s : smallLogger){
				System.out.print(s);
			}
		}
	}
	
	public synchronized static void printBestLog(){
		if(!doLogAll){
			System.out.println("Printing best chunk log");
			for(String s : bestChunkLog){
				System.out.print(s);
			}
			System.out.println("total flips required in chunk- " + bestScore );
		}
	}
	
	public static void saveToBestChunkLog(int score){
		if(!doLogAll){
			bestChunkLog = new LinkedList<String>(smallLogger);
			bestScore = score;
		}
	}
	
	public static void saveBestChunkLogToFile(){
		String fileName = logdir
				+ "N=" + size 
				+ "_C=" + C 
				+ "_ChunckSize=" + chunkSize 
				+ "_Iterations=" + iterations 
				+ "Time=" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd_MM_yyyy_HH_mm"))
				+ ".log";
		try {
			fw = new FileWriter(fileName);
			bw = new BufferedWriter(fw);
			bw.append("N="+size+" C="+C+" ChunkSize="+chunkSize+" iterations="+iterations+"\n");
			for(String s : bestChunkLog){
				bw.append(s);
			}
			bw.append("total flips required in chunk- " + bestScore );
			bw.close();
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public static void mainSimulation() {
		System.out.println("N="+size+" C="+C+" ChunkSize="+chunkSize+" iterations="+iterations);
		initLog();
		bestScore = 0;
		Forest f = new Forest(size,C,chunkSize);
		int numEdges = 0;
		for (int i = 0; i<size-1;i++){
			f.addSomeEdge();
		}
		for (int i = 0; i<iterations; i++){
			if(i%chunkSize == 0){
				f.printGraph();
			}
			numEdges = f.getNumEdges();
			if (numEdges == 0){
				f.addSomeEdge();
			}
			else if(numEdges == size -1){
				f.removeSomeEdge();
			}
			else{
				if (Math.random() > 0.5){
					f.addSomeEdge();
				}
				else{
					f.removeSomeEdge();
				}
			}
		}

		closeLogger();
		if (!doLogAll){
			printBestLog();
			saveBestChunkLogToFile();
		}
		
		System.out.println("done!");

		if(animateResult) {
			Plotter.plot(bestChunkLog, size, C);
		}
	}
	
	public static void cleanAll(){
		smallLogger = null;
		bestChunkLog = null;
		fw = null;
		bw = null;
		bestScore = 0;
	}
	
	public static void main(String[] args) {
		//running a few simulations in a row
//		long[] itrs = new long[]{10000000L,50000000L,100000000L,500000000L,1000000000};
//		for(int i = 7;i<21;i++){
//			for(long itr : itrs) {
//				size = i;
//				chunkSize = 2*size;
//				iterations = itr;
//				mainSimulation();
//				cleanAll();
//			}
//		}
		
		//running a few simulations in a row
//		for(int i = 0;i<15;i++){
//			size = 6 + i;
//			chunkSize = 2*size;
//			mainSimulation();
//			iterations *= Math.pow(10.0, 1/7.0);
//			cleanAll();
//		}
		
		
		mainSimulation();
		
		//in order to run a visualization of an existing log uncomment the next line and switch file name
//		Plotter.plotFromLogFile("N=6_C=2_ChunckSize=12_Iterations=1000000000Time=17_10_2017_09_14");
	}



}
