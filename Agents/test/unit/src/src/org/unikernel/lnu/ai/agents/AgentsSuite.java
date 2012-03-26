/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package src.org.unikernel.lnu.ai.agents;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author mcangel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
{
	org.unikernel.lnu.ai.agents.DFSTest.class, org.unikernel.lnu.ai.agents.AlgorithmTest.class
})
public class AgentsSuite
{

	@BeforeClass
	public static void setUpClass() throws Exception
	{
	}

	@AfterClass
	public static void tearDownClass() throws Exception
	{
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}
	
}
