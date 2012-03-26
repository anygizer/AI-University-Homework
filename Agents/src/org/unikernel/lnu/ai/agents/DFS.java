package org.unikernel.lnu.ai.agents;

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
	public DFS(Graph graph)
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