package edu.ncsu.csc.itrust.action;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import edu.ncsu.csc.itrust.exception.DBException;

public class ViewOfficeVisitActionTest extends TestCase {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private ViewOfficeVisitAction action;
	private TestDataGenerator gen;
	
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
	}

	public void testViewOfficeVisit() throws Exception {
		String hcp = null;
		try {
			action = new ViewOfficeVisitAction(factory, 2L, "955");
			assertEquals(955L, action.getOvID());
			assertEquals(2L, action.getPid());
			assertEquals(955L, action.getOfficeVisit().getID());
			assertEquals(3, action.getPrescriptions().size());
			hcp = action.getHCPName(9000000000L);
			assertNotNull(hcp);
			hcp = action.getHCPName(9000000099L);
			assertEquals("User does not exist", hcp);
			action = new ViewOfficeVisitAction(factory, 2L, "0");
			fail("should have been iTrustException");
		}
		catch (DBException dbe) {
			
		}
		catch (iTrustException e) {
			assertEquals("Office Visit "+Long.valueOf("0")+" with Patient MID 2 does not exist", e.getMessage());
		}
	}

	public void testCanRepresent() throws Exception {
		try {
			action = new ViewOfficeVisitAction(factory, 2L, "1", "11");
		} catch (Exception e) {
			fail("No exception should be thrown; exception: " + e.toString());
		}
	}
	
	public void testCannotRepresent() throws Exception {
		try {
			action = new ViewOfficeVisitAction(factory, 1L, "2", "955");
			fail("exception should have been thrown");
		} catch (iTrustException e) {
			assertEquals("You do not represent this patient, contact your HCP to represent this patient", 
					e.getMessage());
		}
	}
}
