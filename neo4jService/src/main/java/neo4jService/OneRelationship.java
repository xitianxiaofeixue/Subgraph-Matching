package neo4jService;

public class OneRelationship {
	
	private long n1;
	private String label;
	private long n2;
	
	public OneRelationship(long n1,String label,long n2) {
		this.n1 = n1;
		this.label = label;
		this.n2 = n2;
	}

	public long getN1() {
		return n1;
	}

	public void setN1(long n1) {
		this.n1 = n1;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public long getN2() {
		return n2;
	}

	public void setN2(long n2) {
		this.n2 = n2;
	}

}
