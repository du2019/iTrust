package edu.ncsu.csc.itrust.action;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;
import java.util.Date;
import java.util.List;

public class ViewHelperActionTest extends TestCase {
	private long mid = 1L;
	
	protected void setUp() throws Exception {
	}
	
	public void testUpdateUserPrefs() throws FormValidationException, SQLException, iTrustException {
		assertTrue(ViewHelperAction.calculateColor("000000", "FFFFFF", 0).equals("000000"));
		assertTrue(ViewHelperAction.calculateColor("000000", "FFFFFF", 1.0).equals("FFFFFF"));
		assertTrue(ViewHelperAction.calculateColor("000000", "FFFFFF", 0.5).equals("7F7F7F"));
	}

}
