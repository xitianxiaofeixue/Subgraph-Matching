package wip.VF2.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.omg.CORBA.INTERNAL;

import wip.VF2.graph.Edge;
import wip.VF2.graph.Graph;
import wip.VF2.graph.Node;

/**
 * Core Class of VF2 Algorithm
 * @author luo123n
 */
public class VF2 {
	
	/**
	 * 在给定的一个查询图和一组目标图的情况下查找匹配项
	 * Find matches given a query graph and a set of target graphs
	 * @param graphSet		Target graph set
	 * @param queryGraph	Query graph
	 * @return				The state set containing the mappings
	 */
	public ArrayList<State> matchGraphSetWithQuery(ArrayList<Graph> graphSet, Graph queryGraph){
		ArrayList<State> stateSet = new ArrayList<State>();
		for (Graph targetGraph : graphSet){
			State resState = matchGraphPair(targetGraph, queryGraph);
			if (resState.matched){
				stateSet.add(resState);
			}
		}
		return stateSet;
	}
	
	/**
	 * 找出目标图是否包含查询图
	 * Figure out if the target graph contains query graph
	 * @param targetGraph	Big Graph
	 * @param queryGraph	Small Graph
	 * @param state			The state to store the result mapping
	 * @return				Match or not
	 */
	public State matchGraphPair(Graph targetGraph, Graph queryGraph) {
		State state = new State(targetGraph, queryGraph);
		matchRecursive(state, targetGraph, queryGraph);
		return state;
	}
	
	/**
	 * 递归的确定目标图是否包含查询图
	 * Recursively figure out if the target graph contains query graph
	 * @param state			VF2 State
	 * @param targetGraph	Big Graph
	 * @param queryGraph	Small Graph
	 * @return	Match or not
	 */
	private boolean matchRecursive(State state, Graph targetGraph, Graph queryGraph){
		if (state.depth == queryGraph.nodes.size()){	// Found a match
			state.matched = true;
			return true;
		} else {	// Extend the state
			ArrayList<Pair<Integer,Integer>> candidatePairs = genCandidatePairs(state, targetGraph, queryGraph);
			for (Pair<Integer, Integer> entry : candidatePairs){
				if (checkFeasibility(state, entry.getKey(), entry.getValue())){
					state.extendMatch(entry.getKey(), entry.getValue()); // extend mapping
					if (matchRecursive(state, targetGraph, queryGraph)){	// Found a match
						return true;
					}
					state.backtrack(entry.getKey(), entry.getValue()); // remove the match added before
				}
			}
		}
		return false;
	}
		
	/**
	 * 生成给定当前状态的所有候选对
	 * Generate all candidate pairs given current state
	 * @param state			VF2 State
	 * @param targetGraph	Big Graph
	 * @param queryGraph	Small Graph
	 * @return				Candidate Pairs
	 */
	private ArrayList<Pair<Integer,Integer>> genCandidatePairs(State state, Graph targetGraph, Graph queryGraph) {
		ArrayList<Pair<Integer,Integer>> pairList = new ArrayList<Pair<Integer,Integer>>();
		
		if (!state.T1out.isEmpty() && !state.T2out.isEmpty()){
			// Generate candidates from T1out and T2out if they are not empty

			// Faster Version
			// Since every node should be matched in query graph
			// Therefore we can only extend one node of query graph (with biggest id)
			// instead of generate the whole Cartesian product of the target and query
			// 因为查询图中的每个节点都应该匹配
			// 因此我们只能扩展查询图的一个节点（最大id），而不是生成目标和查询的整个笛卡尔积
			int queryNodeIndex = -1;
			for (int i : state.T2out) {
				queryNodeIndex = Math.max(i, queryNodeIndex);
			}
			for (int i : state.T1out) {
				pairList.add(new Pair<Integer,Integer>(i, queryNodeIndex));
			}
			
			// Slow Version
//			for (int i : state.T1out){
//				for (int j : state.T2out){
//					pairList.add(new Pair<Integer,Integer>(i, j));
//				}
//			}
			return pairList;
		} else if (!state.T1in.isEmpty() && !state.T2in.isEmpty()){
			// Generate candidates from T1in and T2in if they are not empty
			
			// Faster Version
			// Since every node should be matched in query graph
			// Therefore we can only extend one node of query graph (with biggest id)
			// instead of generate the whole Cartesian product of the target and query 
			int queryNodeIndex = -1;
			for (int i : state.T2in) {
				queryNodeIndex = Math.max(i, queryNodeIndex);
			}
			for (int i : state.T1in) {
				pairList.add(new Pair<Integer,Integer>(i, queryNodeIndex));
			}
			
			// Slow Version
//			for (int i : state.T1in){
//				for (int j : state.T2in){
//					pairList.add(new Pair<Integer,Integer>(i, j));
//				}
//			}
			return pairList;
		} else {
			// Generate from all unmapped nodes
			
			// Faster Version
			// Since every node should be matched in query graph
			// Therefore we can only extend one node of query graph (with biggest id)
			// instead of generate the whole Cartesian product of the target and query 
			int queryNodeIndex = -1;
			for (int i : state.unmapped2) {
				queryNodeIndex = Math.max(i, queryNodeIndex);
			}
			for (int i : state.unmapped1) {
				pairList.add(new Pair<Integer,Integer>(i, queryNodeIndex));
			}
			
			// Slow Version
//			for (int i : state.unmapped1){
//				for (int j : state.unmapped2){
//					pairList.add(new Pair<Integer,Integer>(i, j));
//				}
//			}
			return pairList;
		}
	}
		
	/**
	 * 检查添加此匹配项的可行性
	 * Check the feasibility of adding this match
	 * @param state				VF2 State
	 * @param targetNodeIndex	Target Graph Node Index
	 * @param queryNodeIndex	Query Graph Node Index
	 * @return					Feasible or not
	 */
	private Boolean checkFeasibility(State state , int targetNodeIndex , int queryNodeIndex) {
		// Node Label Rule
		// The two nodes must have the same label
		if(!state.queryGraph.nodes.get(queryNodeIndex).label.equals("-1")) {
			if (!state.targetGraph.nodes.get(targetNodeIndex).label.equals(state.queryGraph.nodes.get(queryNodeIndex).label)){
				return false;
			}
		}
		
		
		// Predecessor Rule and Successor Rule
		if (!checkPredAndSucc(state, targetNodeIndex, queryNodeIndex)){
			return false;
		}
		
		// In Rule and Out Rule
		if (!checkInAndOut(state, targetNodeIndex, queryNodeIndex)){
			return false;
		}

		// New Rule
//		if (!checkNew(state, targetNodeIndex, queryNodeIndex)){
//			return false;
//		}
				
		return true; 
	}
	
	/**
	 * 验证前置规则和后续规则
	 * Check the predecessor rule and successor rule
	 * It ensures the consistency of the partial matching
	 * @param state				VF2 State
	 * @param targetNodeIndex	Target Graph Node Index
	 * @param queryNodeIndex	Query Graph Node Index
	 * @return					Feasible or not
	 */
	private Boolean checkPredAndSucc(State state, int targetNodeIndex , int queryNodeIndex) {
		Node targetNode = state.targetGraph.nodes.get(targetNodeIndex);
		Node queryNode = state.queryGraph.nodes.get(queryNodeIndex);
		String[][] targetAdjacency = state.targetGraph.getAdjacencyMatrix();
		String[][] queryAdjacency = state.queryGraph.getAdjacencyMatrix();
		// Predecessor Rule
		// For all mapped predecessors of the query node, 
		// there must exist corresponding predecessors of target node.
		// Vice Versa
		// 前置任务
		// 对于查询节点的全部前置映射，在目标节点一定存在对应的节点
//		for (Edge e : targetNode.inEdges) {
//			if (state.core_1[e.source.id] > -1) {
//				if (queryAdjacency[state.core_1[e.source.id]][queryNodeIndex] == -1){
//					return false;	// not such edge in target graph
//				} else if (queryAdjacency[state.core_1[e.source.id]][queryNodeIndex] != e.label) {
//					return false;	// label doesn't match
//				}
//			}
//		}
		
		for (Edge e : queryNode.inEdges) {
			if (state.core_2[e.source.id] > -1) {
				if (targetAdjacency[state.core_2[e.source.id]][targetNodeIndex].equals("-1")){
					return false;	// not such edge in target graph
				} else if (!targetAdjacency[state.core_2[e.source.id]][targetNodeIndex].equals(e.label)){
					return false;	// label doesn't match
				}
			}
		}
		
		// Successsor Rule
		// For all mapped successors of the query node,
		// there must exist corresponding successors of the target node
		// Vice Versa
//		for (Edge e : targetNode.outEdges) {
//			if (state.core_1[e.target.id] > -1) {
//				if (queryAdjacency[queryNodeIndex][state.core_1[e.target.id]] == -1){
//					return false;	// not such edge in target graph
//				} else if (queryAdjacency[queryNodeIndex][state.core_1[e.target.id]] != e.label) {
//					return false;	// label doesn't match
//				}
//			}
//		}
		
		for (Edge e : queryNode.outEdges) {
			if (state.core_2[e.target.id] > -1) {
				if (targetAdjacency[targetNodeIndex][state.core_2[e.target.id]].equals("-1")){
					return false;	// not such edge in target graph
				} else if (!targetAdjacency[targetNodeIndex][state.core_2[e.target.id]].equals(e.label)) {
					return false;	// label doesn't match
				}
			}
		}
		return true;
	}
	
	/**
	 * 验证入度和出度规则
	 * Check the in rule and out rule
	 * This prunes the search tree using 1-look-ahead
	 * @param state				VF2 State
	 * @param targetNodeIndex	Target Graph Node Index
	 * @param queryNodeIndex	Query Graph Node Index
	 * @return					Feasible or not
	 */
	private boolean checkInAndOut(State state, int targetNodeIndex , int queryNodeIndex) {
		
		Node targetNode = state.targetGraph.nodes.get(targetNodeIndex);
		Node queryNode = state.queryGraph.nodes.get(queryNodeIndex);
		int targetPredCnt = 0, targetSucCnt = 0;
		int queryPredCnt = 0, querySucCnt = 0;
		
		// In Rule
		// The number predecessors/successors of the target node that are in T1in 
		// must be larger than or equal to those of the query node that are in T2in
		//入度规则
		//目标节点在T1in中的前驱/后继节点数必须大于或者等于查询节点在T2in中的前驱/后继节点数
		for (Edge e : targetNode.inEdges){
			if (state.inT1in(e.source.id)){
				targetPredCnt++;
			}
		}
		for (Edge e : targetNode.outEdges){
			if (state.inT1in(e.target.id)){
				targetSucCnt++;
			}
		}
		for (Edge e : queryNode.inEdges){
			if (state.inT2in(e.source.id)){
				queryPredCnt++;
			}
		}
		for (Edge e : queryNode.outEdges){
			if (state.inT2in(e.target.id)){
				querySucCnt++;
			}
		}
//		System.out.println(targetNodeIndex + " " + queryNodeIndex);
//		System.out.println(targetPredCnt + " " + queryPredCnt + " "  + targetSucCnt + " "  + querySucCnt);
		if (targetPredCnt < queryPredCnt || targetSucCnt < querySucCnt){
			return false;
		}

		// Out Rule
		// The number predecessors/successors of the target node that are in T1out 
		// must be larger than or equal to those of the query node that are in T2out
		// T1out中的目标节点的前驱/后继数必须大于或者等于处于T2out中的查询节点的前驱/后继数
		for (Edge e : targetNode.inEdges){
			if (state.inT1out(e.source.id)){
				targetPredCnt++;
			}
		}
		for (Edge e : targetNode.outEdges){
			if (state.inT1out(e.target.id)){
				targetSucCnt++;
			}
		}
		for (Edge e : queryNode.inEdges){
			if (state.inT2out(e.source.id)){
				queryPredCnt++;
			}
		}
		for (Edge e : queryNode.outEdges){
			if (state.inT2out(e.target.id)){
				querySucCnt++;
			}
		}
		if (targetPredCnt < queryPredCnt || targetSucCnt < querySucCnt){
			return false;
		}		
		
		return true;
	}

	/**
	 * 检查新规则
	 * Check the new rule
	 * This prunes the search tree using 2-look-ahead
	 * @param state				VF2 State
	 * @param targetNodeIndex	Target Graph Node Index
	 * @param queryNodeIndex	Query Graph Node Index
	 * @return					Feasible or not
	 */
	private boolean checkNew(State state, int targetNodeIndex , int queryNodeIndex){
		
		Node targetNode = state.targetGraph.nodes.get(targetNodeIndex);
		Node queryNode = state.queryGraph.nodes.get(queryNodeIndex);
		
		int targetPredCnt = 0, targetSucCnt = 0;
		int queryPredCnt = 0, querySucCnt = 0;
		
		// In Rule
		// The number predecessors/successors of the target node that are in T1in 
		// must be larger than or equal to those of the query node that are in T2in
		// T1in中目标节点的前驱/后继数,一定比T2in中的查询节点的前驱/后继节点数大
		for (Edge e : targetNode.inEdges){
			if (state.inN1Tilde(e.source.id)){
				targetPredCnt++;
			}
		}
		for (Edge e : targetNode.outEdges){
			if (state.inN1Tilde(e.target.id)){
				targetSucCnt++;
			}
		}
		for (Edge e : queryNode.inEdges){
			if (state.inN2Tilde(e.source.id)){
				queryPredCnt++;
			}
		}
		for (Edge e : queryNode.outEdges){
			if (state.inN2Tilde(e.target.id)){
				querySucCnt++;
			}
		}
		if (targetPredCnt < queryPredCnt || targetSucCnt < querySucCnt){
			return false;
		}
		
		return true;
	}
}
