package edu.ncsu.csc.itrust.dao.remotemonitoring;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.dao.mysql.RemoteMonitoringDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class PatientListTest extends TestCase {
	private RemoteMonitoringDAO rmDAO = TestDAOFactory.getTestInstance().getRemoteMonitoringDAO();
	private TestDataGenerator gen;
	
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.patient2();
		gen.hcp0();
	}

	public void testAddRemoveFromList() throws Exception {
		assertTrue(rmDAO.addPatientToList(2L, 9000000000L));
		assertTrue(rmDAO.removePatientFromList(2L, 9000000000L));
	}

}
