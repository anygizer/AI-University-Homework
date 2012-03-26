package org.unikernel.lnu.ai.graph;

import java.awt.Point;

/**
 *
 * @author uko, mcangel
 */
public class Vertex
{
	private Point preferredLocation;
	private String label;

	public Vertex(String label, Point preferredLocation)
	{
		this.label = label;
		this.preferredLocation = preferredLocation;
	}
	
	public Vertex(String label)
	{
		this(label, new Point(0, 0));
	}
	
	public Vertex()
	{
		this("" + System.currentTimeMillis());
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public Point getPreferredLocation()
	{
		return preferredLocation;
	}

	public void setPreferredLocation(Point preferredLocation)
	{
		this.preferredLocation = preferredLocation;
	}

	@Override
	public String toString()
	{
		return label;
	}
}