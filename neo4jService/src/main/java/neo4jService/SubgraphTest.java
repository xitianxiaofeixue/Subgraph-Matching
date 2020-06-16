package neo4jService;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;

public class SubgraphTest {
	
	static HashMap<Integer, ArrayList<String>> verticesSet = new HashMap<Integer, ArrayList<String>>();
	static HashMap<Integer, ArrayList<Integer>> relationshipSet = new HashMap<Integer, ArrayList<Integer>>();
	static ArrayList<ArrayList<Node>> solutionSet = new ArrayList<ArrayList<Node>>();
	static HashMap<Integer, ArrayList<Node>> searchSpace = new HashMap<Integer, ArrayList<Node>>();
	static ArrayList<Node> tempSolution = new ArrayList<Node>();
	static HashMap<Integer, Integer> finalOrder = new HashMap<Integer, Integer>();
	static GraphDatabaseService db;
	
	public static void main(String[] args) {
		
		// Configuration of graph database - set the database path for which you
				//图数据库的配饰，设置路径
				// have to test
				File file = new File("C:\\Users\\lenovo\\.Neo4jDesktop\\neo4jDatabases\\database-7b811fdf-67ce-4e02-a565-eea58046bdea\\installation-3.5.14\\data\\databases\\graph.db");
				db = new GraphDatabaseFactory().newEmbeddedDatabase(file);
				System.out.println("For DB path: "+db.toString());
				ArrayList<String> v1 = new ArrayList<String>(); 
				ArrayList<String> v2 = new ArrayList<String>(); 
				v1.add("Person");
				v1.add("佟帅辰");
				v2.add("Person");
				v2.add("杜康明");
				verticesSet.put(0, v1);
				verticesSet.put(1, v2);
				ArrayList<Integer> test = new ArrayList();
				test.add(1);
				relationshipSet.put(0, test);
			
				//Uncomment or comment following line to run or not run iGraph query graph by setting appropriate path
				//transformiGRAPHFormat(new File("C:/Users/vin/Desktop/RIT Sem 4/GraphDatabases/Assignment4/iGraph/iGraph/human_q10.igraph"));
				System.out.println("Vertices: "+verticesSet);
				System.out.println("Adjacency List: "+relationshipSet);
				
				System.out.println("-------Naive sub-graph matching results---------");
				Long start1 = System.currentTimeMillis();
				//calculating search space
				//计算搜素空间
				oldSearchSpace();
				int counter = 0;
				for (int d : verticesSet.keySet()) {
					finalOrder.put(counter, d);
					counter++;
				}
				//search initiation
				//搜索开始
				search(0);
				Long end1 = System.currentTimeMillis();
				System.out.println("Count: " + solutionSet.size());
				System.out.println("Time taken in seconds:" + ((end1 - start1) / 1000.0));

				tempSolution.clear();
				solutionSet.clear();
				ArrayList<Integer> nodes = new ArrayList<Integer>();
				ArrayList<Integer> order = new ArrayList<Integer>();
				int min = Integer.MAX_VALUE;
				int index = 0;
			}


			/**
			 * The oldSearchSpace function to compute search space for naive subgraph matching
			 * 将每一个顶点按照顶点集顺序，把数据库中具有相同label的点放在对应的链表中
			 */
			private static void oldSearchSpace() {
				searchSpace.clear();
				//这是一个事务
				Transaction tx = db.beginTx();
				for (Integer nodeCur : verticesSet.keySet()) {
					System.out.println(verticesSet.get(nodeCur));
					ResourceIterator<Node> itr = db.findNodes(Label.label(verticesSet.get(nodeCur).get(0)),"name",verticesSet.get(nodeCur).get(1));
					while (itr.hasNext()) {
						Node n = itr.next();
						if (searchSpace.containsKey(nodeCur)) {
							searchSpace.get(nodeCur).add(n);
						} else {
							searchSpace.put(nodeCur, new ArrayList<Node>());
							searchSpace.get(nodeCur).add(n);
						}
					}
				}
				tx.success();
				System.out.println("Node:  SearchSpace");
				for (int i : searchSpace.keySet()) {
					System.out.println(i + ": " + searchSpace.get(i).size());
				}
			}


			/**
			 * The search function recursively calls itself to match the subgraph
			 *
			 * @param i - integer representating current node
			 *       
			 */	
			private static void search(int i) {
				for (Node n : searchSpace.get(i)) {
					if (tempSolution.size() > i) {
						tempSolution.remove(i);
					}
					if (tempSolution.contains(n))
						continue;
					if (!check(tempSolution, n, i))
						continue;
					tempSolution.add(n);
					if (i < verticesSet.size() - 1) {
						search(i + 1);
					} else {
						if (tempSolution.size() == verticesSet.size()) {
							
							//for iGraph format requirement adding a constraint - Uncomment this code to add constraint. 
							//if(solutionSet.size()>1000){
							//	System.exit(0);
							//}
							System.out.println(Arrays.toString(tempSolution.toArray()));
							solutionSet.add(tempSolution);
						}
					}
				}
			}

			/**
			 * The check function checks whether current node satisfies relationships with nodes in the tempSolution if there are any.
			 *	这个函数检查当前的节点是否满足tempSolution中现存的关系，如果这里有的话
			 * @param tempSolution - partial solution in ArrayList
			 * @param n - node n representating current node
			 * @param i - integer representating current search index
			 * 
			 * @return true if a current node satisfies relationships with nodes in the tempSolution if there are any otherwise false.
			 *       
			 */	
			private static boolean check(ArrayList<Node> tempSolution, Node n, int i) {
				int l = 0;
				if (!tempSolution.isEmpty()) {
					for (int k = 0; k < i; k++) {
						l = 0;
						if (relationshipSet.get(k).contains(i)) {
							Iterator<Relationship> itr = n.getRelationships(Direction.OUTGOING).iterator();
							while (itr.hasNext()) {
								Node tempNode = itr.next().getOtherNode(n);
								if (tempSolution.get(k).getId() == tempNode.getId()) {
									l = 1;
								}
							}
							if (l == 0) {
								return false;
							}
						}
					}
				}
				return true;
			}
		
}

