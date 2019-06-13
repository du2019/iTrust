package edu.ncsu.csc.itrust.dao.remotemonitoring;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.RemoteMonitoringDataBean;
import edu.ncsu.csc.itrust.dao.mysql.RemoteMonitoringDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class PatientDataTest extends TestCase {
	private RemoteMonitoringDAO rmDAO = TestDAOFactory.getTestInstance().getRemoteMonitoringDAO();
	private RemoteMonitoringDAO EvilrmDAO = EvilDAOFactory.getEvilInstance().getRemoteMonitoringDAO();
	private TestDataGenerator gen;
	
	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.patient2();
		gen.hcp0();
		gen.remoteMonitoring1();
	}

	public void testStoreRetrievePatientNormalData() throws Exception {
		rmDAO.storePatientData(2, 100, 70, 80, "self-reported", 2);
		List<RemoteMonitoringDataBean> d = rmDAO.getPatientsData(9000000000L);
		assertEquals(2, d.get(0).getPatientMID());
		assertEquals(100, d.get(0).getSystolicBloodPressure());
		assertEquals(70, d.get(0).getDiastolicBloodPressure());
		assertEquals(80, d.get(0).getGlucoseLevel());
	}
	
	public void testBadStoreRetrievePatientNormalDataBad() throws Exception{
		try {
			EvilrmDAO.storePatientData(2, 100, 70, 80, "self-reported", 2);
			fail();
		} catch (DBException e){
			assertSame(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
		
	}

	public void testBadStoreRetrievePatientGlucoseOnlyDataBad() throws Exception{
		try {
			EvilrmDAO.storePatientData(2, 80, "self-reported", 2);
			fail();
		} catch (DBException e){
			assertSame(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
		
	}
	
	public void testBadStoreRetrievePatientBPOnlyDataBad() throws Exception{
		try {
			EvilrmDAO.storePatientData(2, 80, 100, "self-reported", 2);
			fail();
		} catch (DBException e){
			assertSame(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
		
	}

	public void testValidatePR() throws Exception{
		try {
			rmDAO.validatePR(2, 1);
			assert(true);
		}
		catch (iTrustException e){
			fail();
		}
	}

	public void testValidatePRError() throws Exception{
		try {
			rmDAO.validatePR(1, 2);
			fail();
		}
		catch (iTrustException e){
			assertEquals("Representer is not valid for patient 2", e.getMessage());
		}
	}

	public void testRemovePatientFromListBad() throws Exception{
		try {
			EvilrmDAO.removePatientFromList(1, 2);
			fail();
		} catch (DBException e){
			assertSame(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
		
	}

}
