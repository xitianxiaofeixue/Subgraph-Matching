package neo4jService;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class GetRelationships {
	
	public static void main(String args[]) {
		File file = new File("C:\\Users\\lenovo\\.Neo4jDesktop\\neo4jDatabases\\database-5f13196b-c7f9-415c-b196-5bb6042c721a\\installation-3.5.14\\data\\databases\\graph.db");
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(file);
		String query = "match(n)-[r]->(m) return r";
		try(Transaction tx = db.beginTx()){
			int num = 0;
			Result result = db.execute(query);
			ArrayList<OneRelationship> relationships = new ArrayList<>();
			while(result.hasNext()) {
				Map<String, Object> row = result.next();
		         for ( String key : result.columns() )
		         {
		        	 System.out.println(row.get(key));
		         }
			}
		}catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.shutdown();    //¹Ø±ÕÊý¾Ý¿â
        }
	}

}
