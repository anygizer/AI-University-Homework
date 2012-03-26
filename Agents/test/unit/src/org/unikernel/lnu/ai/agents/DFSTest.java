package org.unikernel.lnu.ai.agents;

import java.util.Collection;
import org.junit.*;
import static org.junit.Assert.*;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
public class DFSTest
{
	public DFSTest()
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
	
	@Before
	public void setUp()
	{
	}
	
	@After
	public void tearDown()
	{
	}

//	/**
//	 * Test of step method, of class DFS.
//	 */
//	@Test
//	public void testStep()
//	{
//		System.out.println("step");
//		DFS instance = null;
//		Vertex[] expResult = null;
//		Vertex[] result = instance.step();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}

	/**
	 * Test of search method, of class DFS.
	 */
	@Test
	public void testSearch1()
	{
		System.out.println("search1");
		Graph g = new Graph();
		Vertex s = new Vertex();
		Vertex e = new Vertex();
		g.addVertex(s);
		g.addVertex(e);
		g.connectVertices(s, e, 0.);
		DFS instance = new DFS(g);
		instance.setStartVertex(s);
		instance.setEndVertex(e);
		Vertex[] expResult = new Vertex[]{s, e};
		Collection<Vertex> result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
	
	@Test
	public void testSearch2()
	{
		System.out.println("search2");
		Graph g = new Graph();
		Vertex s = new Vertex();
		Vertex e = new Vertex();
		Vertex a = new Vertex();
		g.addVertex(s);
		g.addVertex(e);
		g.addVertex(a);
		g.connectVertices(s, e, 0.);
		g.connectVertices(s, a, 0.);
		DFS instance = new DFS(g);
		instance.setStartVertex(s);
		instance.setEndVertex(e);
		Vertex[] expResult = new Vertex[]{s, e};
		Collection<Vertex> result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
	
	@Test
	public void testSearch3()
	{
		System.out.println("search3");
		Graph g = new Graph();
		Vertex s = new Vertex("s");
		Vertex e = new Vertex("e");
		Vertex a = new Vertex("a");
		g.addVertex(s);
		g.addVertex(e);
		g.addVertex(a);
		g.connectVertices(s, a, 0.);
		g.connectVertices(a, e, 0.);
		g.connectVertices(s, e, 0.);
		DFS instance = new DFS(g);
		instance.setStartVertex(s);
		instance.setEndVertex(e);
		Vertex[] expResult = new Vertex[]{s, a, e};
		Collection<Vertex> result = instance.search();
		System.out.println("Connected vertexes to s:");
		for(Vertex i : g.getConnectedVertices(s))
		{
			System.out.print(" " + i);
		}
		System.out.println();
		assertArrayEquals(expResult, result.toArray());
	}
	
	@Test
	public void testSearch4()
	{
		System.out.println("search4");
		Graph g = new Graph();
		Vertex s = new Vertex();
		Vertex e = new Vertex();
		Vertex a = new Vertex();
		g.addVertex(s);
		g.addVertex(e);
		g.addVertex(a);
		g.connectVertices(s, a, 0.);
		g.connectVertices(a, e, 0.);
		DFS instance = new DFS(g);
		instance.setStartVertex(s);
		instance.setEndVertex(e);
		Vertex[] expResult = new Vertex[]{s, a, e};
		Collection<Vertex> result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
	
	@Test
	public void testSearch5()
	{
		System.out.println("search5");
		Graph g = new Graph();
		Vertex s = new Vertex("s");
		Vertex e = new Vertex("e");
		Vertex n1 = new Vertex("n1");
		Vertex n2 = new Vertex("n2");
		Vertex n3 = new Vertex("n3");
		g.addVertex(s);
		g.addVertex(e);
		g.addVertex(n1);
		g.addVertex(n2);
		g.addVertex(n3);
		g.connectVertices(s, n1, 0.);
		g.connectVertices(n1, n2, 0.);
		g.connectVertices(n2, n3, 0.);
		g.connectVertices(n1, e, 0.);
		DFS instance = new DFS(g);
		instance.setStartVertex(s);
		instance.setEndVertex(e);
		Vertex[] expResult = new Vertex[]{s, n1, e};
		Collection<Vertex> result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
}