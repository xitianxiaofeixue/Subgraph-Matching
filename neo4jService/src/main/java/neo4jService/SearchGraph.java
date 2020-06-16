package neo4jService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;


public class SearchGraph {
	
	public int isEntity(int num,ArrayList<Integer> vertexsIndex) {
		for (int i = 0 ; i < vertexsIndex.size() ; i ++) {
			if(num == vertexsIndex.get(i)) {
				return i;
			}
		}
		return -1;
	}
	
	public  ArrayList<String> getRelations(String s){
		String x = s;
		ArrayList<String> relationShips = new ArrayList<String>();
		try {
			String[] arg = new String[] { "python", "C:\\Users\\lenovo\\PycharmProjects\\untitled\\venv\\Include\\tscc.py", String.valueOf(x)};
			Process proc = Runtime.getRuntime().exec(arg);// 执行py文件
			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String s1,s2,s3;
			s1 = in.readLine();
			s2 = in.readLine();
			s3 = in.readLine();
			System.out.println(s1);
			System.out.println(s2);
			System.out.println(s3);
			String words[] = s1.split("	+");
			String entitys[] = s2.split("	+");
			String relationships[] = s3.split("	+");
			ArrayList<String> vertexs = new ArrayList<String>();
			ArrayList<Integer> vertexsIndex = new ArrayList<Integer>();
			Path queryPath = Paths.get("C:\\Users\\lenovo\\Desktop\\VF2-master(java)\\data\\graphDB", "tsc2.my");
			PrintWriter writer = new PrintWriter(queryPath.toFile());
			writer.write("t # 1" + "\n");
			for(int i = 0 ; i < entitys.length ; i ++) {
				if( !entitys[i].equals("O")) {
					vertexs.add(words[i]);
					vertexsIndex.add(i);
				}
				if( words[i].equals("谁")) {
					vertexs.add(words[i]);
					vertexsIndex.add(i);
				}
			}
			int relations[] = new int[relationships.length];
			for(int i = 0 ; i < relationships.length ; i ++) {
				String entityRelation[] = relationships[i].split(":");
				relations[i] = Integer.parseInt(entityRelation[0]) - 1;
			}
			for(int i = 0 ; i < relations.length ; i ++) {
				if(relations[i] == -1) {
					for(int j = i ; j < relations.length ; j ++) {
						if (relations[j] == i) {
							relations[i] = j;
							break;
						}
					}
				}
			}
//			for(int i = 0 ; i < relations.length ; i ++) {
//				System.out.print(relations[i] + " ");
//			}
			
			for(int i = 0 ; i < vertexs.size() ; i ++) {
				writer.write("v " + i + " " + vertexs.get(i) + "\n");
			}
			for(int i = 0 ; i < vertexs.size() ; i ++) {
				if(!vertexs.get(i).equals("谁")) {
					StringBuilder process = new StringBuilder("");
					int num = vertexsIndex.get(i);
					num = relations[num];
					while(isEntity(num,vertexsIndex) == -1) {
						process.append(words[num]);
						num = relations[num];
					}
					writer.write("e " + i + " " + isEntity(num,vertexsIndex) + " " + process + "\n");
				}
			}
			
//			String entity1 = null;
//			int entityone = 0;
//			for (int i = 0 ; i < entitys.length ; i ++) {
//				if( entitys[i].equals("S-Nh") ){
//					entity1 = words[i];
//					relationShips.add(entity1);
//					entityone = i;
//				}
//			}
//			String entityRelation[] = relationships[entityone].split(":");
//			while (entityRelation[1].equals("ATT")) {
//				Integer a = Integer.parseInt(entityRelation[0]) - 1;
//				relationShips.add(words[a]);
//				entityRelation = relationships[a].split(":");
//			}
//			for (int i = 0 ; i < relationShips.size() ; i ++) {
//				System.out.println(relationShips.get(i));
//			}
			
//			唐彦	的	学生	的	室友	是	谁	？
//			S-Nh	O	O	O	O	O	O	O
//			3:ATT	1:RAD	5:ATT	3:RAD	6:SBV	0:HED	6:VOB	6:WP
			writer.write("\n");
			writer.flush();
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return relationShips;	
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SearchGraph s1 = new SearchGraph();
		String x = "唐彦的学生佟帅辰的室友是谁？";
		s1.getRelations(x);
	}
}
