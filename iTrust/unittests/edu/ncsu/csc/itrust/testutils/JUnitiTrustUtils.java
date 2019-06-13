package edu.ncsu.csc.itrust.testutils;

import static junit.framework.Assert.assertEquals;
import java.util.List;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;

public class JUnitiTrustUtils {

	public static void assertTransactionOnly(TransactionType transType, long loggedInMID, long secondaryMID,
			String addedInfo) throws DBException {
		List<TransactionBean> transList = TestDAOFactory.getTestInstance().getTransactionDAO()
				.getAllTransactions();
		assertEquals("Only one transaction should have been logged", 1, transList.size());
		assertTransaction(transType, loggedInMID, secondaryMID, addedInfo, transList.get(0));
	}

	public static void assertTransactionsNone() throws DBException {
		assertEquals("No transactions should have been logged", 0, TestDAOFactory.getTestInstance()
				.getTransactionDAO().getAllTransactions().size());
	}

	private static void assertTransaction(TransactionType transType, long loggedInMID, long secondaryMID,
			String addedInfo, TransactionBean trans) {
		assertEquals(transType, trans.getTransactionType());
		assertEquals(loggedInMID, trans.getLoggedInMID());
		assertEquals(secondaryMID, trans.getSecondaryMID());
		assertEquals(addedInfo, trans.getAddedInfo());
	}

}
