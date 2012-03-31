package org.unikernel.lnu.ai.agents;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
public class DFS extends Algorithm
{
	private ArrayList<Vertex> currentPath = new ArrayList<Vertex>();
	private ArrayList<Vertex> pathToWalk = new ArrayList<Vertex>();
	public DFS(Graph graph)
	{
		super(graph);
	}

	@Override
	public StepResult step()
	{
		if(startVertex == null || endVertex == null || !resultingWay.isEmpty())
		{
			return null;
		}
		if(currentPath.isEmpty())
		{//first step
			pathToWalk.add(startVertex);
		}
		Vertex previousVertex = currentPath.isEmpty() ?
				null : currentPath.get(currentPath.size()-1);
		Vertex currentVertex;
		while (walkedTrough.contains(pathToWalk.get(0)))
		{//omit cycles
			pathToWalk.remove(0);
		}
		//if it's first step or forthtracking
		if(previousVertex == null || graph.areConnected(previousVertex, pathToWalk.get(0)))
		{	
			currentVertex = pathToWalk.remove(0);
			currentPath.add(currentVertex);
			walkedTrough.add(currentVertex);
			if (currentVertex.equals(endVertex))
			{//goal reached
				resultingWay.addAll(currentPath);
				return new StepResult(true, false, previousVertex, currentVertex);
			}
			pathToWalk.addAll(0, graph.getConnectedVertices(currentVertex));	//add all connected vertices
			pathToWalk.remove(previousVertex);									//except the parent one
			return new StepResult(false, false, previousVertex, currentVertex);
		}
		//backtracking
		currentPath.remove(previousVertex);
		currentVertex = currentPath.get(currentPath.size()-1);
		walkedTrough.add(currentVertex);
		return new StepResult(false, true, previousVertex, currentVertex);
	}

	@Override
	public Collection<Vertex> search()
	{
		if(startVertex != null && endVertex != null && dfsSearch(startVertex))
		{
			return Collections.unmodifiableList(resultingWay);
		}
		return null;
	}
	
	private boolean dfsSearch(Vertex currentVertex)
	{
		walkedTrough.add(currentVertex);
		// Check if a goal was reached
		if (currentVertex.equals(endVertex))
		{
			resultingWay.add(currentVertex);
			return true;
		} else
		{
			// iterate through all connected vertices
			for (Vertex nextVertex : graph.getConnectedVertices(currentVertex))
			{
				// omit cycles
				if (!walkedTrough.contains(nextVertex))
				{
					if (dfsSearch(nextVertex))
					{
						// add Vertex to the resulting way
						resultingWay.add(0, currentVertex);
						return true;
					} else	// remove Vertexes on the backtrack from the resulting way
					{
//						resultingWay.remove(nextVertex);
					}
				}
			}
			// if there is no result - return false
			return false;
		}
	}	
}