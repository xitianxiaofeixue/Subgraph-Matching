package neo4jService;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.nio.file.Path;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GetDataGraph {
	
	public static void main(String args[]) throws FileNotFoundException{
		
		Path tsc1 = Paths.get("C:\\Users\\lenovo\\eclipse-workspace\\neo4jService\\src\\main\\java\\neo4jService", "DataGraph.txt");
		PrintWriter writer = new PrintWriter(tsc1.toFile());
		writer.write("t # 0" + "\n");
		File file = new File("C:\\Users\\lenovo\\.Neo4jDesktop\\neo4jDatabases\\database-3f372bc2-0775-47a5-abae-49b1c72951a0\\installation-3.5.14\\data\\databases\\graph.db");
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(file);
		String query = "match (n) return n";
		String query2 = "match(n)-[r]->(m) return r";
		ArrayList<OneNode> nodes = new ArrayList<>();
		try(Transaction tx = db.beginTx()){
			Result result = db.execute(query);
			while(result.hasNext()) {
				Node n = (Node) result.next().get("n");
				OneNode m = new OneNode(n.getId(),n.getProperties("name").toString());
				nodes.add(m);
			}
			for(int n = 0 ; n < nodes.size() ; n ++) {
				String x = nodes.get(n).getName();
				int a = x.indexOf("=");
				int b = x.indexOf("}");
				String y = x.substring(a+1,b);
				writer.write("v " + n + " " + y + "\n");
			}
			writer.flush();
			tx.success();
		}catch (Exception e) {
            e.printStackTrace();
        }
		try(Transaction tx = db.beginTx()){
		int num = 0;
		Result result = db.execute(query2);
		ArrayList<OneRelationship> relationships = new ArrayList<>();
		while(result.hasNext()) {
			Map<String, Object> row = result.next();
	         for ( String key : result.columns() )
	         {
	        	 String x = row.get(key).toString();
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
	     		 int n1 = 0,n2 = 0;
	     		 for(int i = 0 ; i < nodes.size() ; i ++) {
	     			 if(nodes.get(i).getN() == y1) {
	     				 n1 = i;
	     			 }
	     			 if(nodes.get(i).getN() == z1) {
	     				 n2 = i;
	     			 }
	     		 }
	     		 writer.write("e " + n1 + " " + n2 + " " + o + "\n");
	         }
		}
		writer.write("t # -1");
		writer.flush();
	}catch (Exception e) {
        e.printStackTrace();
    }
	}
}
