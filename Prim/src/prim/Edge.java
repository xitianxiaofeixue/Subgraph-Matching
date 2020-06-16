package prim;

public class Edge {
	
	public Graph graph; 	// the graph to which the edge belongs
	
	public Node source; 	// the source / origin of the edge
	public Node target; 	// the target / destination of the edge 
	public String label; 	// the label of this edge
	public int num;
	public int start_vex;
	public int stop_vex;
	
	// creates new edge
	public Edge(Graph g, Node source, Node target, String label,int num) {
		this.graph = g;
		this.source = source; // store source
		source.outEdges.add(this); // update edge list at source
		this.target = target; // store target
		target.inEdges.add(this); // update edge list at target
		this.label = label;
		this.num = num;
	}

	public Edge() {
		
	}
	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}

	public Node getTarget() {
		return target;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getStart_vex() {
		return start_vex;
	}

	public void setStart_vex(int start_vex) {
		this.start_vex = start_vex;
	}

	public int getStop_vex() {
		return stop_vex;
	}

	public void setStop_vex(int stop_vex) {
		this.stop_vex = stop_vex;
	}
	
}
