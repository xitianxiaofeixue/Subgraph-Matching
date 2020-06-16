package neo4jService;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class cypher {
	
	
	public static void main(String args[]) {
		ArrayList<String> results = new ArrayList<String>();
		Scanner in = new Scanner(System.in);
		System.out.println("请输入你的问题：");
		String x = in.nextLine();
		SearchGraph s1 = new SearchGraph();
		results = s1.getRelations(x);
//		for (int i = 0 ; i < results.size() ; i ++) {
//			System.out.println(results.get(i));
//		}
		File file = new File("C:\\Users\\lenovo\\.Neo4jDesktop\\neo4jDatabases\\database-c3a368c6-389f-4295-a865-000cde70c03c\\installation-3.5.14\\data\\databases\\graph.db");
		GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(file);
		String query = "match(s:Person)" ;
		String letter[] = new String[5];
		letter[0] = "a";
		letter[1] = "b";
		letter[2] = "c";
		letter[3] = "d";
		letter[4] = "e";
		for (int i = 1 ; i < results.size() ; i ++) {
			query += "<-[:`" + results.get(i) + "`]-("+ letter[i - 1] +":Person)";
		}
		query += " where s.name = '" + results.get(0) + "' return " + letter[results.size() - 2] + ".name";
		ArrayList<String> output = new ArrayList<String>();
		//String query ="match(s:Person)<-[:`学生`]-(a:Person)<-[:`室友`]-(b:Person) where s.name = \"唐彦\" return b";
	        Map<String, Object >parameters = new HashMap<String, Object>();
	        try ( Result result = db.execute( query, parameters ) )
	         {
	             while ( result.hasNext() )
	             {
	                 Map<String, Object> row = result.next();
	                 for ( String key : result.columns() )
	                 {
	                	  output.add((String) row.get( key ));
	                 }
	             }
	             System.out.print("这个问题的答案是：");
	             for(int i = 0 ; i < output.size() - 1 ; i ++) {
	            	 System.out.print(output.get(i) + ",");
	             }
	             System.out.print(output.get(output.size() - 1));
	         }

	         registerShutdownHook(db);
	         System.out.println("!");
		//match(s:Person)<-[:`学生`]-(a:Person)<-[:`室友`]-(b:Person) where s.name = "唐彦" return b
	}
	private static void registerShutdownHook(final GraphDatabaseService graphDb){
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run(){
                graphDb.shutdown();
            }
        });
    }
}





		
		
