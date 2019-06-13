package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.risk.RiskChecker;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ChronicDiseaseRiskActionTest extends TestCase {
	private TestDataGenerator gen = new TestDataGenerator();
	private DAOFactory factory = TestDAOFactory.getTestInstance();

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.patient2();
	}

	public void testGetPatient2() throws Exception {
		ChronicDiseaseRiskAction action = new ChronicDiseaseRiskAction(factory, 2L, "2");
		assertEquals(2L, action.getPatientID());
		assertEquals("Andy Programmer", action.getUserName());
		List<RiskChecker> atRisk = action.getDiseasesAtRisk();
		assertEquals(2, atRisk.size());
		List<TransactionBean> transList = factory.getTransactionDAO().getAllTransactions();
		assertEquals(1, transList.size());
		assertEquals(TransactionType.IDENTIFY_RISK_FACTORS,transList.get(0).getTransactionType());
		assertEquals(2L,transList.get(0).getLoggedInMID());
		assertEquals("",transList.get(0).getAddedInfo());
		
		
		//The test for the Chronic Disease mediator will assert the rest of these
	}

}
