package org.unikernel.lnu.ai.agents;

import java.util.Collections;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.unikernel.lnu.ai.agents.api.Algorithm;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.HeuristicsVertex;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
@ServiceProvider(service=Algorithm.class)
public class IDAStar extends Algorithm
{

	public IDAStar()
	{
	}
	
	public IDAStar(Graph graph)
	{
		super(graph);
	}

	@Override
	public List<Vertex> search()
	{
		if(startVertex == null || endVertex == null)
		{
			return null;
		}
//		cost_limit = heuristics[rootNode]
// 
//        while True:
//                (solution, cost_limit) = DFS(0, rootNode, cost_limit, [rootNode])
//                if solution != None: return (solution, cost_limit)
//                if cost_limit == ∞: return None
		reset();
		int costLimit = ((HeuristicsVertex)startVertex).getHeuristics();
		
		while(costLimit != Integer.MAX_VALUE)
		{
			DFSSearchResult res = dfsSearch(0, (HeuristicsVertex)startVertex, costLimit);
			if(res.isSolution())
			{
				return Collections.unmodifiableList(resultingWay);
			}
			costLimit = res.getCostLimit();
			walkedTrough.add(null);	//next iteration mark
			walkedVertices.clear();
		}
		return null;
	}

	private DFSSearchResult dfsSearch(int startCost, HeuristicsVertex currentVertex, int costLimit)
	{
//		print "start_cost:", start_cost, ", node:", node, ", cost_limit:", cost_limit, ", path_so_far:", path_so_far
// 
//        minimum_cost = start_cost + heuristics[node]
//        print "  minimum_cost:", minimum_cost
//        if minimum_cost > cost_limit: return (None, minimum_cost)
//        if node in goalNodes: return (path_so_far, cost_limit)
// 
//        next_cost_limit = Infinity
//        for succNode in successors[node]:
//                newStartCost = start_cost + edgeCosts[(node,succNode)]
//                (solution, new_cost_limit) = DFS(newStartCost, succNode, cost_limit, path_so_far + [succNode])
//                if solution != None: return (solution, new_cost_limit)
//                next_cost_limit = min(next_cost_limit, new_cost_limit)
// 
//        return (None, next_cost_limit)
		
		int minimumCost = startCost + currentVertex.getHeuristics();
		if(minimumCost > costLimit)
		{
			return new DFSSearchResult(false, minimumCost);
		}
		walkedVertices.add(currentVertex);
		if(currentVertex.equals(endVertex))
		{//a goal was reached
			resultingWay.add(currentVertex);
			return new DFSSearchResult(true, costLimit);
		}
		
		int nextCostLimit = Integer.MAX_VALUE;
		for(Vertex i : graph.getConnectedVertices(currentVertex))
		{// iterate through all connected vertices
			if(walkedVertices.contains(i))
			{//omit cycles
				continue;
			}
			//step forward
			walkedTrough.add(new StepData(graph.getConnectionBetween(currentVertex, i), StepData.Type.PATH));
			int newStartCost = startCost + (int)graph.getWeightBetween(currentVertex, i);
			DFSSearchResult sol = dfsSearch(newStartCost, (HeuristicsVertex)i, costLimit);
			if(sol.isSolution())
			{
				resultingWay.add(0, currentVertex);
				return sol;
			}
			//find next cost limit as minimum of connected vertices minimum costs
			nextCostLimit = Math.min(nextCostLimit, sol.getCostLimit());
			//backtracking
			walkedTrough.add(new StepData(graph.getConnectionBetween(i, currentVertex), StepData.Type.BACKTRACK));
		}
		
		return new DFSSearchResult(false, nextCostLimit);
	}
	
	private class DFSSearchResult
	{
		private boolean solution;
		private int costLimit;

		public DFSSearchResult(boolean solution, int costLimit)
		{
			this.solution = solution;
			this.costLimit = costLimit;
		}

		public int getCostLimit()
		{
			return costLimit;
		}

		public boolean isSolution()
		{
			return solution;
		}
	}
}
