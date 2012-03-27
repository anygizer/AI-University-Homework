package org.unikernel.lnu.ai.agents;

import java.util.*;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
public abstract class Algorithm
{
	Graph graph;
	Vertex startVertex;
	Vertex endVertex;
	protected Set<Vertex> walkedTrough = new HashSet<Vertex>();
	protected ArrayList<Vertex> resultingWay = new ArrayList<Vertex>();

	public Algorithm(Graph graph)
	{
		this.graph = graph;
	}
	
	public void setStartVertex(Vertex startVertex)
	{
		this.startVertex = startVertex;
	}
	public void setEndVertex(Vertex endVertex)
	{
		this.endVertex = endVertex;
	}

	public Set<Vertex> getWalkedTrough()
	{
		return Collections.unmodifiableSet(walkedTrough);
	}

	public List<Vertex> getResultingWay()
	{
		return Collections.unmodifiableList(resultingWay);
	}

	public abstract Vertex[] step();
	public abstract Collection<Vertex> search();
}