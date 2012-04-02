package org.unikernel.lnu.ai.agents;

import java.util.*;
import org.openide.util.lookup.ServiceProvider;
import org.unikernel.lnu.ai.agents.api.Algorithm;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.HeuristicsVertex;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author uko
 */
@ServiceProvider(service=Algorithm.class)
public class UniformCostSearch extends Algorithm
{

	public UniformCostSearch(){}

	
	public UniformCostSearch(Graph graph)
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
		if(uniformCostSearch(startVertex))
		{
			return Collections.unmodifiableList(resultingWay);
		}
		return null;
	}
	
	private boolean uniformCostSearch(Vertex startVertex)
	{
		Map<Vertex, Vertex> movementMap = new HashMap<Vertex, Vertex>();
		final Map<Vertex, Integer> weights = new HashMap<Vertex, Integer>();
		weights.put(startVertex, new Integer(0));
		ArrayList<Vertex> frontier = new ArrayList<Vertex>();
		frontier.add(startVertex);
		while(frontier.size()>0)
		{
			Vertex currentVertex = (HeuristicsVertex)Collections.min(frontier,
					new Comparator<Vertex>()
					{
						@Override
						public int compare(Vertex entry1, Vertex entry2)
						{
							return weights.get(entry1).compareTo(weights.get(entry2));
						}
					});
			frontier.remove(currentVertex);
			if (currentVertex.equals(endVertex))
			{
				Vertex walker = currentVertex;
				do
				{
					resultingWay.add(walker);
					walker = movementMap.get(walker);
				}
				while(movementMap.containsKey(walker));
				resultingWay.add(walker);
				Collections.reverse(resultingWay);
				return true;
			}
			walkedVertices.add(currentVertex);
			for (Vertex nextVertex : graph.getConnectedVertices(currentVertex))
			{
				if (!walkedVertices.contains(nextVertex))
				{
					boolean thisPathIsBetter;
					int pathWaightToNextVertex = weights.get(currentVertex)+(int)graph.getWeightBetween(currentVertex, nextVertex);
					if(!frontier.contains(nextVertex))
					{
						frontier.add(nextVertex);
						thisPathIsBetter = true;
					}
					else
					{
						if(weights.get(nextVertex) >pathWaightToNextVertex)
							thisPathIsBetter = true;
						else
							thisPathIsBetter = false;
					}
					if (thisPathIsBetter)
					{
						weights.put(nextVertex, pathWaightToNextVertex);
						movementMap.put(nextVertex, currentVertex);
						walkedTrough.add(new StepData(graph.getConnectionBetween(currentVertex, nextVertex)));
					}
				}
			}
			
		}
		return false;
	}
	
	private class MinimumArrayList<E extends Comparable> extends ArrayList<E>
	{
		public E min()
		{
			E min = this.get(0);
			for (E i : this)
				if (i.compareTo(min) < 0)
					min = i;
			return min;
		}
	}
}
