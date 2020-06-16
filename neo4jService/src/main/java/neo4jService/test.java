package neo4jService;

public class test {

	public static void main(String args[]) {
		String x = "(43256)-[belong_to,107565]->(43377)";
		int a = x.indexOf("(");
		int b = x.indexOf(")");
		String y = x.substring(a + 1,b);
		long y1 = Long.parseLong(y);
		int c = x.lastIndexOf("(");
		int d = x.lastIndexOf(")");
		String z = x.substring(c + 1, d);
		long z1 = Long.parseLong(z);
		int e = x.indexOf("[");
		int f = x.indexOf(",");
		String o = x.substring(e + 1,f);
		System.out.println(y1);
		System.out.println(z);
		System.out.println(o);
	}
}
