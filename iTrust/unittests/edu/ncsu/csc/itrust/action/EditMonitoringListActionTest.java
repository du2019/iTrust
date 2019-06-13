package edu.ncsu.csc.itrust.action;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class EditMonitoringListActionTest extends TestCase {
	EditMonitoringListAction action;
	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		gen.patient1();
		action = new EditMonitoringListAction(TestDAOFactory.getTestInstance(), 9000000000L);
	}

	public void testAddToRemoveFromList() throws Exception {
		assertTrue(action.addToList(1L));
		assertFalse(action.addToList(1L));
		assertTrue(action.removeFromList(1L));
		assertFalse(action.removeFromList(1L));
	}
	
	public void testIsPatientInList() throws Exception {
		action.addToList(1L);
		assertTrue(action.isPatientInList(1));
		assertFalse(action.isPatientInList(2));
		
	}

}
