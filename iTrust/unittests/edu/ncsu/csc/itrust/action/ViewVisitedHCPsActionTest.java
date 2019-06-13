package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.HCPVisitBean;
import edu.ncsu.csc.itrust.beans.PersonnelBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ViewVisitedHCPsActionTest extends TestCase {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen;
	private ViewVisitedHCPsAction action;
	private ViewVisitedHCPsAction action2;

	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		gen.patient_hcp_vists();
		action = new ViewVisitedHCPsAction(factory, 2L);
		action2 = new ViewVisitedHCPsAction(factory, 1L);
	}

	public void testGetVisitedHCPsRed() {
		List<HCPVisitBean> beans = action.getVisitedHCPs();

		assertEquals("Jason Frankenstein", beans.get(0).getHCPName());
		assertEquals("Kelly Doctor", beans.get(1).getHCPName());
		assertEquals("Gandalf Stormcrow", beans.get(2).getHCPName());
	}

	public void testGetVisitedHCPsRed2() {
		List<HCPVisitBean> beans = action2.getVisitedHCPs();

		assertEquals("Kelly Doctor", beans.get(0).getHCPName());
		assertEquals("Gandalf Stormcrow", beans.get(1).getHCPName());
	}

	public void testCheckDeclared() {
		assertEquals(false, action.checkDeclared(0));
		assertEquals(true, action.checkDeclared(9000000003L));
	}

	public void testFilterHCPList() {
		List<PersonnelBean> beans = action.filterHCPList("Frank", "pediatrician", "");

		for (PersonnelBean bean : beans) {
			assertEquals("Lauren Frankenstein", bean.getFullName());
		}
	}
	
	public void testDeclareAndUndeclareHCP() throws Exception {
		action.declareHCP("Kelly Doctor");
		assertEquals(true, action.checkDeclared(9000000000L));
		action.undeclareHCP("Kelly Doctor");
		assertEquals(false, action.checkDeclared(9000000000L));
	}
}
