package org.unikernel.lnu.ai.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.HeuristicsVertex;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
public class IDAStar extends Algorithm
{
	public IDAStar(Graph graph)
	{
		super(graph);
	}
	
	@Override
	public Vertex[] step()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Collection<Vertex> search()
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
		int costLimit = ((HeuristicsVertex)startVertex).getHeuristics();
		
		while(costLimit != Integer.MAX_VALUE)
		{
			walkedTrough.clear();
			if(dfsSearch(0, (HeuristicsVertex)startVertex, costLimit).isSolution())
			{
				return Collections.unmodifiableCollection(resultingWay);
			}
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
		walkedTrough.add(currentVertex);
		if(currentVertex.equals(endVertex))
		{
			resultingWay.add(currentVertex);
			return new DFSSearchResult(true, costLimit);
		}
		
		int nextCostLimit = Integer.MAX_VALUE;
		for(Vertex i : graph.getConnectedVertices(currentVertex))
		{
			int newStartCost = startCost + (int)graph.getWeightBetween(currentVertex, i);
			DFSSearchResult sol = dfsSearch(newStartCost, (HeuristicsVertex)i, costLimit);
			if(sol.isSolution())
			{
				resultingWay.add(0, currentVertex);
				return sol;
			}
			nextCostLimit = Math.min(nextCostLimit, sol.getCostLimit());
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
