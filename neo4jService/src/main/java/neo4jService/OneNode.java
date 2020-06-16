package neo4jService;

public class OneNode {
	
	private long n;
	private String name;
	
	public OneNode (long n,String name) {
		this.n = n;
		this.name = name;
	}

	public long getN() {
		return n;
	}

	public void setN(long n) {
		this.n = n;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
