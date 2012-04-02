package org.unikernel.lnu.ai.agents;

import java.util.Collections;
import java.util.List;
import org.openide.util.lookup.ServiceProvider;
import org.unikernel.lnu.ai.agents.api.Algorithm;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
@ServiceProvider(service=Algorithm.class)
public class DFS extends Algorithm
{

	public DFS()
	{
	}
	
	public DFS(Graph graph)
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
		reset();
		if(dfsSearch(startVertex))
		{
			return Collections.unmodifiableList(resultingWay);
		}
		return null;
	}
	
	private boolean dfsSearch(Vertex currentVertex)
	{
		walkedVertices.add(currentVertex);
		if (currentVertex.equals(endVertex))
		{//a goal was reached
			resultingWay.add(currentVertex);
			return true;
		}
		for (Vertex nextVertex : graph.getConnectedVertices(currentVertex))
		{// iterate through all connected vertices
			if (!walkedVertices.contains(nextVertex))
			{// omit cycles
				//step forward
				walkedTrough.add(new StepData(graph.getConnectionBetween(currentVertex, nextVertex), StepData.Type.PATH));
				if (dfsSearch(nextVertex))
				{
					// add Vertex to the resulting way
					resultingWay.add(0, currentVertex);
					return true;
				}
				//backtracking
				walkedTrough.add(new StepData(graph.getConnectionBetween(nextVertex, currentVertex), StepData.Type.BACKTRACK));
			}
		}
		// if there is no result - return false
		return false;
	}
}