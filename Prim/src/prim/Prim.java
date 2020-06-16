package prim;

public class Prim {
	public Edge[] minTree(Graph queryGraph) {
		int min,vx,vy;
		int weight;
		Edge edge;
		int nodeNum = queryGraph.nodes.size();
		int pgraph[][] = queryGraph.getNumMatrix();
		Edge mst[] = new Edge[nodeNum - 1];
		for(int i = 0 ; i < nodeNum - 1 ; i ++) {
			Edge e1 = new Edge();
			e1.setStart_vex(0);
			e1.setStop_vex(i+1);
			e1.setNum(Math.min(pgraph[0][i+1], pgraph[i+1][0]));
			mst[i] = e1;
		}
		for(int i = 0 ; i < nodeNum - 1 ; i ++) {
			weight = Integer.MAX_VALUE; min = i;
			for(int j = i ; j < nodeNum - 1 ; j ++) {
				if(mst[j].getNum()<weight) {
					weight = mst[j].getNum();
					min = j;
				}
			}
			edge = mst[min];mst[min] = mst[i];mst[i] = edge;
			vx = mst[i].getStop_vex();
			for(int j = i + 1 ; j < nodeNum - 1 ; j ++) {
				vy = mst[j].getStop_vex();
				weight = Math.min(pgraph[vx][vy], pgraph[vy][vx]);
				if(weight < mst[j].getNum()) {
					mst[j].setNum(weight);
					mst[j].setStart_vex(vx);
				}
			}
		}
		for(int i = 0 ; i < mst.length ; i ++) {
			System.out.println(mst[i].label + " " + mst[i].start_vex + " " + mst[i].stop_vex + " " + mst[i].getNum());
		}
		return mst;
	}
}
