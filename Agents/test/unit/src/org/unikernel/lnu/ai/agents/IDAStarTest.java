package org.unikernel.lnu.ai.agents;

import java.util.Collection;
import org.junit.*;
import static org.junit.Assert.*;
import org.unikernel.lnu.ai.graph.Graph;
import org.unikernel.lnu.ai.graph.HeuristicsVertex;
import org.unikernel.lnu.ai.graph.Vertex;

/**
 *
 * @author mcangel
 */
public class IDAStarTest
{
	Graph g;
	IDAStar instance;
	
	public IDAStarTest()
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
		g = new Graph();
		instance = new IDAStar(g);
	}
	
	@After
	public void tearDown()
	{
		g = null;
		instance = null;
	}

//	/**
//	 * Test of step method, of class IDAStar.
//	 */
//	@Test
//	public void testStep()
//	{
//		System.out.println("step");
//		IDAStar instance = null;
//		Vertex[] expResult = null;
//		Vertex[] result = instance.step();
//		assertEquals(expResult, result);
//		// TODO review the generated test code and remove the default call to fail.
//		fail("The test case is a prototype.");
//	}

	/**
	 * Test of search method, of class IDAStar.
	 */
	@Test
	public void testSearch1()
	{
		System.out.println("search1");
		HeuristicsVertex r = new HeuristicsVertex(4, "r");
		HeuristicsVertex n1 = new HeuristicsVertex(3, "n1");
		HeuristicsVertex n2 = new HeuristicsVertex(3, "n2");
		HeuristicsVertex n3 = new HeuristicsVertex(2, "n3");
		HeuristicsVertex n4 = new HeuristicsVertex(4, "n4");
		HeuristicsVertex n5 = new HeuristicsVertex(1, "n5");
		HeuristicsVertex n6 = new HeuristicsVertex(1, "n6");
		HeuristicsVertex n7 = new HeuristicsVertex(2, "n7");
		HeuristicsVertex n8 = new HeuristicsVertex(1, "n8");
		HeuristicsVertex n9 = new HeuristicsVertex(2, "n9");
		HeuristicsVertex n10 = new HeuristicsVertex(1, "n10");
		HeuristicsVertex n11 = new HeuristicsVertex(0, "n11");
		g.addVertices(r, n1, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11);
		g.connectVertices(r, n1, 1);
		g.connectVertices(r, n2, 1);
		g.connectVertices(n1, n3, 1);
		g.connectVertices(n1, n4, 1);
		g.connectVertices(n2, n5, 1);
		g.connectVertices(n2, n6, 1);
		g.connectVertices(n3, n7, 1);
		g.connectVertices(n4, n8, 1);
		g.connectVertices(n5, n9, 1);
		g.connectVertices(n6, n10, 1);
		g.connectVertices(n10, n11, 1);
		instance.setStartVertex(r);
		instance.setEndVertex(n11);
		HeuristicsVertex[] expResult = new HeuristicsVertex[]{r, n2, n6, n10, n11};
		Collection result = instance.search();
		assertArrayEquals(expResult, result.toArray());
	}
}
