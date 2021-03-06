package org.unikernel.lnu.ai.agents.api;

import java.util.*;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
public abstract class Algorithm
{
	protected Graph graph;
	protected Vertex startVertex;
	protected Vertex endVertex;
	protected ArrayList<StepData> walkedTrough = new ArrayList<StepData>();
	protected ArrayList<Vertex> resultingWay = new ArrayList<Vertex>();
	protected Set<Vertex> walkedVertices = new HashSet<Vertex>();

	public Algorithm()
	{
	}
	
	public Algorithm(Graph graph)
	{
		this.graph = graph;
	}

	public void setGraph(Graph graph)
	{
		this.graph = graph;
	}
	
	public void setStartVertex(Vertex startVertex)
	{
		this.startVertex = startVertex;
	}

	public Vertex getStartVertex()
	{
		return startVertex;
	}
	
	public void setEndVertex(Vertex endVertex)
	{
		this.endVertex = endVertex;
	}

	public Vertex getEndVertex()
	{
		return endVertex;
	}

	public List<StepData> getWalkedTrough()
	{
		return Collections.unmodifiableList(walkedTrough);
	}

	public List<Vertex> getResultingWay()
	{
		return Collections.unmodifiableList(resultingWay);
	}

	public abstract List<Vertex> search();
	
	public void reset(boolean... endVertexTo)
	{
		if(endVertexTo.length > 0 && endVertexTo[0])
		{
			startVertex = null;
			endVertex = null;
		}
		walkedTrough.clear();
		resultingWay.clear();
		walkedVertices.clear();
	}
	
	public static class StepData
	{
		public enum Type {PATH, BACKTRACK, UNKNOWN}
		
		public Graph.Connection connection;
		public Type type;

		public StepData(Graph.Connection connection)
		{
			this.connection = connection;
			this.type = Type.UNKNOWN;
		}
		
		public StepData(Graph.Connection connection, Type type)
		{
			this.connection = connection;
			this.type = type;
		}

		@Override
		public String toString()
		{
			return connection.getFirstVertex() + "->" + connection.getSecondVertex() + ":" + type;
		}
	}
}