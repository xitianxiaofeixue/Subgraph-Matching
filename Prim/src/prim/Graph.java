package prim;

import java.util.ArrayList;

/**
 * Graph Class, along with some methods to manipulate the graph.
 * @author luo123n
 */
public class Graph {
	
	public String name; // name of the graph
	public ArrayList<Node> nodes = new ArrayList<Node>(); // list of all nodes
	public ArrayList<Edge> edges = new ArrayList<Edge>(); // list of all edges
	
	private String[][] adjacencyMatrix; // stores graph structure as adjacency matrix (-1: not adjacent, >=0: the edge label)
	private boolean adjacencyMatrixUpdateNeeded = true; // indicates if the adjacency matrix needs an update
	private int[][] numMatrix;
	
	public Graph(String name) {
		this.name = name;
	}
	
	public void addNode(int id, String label) {
		nodes.add(new Node(this, id, label));
		this.adjacencyMatrixUpdateNeeded = true;
	}
	
	public void addEdge(Node source, Node target, String label, int num) {
		edges.add(new Edge(this, source, target, label, num));
		this.adjacencyMatrixUpdateNeeded = true;
	}
	
	public void addEdge(int sourceId, int targetId, String label, int num) {
		this.addEdge(this.nodes.get(sourceId), this.nodes.get(targetId), label, num);
	}
	
	
	/**
	 * Get the adjacency matrix
	 * Reconstruct it if it needs an update
	 * @return Adjacency Matrix
	 */
	public String[][] getAdjacencyMatrix() {
		
		if (this.adjacencyMatrixUpdateNeeded) {
			
			int k = this.nodes.size();
			this.adjacencyMatrix = new String[k][k];	// node size may have changed
			for (int i = 0 ; i < k ; i++)			// initialize entries to -1	
				for (int j = 0 ; j < k ; j++)
					this.adjacencyMatrix[i][j] = "-1"; 
			
			for (Edge e : this.edges) {
				this.adjacencyMatrix[e.source.id][e.target.id] = e.label; // label must bigger than -1
			}
			this.adjacencyMatrixUpdateNeeded = false;
		}
		return this.adjacencyMatrix;
	}
	
	public int[][] getNumMatrix() {
		int k = this.nodes.size();
		this.numMatrix = new int[k][k];	// node size may have changed
		for (int i = 0 ; i < k ; i++)			// initialize entries to -1	
			for (int j = 0 ; j < k ; j++)
				this.numMatrix[i][j] = Integer.MAX_VALUE; 		
		for (Edge e : this.edges) {
			this.numMatrix[e.source.id][e.target.id] = e.num; // label must bigger than -1
		}
		return this.numMatrix;
	}
	
	// prints adjacency matrix to console
	public void printGraph() {
		String[][] a = this.getAdjacencyMatrix();
		int k = a.length;
		
		System.out.print(this.name + " - Nodes: ");
		for (Node n : nodes) System.out.print(n.id + " ");
		System.out.println();
		for (int i = 0 ; i < k ; i++) {
			for (int j = 0 ; j < k ; j++) {
				System.out.print(a[i][j] + " ");
			}
			System.out.println();
		}
	}

	public void setNumMatrix(int[][] numMatrix) {
		this.numMatrix = numMatrix;
	}
}
