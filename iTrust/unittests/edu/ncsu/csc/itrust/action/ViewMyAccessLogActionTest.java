package edu.ncsu.csc.itrust.action;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.TransactionBean;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ViewMyAccessLogActionTest extends TestCase {
	ViewMyAccessLogAction action;
	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		gen.hcp3();
		gen.er4();
		gen.uap1();
		gen.admin1();
		gen.patient1();
		gen.patient2();
		action = new ViewMyAccessLogAction(TestDAOFactory.getTestInstance(), 2L);
	}

	public void testNoProblems() throws Exception {
		String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		List<TransactionBean> accesses = action.getAccesses(today, today, false);
		assertEquals(0, accesses.size());
	}
	
	public void testGetAccessesBadData() throws Exception {
		gen.transactionLog();
		List<TransactionBean> accesses = action.getAccesses(null, null, false);
		assertEquals(5, accesses.size());
		for (TransactionBean t : accesses) {
			assertEquals(9000000000L, t.getLoggedInMID());
			assertEquals(2L, t.getSecondaryMID());
			assertEquals("Viewed patient records", t.getAddedInfo());
		}
		// note: the actual bounding is not done here, see the DAO test
		try {
			action.getAccesses("", "", false);
			fail("exception should have been thrown");
		} catch (FormValidationException e) {
			assertEquals(1, e.getErrorList().size());
			assertEquals("Enter dates in MM/dd/yyyy", e.getErrorList().get(0));
		}
	}
	
	public void testGetAccessesByRole() throws Exception {
		gen.transactionLog3();
		action = new ViewMyAccessLogAction(TestDAOFactory.getTestInstance(), 1L);
		List<TransactionBean> accesses = action.getAccesses(null, null, true);
		assertEquals("DLHCP", accesses.get(0).getRole());
		assertEquals("DLHCP", accesses.get(1).getRole());
		assertEquals("Emergency Responder", accesses.get(2).getRole());
		assertEquals("LHCP", accesses.get(3).getRole());
		assertEquals("LHCP", accesses.get(4).getRole());
		assertEquals("LHCP", accesses.get(5).getRole());
		assertEquals("Personal Health Representative", accesses.get(6).getRole());
		assertEquals("UAP", accesses.get(7).getRole());
		assertEquals("UAP", accesses.get(8).getRole());
	}

	public void testDefaultNoList() throws Exception {
		String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		assertEquals(today, action.getDefaultStart(new ArrayList<TransactionBean>()));
		assertEquals(today, action.getDefaultEnd(new ArrayList<TransactionBean>()));
	}

	public void testDefaultWithList() throws Exception {
		String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date());
		ArrayList<TransactionBean> list = new ArrayList<TransactionBean>();
		TransactionBean t = new TransactionBean();
		t.setTimeLogged(new Timestamp(System.currentTimeMillis()));
		list.add(t);
		assertEquals(today, action.getDefaultStart(list));
		assertEquals(today, action.getDefaultEnd(list));
	}
}
