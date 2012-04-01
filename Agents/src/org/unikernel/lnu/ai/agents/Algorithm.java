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
	protected ArrayList<StepData> walkedTrough = new ArrayList<StepData>();
	protected ArrayList<Vertex> resultingWay = new ArrayList<Vertex>();
	protected Set<Vertex> walkedVertices = new HashSet<Vertex>();

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
	
	public class StepData
	{
		public Graph.Connection connection;
		public boolean backtracking = false;

		public StepData(Graph.Connection connection)
		{
			this.connection = connection;
		}
		
		public StepData(Graph.Connection connection, boolean backtracking)
		{
			this.connection = connection;
			this.backtracking = backtracking;
		}

		@Override
		public String toString()
		{
			return connection.getFirstVertex() + "->" + connection.getSecondVertex() + ":" + backtracking;
		}
	}
}