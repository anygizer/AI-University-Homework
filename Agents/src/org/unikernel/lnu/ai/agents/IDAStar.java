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
	private ArrayList<Vertex> currentPath = new ArrayList<Vertex>();
	private ArrayList<Vertex> pathToWalk = new ArrayList<Vertex>();
	private int currentStartCost;
	private int costLimit;
	private int nextIterationCostLimit;
	private int iteration = 0;
	
	public IDAStar(Graph graph)
	{
		super(graph);
	}
	
	@Override
	public void reset()
	{
		super.reset();
		currentPath = new ArrayList<Vertex>();
		pathToWalk = new ArrayList<Vertex>();
		iteration = 0;
	}
	
	@Override
	public StepResult step()
	{
		if(startVertex == null || endVertex == null || !resultingWay.isEmpty())
		{
			return null;
		}
		if(iteration != 0 && pathToWalk.isEmpty())
		{//next iteration
			iteration++;
			currentPath.clear();
			walkedTrough.clear();
		}
		if(currentPath.isEmpty())
		{//first step
			if (iteration == 0)	//++?
			{//of the first iteration
				nextIterationCostLimit = ((HeuristicsVertex) startVertex).getHeuristics();
				iteration++;
			}
			pathToWalk.add(startVertex);
			currentStartCost = 0;
			costLimit = nextIterationCostLimit;
			nextIterationCostLimit = Integer.MAX_VALUE;
		}
		Vertex previousVertex = currentPath.isEmpty() ?
				null : currentPath.get(currentPath.size()-1);
		
		HeuristicsVertex currentVertex;
		if(previousVertex == null || graph.areConnected(previousVertex, pathToWalk.get(0)))
		{//first step (pathToWalk empty) or _forthtracking_ (previousVertex != null)
			currentVertex = (HeuristicsVertex) pathToWalk.remove(0);
			if(previousVertex != null)
			{
				currentStartCost += graph.getWeightBetween(previousVertex, currentVertex);
			}
			int minimumCost = currentStartCost + currentVertex.getHeuristics();
			if (minimumCost > costLimit)
			{
				currentStartCost -= graph.getWeightBetween(previousVertex, currentVertex);
				nextIterationCostLimit = Math.min(nextIterationCostLimit, minimumCost);
				return new StepResult(false, true, previousVertex, currentVertex);	//pseudo-backtracking
			}
			walkedTrough.add(currentVertex);
			currentPath.add(currentVertex);
			if (currentVertex.equals(endVertex))
			{
				resultingWay.addAll(currentPath);
				return new StepResult(true, false, previousVertex, currentVertex);
			}
			Collection<Vertex> connVs = graph.getConnectedVertices(currentVertex);
			connVs.removeAll(walkedTrough);	//omit cycles
			pathToWalk.addAll(0, connVs);
			return new StepResult(false, false, previousVertex, currentVertex);
		}
		//backtracking
		currentPath.remove(previousVertex);	//here it's a Vertex we are backing from
		currentVertex = (HeuristicsVertex)currentPath.get(currentPath.size() - 1);
		currentStartCost -= graph.getWeightBetween(currentVertex, previousVertex);
		walkedTrough.add(currentVertex);
		return new StepResult(false, true, previousVertex, currentVertex);
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
			DFSSearchResult res = dfsSearch(0, (HeuristicsVertex)startVertex, costLimit);
			if(res.isSolution())
			{
				return Collections.unmodifiableCollection(resultingWay);
			}
			costLimit = res.getCostLimit();
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
			if(walkedTrough.contains(i))
			{//omit cycles
				continue;
			}
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
