package org.unikernel.lnu.ai.graph;

import java.awt.Point;

/**
 *
 * @author mcangel
 */
public class HeuristicsVertex extends Vertex implements Comparable<HeuristicsVertex>
{
	private int heuristics;

	public HeuristicsVertex(int heuristics, String label, Point preferredLocation)
	{
		super(label, preferredLocation);
		this.heuristics = heuristics;
	}

	public HeuristicsVertex(int heuristics, String label)
	{
		super(label);
		this.heuristics = heuristics;
	}

	public HeuristicsVertex(int heuristics)
	{
		this.heuristics = heuristics;
	}
	
	public HeuristicsVertex(String label, Point preferredLocation)
	{
		super(label, preferredLocation);
	}

	public HeuristicsVertex(String label)
	{
		super(label);
	}

	public HeuristicsVertex()
	{
	}

	public int getHeuristics()
	{
		return heuristics;
	}

	public void setHeuristics(int heuristics)
	{
		this.heuristics = heuristics;
	}
	
	@Override
	public int compareTo(HeuristicsVertex t)
	{
		return this.heuristics-t.getHeuristics();
	}
}
