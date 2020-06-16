package prim;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;



public class Test {
	public static void main(String args[]) throws FileNotFoundException {
		String querypath = "C:\\Users\\lenovo\\eclipse-workspace\\Prim\\src\\prim\\newGraph.txt";
		Path queryPath = Paths.get(querypath);
		ArrayList<Graph> graphSet = loadGraphSetFromFile(queryPath, "Graph");
		Prim prim = new Prim();
		long startTime = System.currentTimeMillis();
		prim.minTree(graphSet.get(0));
		long endTime = System.currentTimeMillis(); 
		System.out.println("程序运行时间：" + (endTime - startTime) + "ms"); //输出程序运行时间
	}

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
				int num = Integer.parseInt(lineSplit[4]);
				graph.addEdge(sourceId, targetId, edgeLabel, num);
			}
		}
		scanner.close();
		return graphSet;
	}
	private static void printTimeFlapse(long startMilli){
		long currentMili=System.currentTimeMillis();
		System.out.println((currentMili - startMilli) + "milliseconds per graph in average.");
	}
}
