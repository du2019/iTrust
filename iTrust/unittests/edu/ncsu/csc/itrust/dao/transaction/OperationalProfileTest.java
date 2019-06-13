package edu.ncsu.csc.itrust.dao.transaction;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.OperationalProfile;
import edu.ncsu.csc.itrust.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class OperationalProfileTest extends TestCase {
	private TestDataGenerator gen;
	private TransactionDAO transDAO = TestDAOFactory.getTestInstance().getTransactionDAO();

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.operationalProfile();
		gen.tester();
	}

	public void testGetOperationalProfile() throws Exception {
		OperationalProfile op = transDAO.getOperationalProfile();
		Integer[] totalCounts =     {0, 1, 2, 2, 4, 5, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		Integer[] patientCounts =   {0, 1, 2, 1, 2, 0, 1, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 1, 2, 1, 1, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		Integer[] personnelCounts = {0, 0, 0, 1, 2, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		assertEquals(34, op.getNumTotalTransactions());
		assertEquals(26, op.getNumPatientTransactions());
		assertEquals(8, op.getNumPersonnelTransactions());
		for (TransactionType type : TransactionType.values()) {
			assertEquals("for type " + type.getDescription() + "(" + type.getCode() + ")", totalCounts[type.getCode()],
					op.getTotalCount().get(type));
		}
		for (TransactionType type : TransactionType.values()) {
			assertEquals("for type " + type.getDescription() + "(" + type.getCode() + ")",
					patientCounts[type.getCode()], op.getPatientCount().get(type));
		}
		for (TransactionType type : TransactionType.values()) {
			assertEquals("for type " + type.getDescription() + "(" + type.getCode() + ")", personnelCounts[type
					.getCode()], op.getPersonnelCount().get(type));
		}
	}
	
	public void testOperationProfileException() throws Exception {
		TransactionDAO evilTranDAO = EvilDAOFactory.getEvilInstance().getTransactionDAO();
		try{
			evilTranDAO.getAllTransactions();
			fail("exception should have been thrown");
		}catch(DBException e){
			assertEquals(EvilDAOFactory.MESSAGE, e.getSQLException().getMessage());
		}
	}
}
