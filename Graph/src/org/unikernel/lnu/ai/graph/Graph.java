package org.unikernel.lnu.ai.graph;

import java.util.*;

/**
 * A simple bidirectional graph featuring a creation of the graph with 
 * an ability to add vertices and management of the connections between them.
 *
 * @author uko, mcangel
 */
public class Graph
{
	private Set<Vertex> vertices;
	private Map<Connection, Double> connections;
	private Map<Vertex, Collection<Connection>> VertexConnections;

	/**
	 * Creates a new graph
	 */
	public Graph()
	{
		vertices = new HashSet<Vertex>();
		connections = new HashMap<Connection, Double>();
		VertexConnections = new HashMap<Vertex, Collection<Connection>>();
	}
	
	/**
	 * Adds a new vertex to the graph.
	 * @param vertex Vertex to add to the graph.
	 */
	public void addVertex(Vertex vertex)
	{
		vertices.add(vertex);
	}
	
	/**
	 * Adds as many new vertices to the graph as provided.
	 * @param vertices Vertex'es to add to the graph.
	 */
	public void addVertices(Vertex... vertices)
	{
		for(Vertex i : vertices)
		{
			addVertex(i);
		}
	}
	
	public void removeVertex(Vertex vertex)
	{
		if (VertexConnections.containsKey(vertex))
		{
			for (Object con : VertexConnections.get(vertex).toArray())
			{
				Connection i = (Connection) con;
				this.disconnectVertices(i.getFirstVertex(), i.getSecondVertex());
			}
		}
		this.vertices.remove(vertex);
	}
	
	public Set<Vertex> getVertices()
	{
		return Collections.unmodifiableSet(this.vertices);
	}

	/**
	 * Adds a connection between two vertexes.
	 * @param firstVertex first vertex
	 * @param secondVertex second vertex
	 * @param weight a weight of the connection
	 */
	public Connection connectVertices(Vertex firstVertex, Vertex secondVertex, double weight)
	{
		//if the specified elements are in the circuit
		if (vertices.contains(firstVertex) && vertices.contains(secondVertex))
		{
			//make a connection
			Connection conn = new Connection(firstVertex, secondVertex);

			//put it in the connections
			connections.put(conn, weight);

			//put it in the map of vertex->connection for each element
			if (!VertexConnections.containsKey(firstVertex))
			{
				VertexConnections.put(firstVertex, new ArrayList<Connection>());
			}
			if (!VertexConnections.containsKey(secondVertex))
			{
				VertexConnections.put(secondVertex, new ArrayList<Connection>());
			}
			VertexConnections.get(firstVertex).add(conn);
			VertexConnections.get(secondVertex).add(conn);
			return conn;
		}
		return null;
	}

	/**
	 * Removes a connection between two vertexes.
	 * @param firstVertex first vertex
	 * @param secondVertex second vertex
	 */
	public void disconnectVertices(Vertex firstVertex, Vertex secondVertex)
	{
		Connection conn = new Connection(firstVertex, secondVertex);
		if (connections.remove(conn) != null)
		{
			VertexConnections.get(firstVertex).remove(conn);
			VertexConnections.get(secondVertex).remove(conn);
			if (VertexConnections.get(firstVertex).isEmpty())
			{
				VertexConnections.remove(firstVertex);
			}
			if (VertexConnections.get(secondVertex).isEmpty())
			{
				VertexConnections.remove(secondVertex);
			}
		}
	}
	
	public Collection<Vertex> getConnectedVertices(Vertex vertex)
	{
		Collection<Vertex> connectedVertices = new ArrayList<Vertex>();
		for(Connection i : VertexConnections.get(vertex))
		{
			connectedVertices.add(i.getOtherVertex(vertex));
		}
		return connectedVertices;
	}
	
	public double getWeightBetween(Vertex sourceVertex, Vertex targetVertex)
	{
		return connections.get(getConnectionBetween(sourceVertex, targetVertex));
	}
	
	public void setWeightBetween(Vertex sourceVertex, Vertex targetVertex, double weight)
	{
		connections.put(getConnectionBetween(sourceVertex, targetVertex), weight);
	}
	
	public Connection getConnectionBetween(Vertex sourceVertex, Vertex targetVertex)
	{
		if(sourceVertex == null || targetVertex == null)
		{
			return  null;
		}
		for(Connection i : VertexConnections.get(sourceVertex))
		{
			if(i.getOtherVertex(sourceVertex).equals(targetVertex))
			{
				return i;
			}
		}
		return null;
	}
	
	public boolean areConnected(Vertex sourceVertex, Vertex targetVertex)
	{
		return getConnectionBetween(sourceVertex, targetVertex) != null;
	}
	
	public class Connection
	{
		private Vertex firstVertex;
		private Vertex secondVertex;

		public Connection(Vertex firstVertex, Vertex secondVertex)
		{
			this.firstVertex = firstVertex;
			this.secondVertex = secondVertex;
		}

		@Override
		public boolean equals(Object o)
		{
			if (o.getClass().equals(Connection.class))
			{
				Connection temp = (Connection) o;
				if (this.firstVertex.equals(temp.firstVertex) && this.secondVertex.equals(temp.secondVertex))
				{
					return true;
				}
			}
			return false;
		}

		@Override
		public int hashCode()
		{
			int hash = 5;
			hash = 67 * hash + (this.firstVertex != null ? this.firstVertex.hashCode() : 0);
			hash = 67 * hash + (this.secondVertex != null ? this.secondVertex.hashCode() : 0);
			return hash;
		}

		/**
		 * @return the firstVertex
		 */
		public Vertex getFirstVertex()
		{
			return firstVertex;
		}

		/**
		 * @return the secondVertex
		 */
		public Vertex getSecondVertex()
		{
			return secondVertex;
		}
		
		/**
		 * Returns the vertex on the other hand of this connection.
		 * @param vertex 
		 * @return 
		 */
		public Vertex getOtherVertex(Vertex vertex)
		{
			if(firstVertex.equals(vertex))
			{
				return secondVertex;
			}
			if (secondVertex.equals(vertex))
			{
				return firstVertex;
			}
			return null;
		}
	}
}