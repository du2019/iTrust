package edu.ncsu.csc.itrust.action;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.MedicationBean;
import edu.ncsu.csc.itrust.beans.OverrideReasonBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class UpdateORCListActionTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private UpdateReasonCodeListAction action;
	private final static long performingAdmin = 9000000001l;
	private TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {
		action = new UpdateReasonCodeListAction(factory, performingAdmin);
		gen.clearAllTables();
		gen.admin1();
	}

	private String getAddCodeSuccessString(MedicationBean proc) {
		return "Success: " + proc.getNDCode() + " - " + proc.getDescription() + " added";
	}

	public void testEvilFactory() {
		action = new UpdateReasonCodeListAction(EvilDAOFactory.getEvilInstance(), 0l);
		OverrideReasonBean mb = new OverrideReasonBean();
		mb.setDescription("description");
		mb.setORCode("00010");
		try {
			String x = action.addORCode(mb);
			assertEquals(
					"A database exception has occurred. Please see the log in the console for stacktrace", x);
		} catch (Exception e) {

		}

		try {
			String x = action.updateInformation(mb);
			assertEquals(
					"A database exception has occurred. Please see the log in the console for stacktrace", x);
		} catch (Exception e) {

		}
	}

	private void addEmpty(String code) throws Exception {
		OverrideReasonBean orc = new OverrideReasonBean(code, "0");
		String result = action.addORCode(orc);
		assert(result.contains("Success"));
		orc = factory.getORCodesDAO().getORCode(code);
		assertEquals("0", orc.getDescription());
	}

	public void testAddORCode() throws Exception {
		final String code = "99998";
		final String desc = "UpdateORCodeListActionTest testAddORCode";
		OverrideReasonBean orb = new OverrideReasonBean(code, desc);
		
		/* DO NOT REMOVE! This must be stored in a variable. DO NO QUESTION THE JAVA! */
		String result = action.addORCode(orb);
		
		assert(result.contains("Success"));
		System.out.println(orb);
		orb = factory.getORCodesDAO().getORCode(code);
		assertEquals(desc, orb.getDescription());
	}

	
	public void testAddDuplicate() throws DBException, FormValidationException {
		final String code = "99997";
		final String descrip0 = "description 0";
		OverrideReasonBean orc = new OverrideReasonBean(code, descrip0);
		
		String result = action.addORCode(orc);
		
		assert(result.contains("Success"));
		orc.setDescription("description 1");
		
		result = action.addORCode(orc);
		assertEquals("Error: Code already exists.", result);
		orc = factory.getORCodesDAO().getORCode(code);
		assertEquals(descrip0, orc.getDescription());
	}

	public void testUpdateNDInformation0() throws Exception {
		final String code = "88888";
		final String desc = "new descrip 0";
		OverrideReasonBean orc = new OverrideReasonBean(code);
		addEmpty(code);
		orc.setDescription(desc);
		String result = action.updateInformation(orc);
		assertEquals("Success: 1 row(s) updated", result);
		orc = factory.getORCodesDAO().getORCode(code);
		assertEquals(desc, orc.getDescription());
	}

	public void testUpdateNonExistent() throws Exception {
		final String code = "99996";
		OverrideReasonBean orc = new OverrideReasonBean(code, "shouldnt be here");
		String result = action.updateInformation(orc);
		assertEquals("Error: Code not found.", result);
		
		OverrideReasonBean orc2 = new OverrideReasonBean("99995", "Test");
		String result2 = action.addORCode(orc2);
		assert(result2.contains("Success"));
		
		
		assertEquals(null, factory.getORCodesDAO().getORCode(code));
		assertEquals(1, factory.getORCodesDAO().getAllORCodes().size());
	}
}
