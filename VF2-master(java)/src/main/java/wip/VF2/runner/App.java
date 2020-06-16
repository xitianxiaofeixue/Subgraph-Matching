package wip.VF2.runner;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import wip.VF2.core.State;
import wip.VF2.core.VF2;
import wip.VF2.graph.Graph;


public class App {

	public static void main(String[] args) throws FileNotFoundException {
		
		String querypath = "C:\\Users\\lenovo\\eclipse-workspace\\hanlp\\src\\hanlp\\newGraph.txt"; //查询图存放地址
		
		Path graphPath = Paths.get("C:\\Users\\lenovo\\Desktop\\VF2-master(java)\\data\\graphDB", "wyc1.my"); //知识图谱中的节点和边存放地址
		Path queryPath = Paths.get(querypath);
		Path outPath = Paths.get("C:\\Users\\lenovo\\Desktop\\VF2-master(java)\\data\\graphDB", "res_Q21.my"); //结果输出地址
		
		if (args.length == 0) {
			System.out.println();
			System.out.println("Warning: no arguments given, using default arguments");
			System.out.println();
		}
		
		for (int i = 0; i < args.length; i++){
			if (args[i].equals("-t")) {
				graphPath = Paths.get(args[i+1]);
				i++;
			} else if (args[i].equals("-q")) {
				queryPath = Paths.get(args[i+1]);
				i++;
			} else if (args[i].equals("-o")) {
				outPath = Paths.get(args[i+1]);
				i++;
			} else {
				System.exit(1);
			}
		}
		
		System.out.println("Target Graph Path: " + graphPath.toString());
		System.out.println("Query Graph Path: " + queryPath.toString());
		System.out.println("Output Path: " + outPath.toString());
		System.out.println();
		
		
		long startTime = System.currentTimeMillis();
	
		PrintWriter writer = new PrintWriter(outPath.toFile());

		ArrayList<Graph> graphSet = loadGraphSetFromFile(graphPath, "Graph");
		ArrayList<Graph> querySet = loadGraphSetFromFile2(queryPath, "Query");
		long endTime = System.currentTimeMillis(); 
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
		VF2 vf2= new VF2();
		
		System.out.println("Loading Done!");
		
		int queryCnt = 0;
		for (Graph queryGraph : querySet){
			queryCnt++;
			ArrayList<State> stateSet = vf2.matchGraphSetWithQuery(graphSet, queryGraph);
			if (stateSet.isEmpty()){
				System.out.println("Cannot find a map for: " + queryGraph.name);
				System.out.println();
				writer.write("Cannot find a map for: " + queryGraph.name + "\n\n");
				writer.flush();
			} else {
				System.out.println("Found " + stateSet.size() + " maps for: " + queryGraph.name);
				System.out.println();
				writer.write("Maps for: " + queryGraph.name + "\n");
				for (State state : stateSet){
					writer.write("In: " + state.targetGraph.name + "\n");
					// state.printMapping();
					state.writeMapping(writer);
				}		
				writer.write("\n");
				writer.flush();
			}
		}
	}

	/*
	 * public void test(String queryGraphPath,String graphGraphPath) throws
	 * FileNotFoundException {
	 * 
	 * Path graphPath = Paths.get(graphGraphPath); Path queryPath =
	 * Paths.get(queryGraphPath); Path outPath =
	 * Paths.get("C:\\Users\\lenovo\\Desktop\\VF2-master(java)\\data\\graphDB",
	 * "res_Q21.my");
	 * 
	 * System.out.println("Target Graph Path: " + graphPath.toString());
	 * System.out.println("Query Graph Path: " + queryPath.toString());
	 * System.out.println("Output Path: " + outPath.toString());
	 * System.out.println();
	 * 
	 * 
	 * 
	 * PrintWriter writer = new PrintWriter(outPath.toFile());
	 * 
	 * ArrayList<Graph> graphSet = loadGraphSetFromFile(graphPath, "Graph");
	 * ArrayList<Graph> querySet = loadGraphSetFromFile2(queryPath, "Query"); VF2
	 * vf2= new VF2(); System.out.println("Loading Done!");
	 * 
	 * for (Graph queryGraph : querySet){ ArrayList<State> stateSet =
	 * vf2.matchGraphSetWithQuery(graphSet, queryGraph); if (stateSet.isEmpty()){
	 * System.out.println("Cannot find a map for: " + queryGraph.name);
	 * System.out.println(); writer.write("Cannot find a map for: " +
	 * queryGraph.name + "\n\n"); writer.flush(); } else {
	 * System.out.println("Found " + stateSet.size() + " maps for: " +
	 * queryGraph.name); System.out.println(); writer.write("Maps for: " +
	 * queryGraph.name + "\n"); for (State state : stateSet){ writer.write("In: " +
	 * state.targetGraph.name + "\n"); // state.printMapping();
	 * state.writeMapping(writer); } writer.write("\n"); writer.flush(); } } }
	 */
	/**
	 * Load graph set from file
	 * @param inpath	Input path
	 * @param namePrefix	The prefix of the names of graphs
	 * @return	Graph Set
	 * @throws FileNotFoundException
	 */
	private static ArrayList<Graph> loadGraphSetFromFile(Path inpath, String namePrefix) throws FileNotFoundException{

		ArrayList<Graph> graphSet = new ArrayList<Graph>();
		Scanner scanner = new Scanner(inpath.toFile());
		Graph graph = null;
		while (scanner.hasNextLine()){
			String line = scanner.nextLine().trim();
			if (line.equals("")){
				continue;
			} else if (line.startsWith("t")) {
				String graphId = line.split(" ")[2];
				if (graph != null){
					graphSet.add(graph);
				}
				graph = new Graph(namePrefix + graphId);
			} else if (line.startsWith("v")) {
				String[] lineSplit = line.split(" ");
				int nodeId = Integer.parseInt(lineSplit[1]);
				String nodeLabel = lineSplit[2];
				graph.addNode(nodeId, nodeLabel);
			} else if (line.startsWith("e")) {
				String[] lineSplit = line.split(" ");
				int sourceId = Integer.parseInt(lineSplit[1]);
				int targetId = Integer.parseInt(lineSplit[2]);
				String edgeLabel = lineSplit[3];
				graph.addEdge(sourceId, targetId, edgeLabel);
			}
		}
		scanner.close();
		return graphSet;
	}
	private static ArrayList<Graph> loadGraphSetFromFile2(Path inpath, String namePrefix) throws FileNotFoundException{

		ArrayList<Graph> graphSet = new ArrayList<Graph>();
		Scanner scanner = new Scanner(inpath.toFile());
		Graph graph = null;
		while (scanner.hasNextLine()){
			String line = scanner.nextLine().trim();
			if (line.equals("")){
				continue;
			} else if (line.startsWith("t")) {
				String graphId = line.split(" ")[2];
				if (graph != null){
					graphSet.add(graph);
				}
				graph = new Graph(namePrefix + graphId);
			} else if (line.startsWith("v")) {
				String[] lineSplit = line.split(" ");
				int nodeId = Integer.parseInt(lineSplit[1]);
				String nodeLabel = lineSplit[2];
				graph.addNode(nodeId, nodeLabel);
			} else if (line.startsWith("e")) {
				String[] lineSplit = line.split(" ");
				int sourceId = Integer.parseInt(lineSplit[1]);
				int targetId = Integer.parseInt(lineSplit[2]);
				String edgeLabel = lineSplit[3];
				graph.addEdge(sourceId, targetId, edgeLabel);
				for(int i = 0 ; i < 5000 ; i ++) {
					System.out.println("1");
				}
			}
		}
		scanner.close();
		return graphSet;
	}
}