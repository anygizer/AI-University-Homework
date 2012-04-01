/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.unikernel.lnu.ai.agents;

import java.util.Collection;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.HeuristicsVertex;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author uko
 */
public class UniformCostSearchTest
{
	public UniformCostSearchTest()
	{
	}
	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}
	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}
	/**
	 * Test of step method, of class UniformCostSearch.
	 */
//	@Test
//	public void testStep()
//	{
//		System.out.println("step");
//		UniformCostSearch instance = null;
//		Vertex[] expResult = null;
//		Vertex[] result = instance.step();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}
	/**
	 * Test of search method, of class UniformCostSearch.
	 */
	@Test
	public void testSearch()
	{
		System.out.println("search1");
		Graph g = new Graph();
		HeuristicsVertex s = new HeuristicsVertex("s");
		HeuristicsVertex e = new HeuristicsVertex("e");
		g.addVertex(s);
		g.addVertex(e);
		g.connectVertices(s, e, 0.);
		UniformCostSearch instance = new UniformCostSearch(g);
		instance.setStartVertex(s);
		instance.setEndVertex(e);
		Vertex[] expResult = new Vertex[]{s, e};
		Collection result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
	
	@Test
	public void testSearch2()
	{
		System.out.println("search2");
		Graph g = new Graph();
		HeuristicsVertex s = new HeuristicsVertex("s");
		HeuristicsVertex e = new HeuristicsVertex("e");
		HeuristicsVertex k = new HeuristicsVertex("k");
		g.addVertex(s);
		g.addVertex(e);
		g.addVertex(k);
		g.connectVertices(s, e, 0.);
		g.connectVertices(e, k, 1.);
		UniformCostSearch instance = new UniformCostSearch(g);
		instance.setStartVertex(s);
		instance.setEndVertex(k);
		Vertex[] expResult = new Vertex[]{s, e, k};
		Collection result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
	
	@Test
	public void testSearch3()
	{
		System.out.println("search2");
		Graph g = new Graph();
		HeuristicsVertex s = new HeuristicsVertex("s");
		HeuristicsVertex e = new HeuristicsVertex("e");
		HeuristicsVertex k = new HeuristicsVertex("k");
		g.addVertex(s);
		g.addVertex(e);
		g.addVertex(k);
		g.connectVertices(s, e, 0.);
		g.connectVertices(e, k, 1.);
		UniformCostSearch instance = new UniformCostSearch(g);
		instance.setStartVertex(s);
		instance.setEndVertex(k);
		Vertex[] expResult = new Vertex[]{s, e, k};
		Collection result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
}
