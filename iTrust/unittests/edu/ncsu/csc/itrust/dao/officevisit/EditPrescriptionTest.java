package edu.ncsu.csc.itrust.dao.officevisit;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.action.EditPrescriptionAction;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class EditPrescriptionTest extends TestCase {
	private EditPrescriptionAction epa;
	private PrescriptionBean pres;
	private OfficeVisitDAO ovDAO = TestDAOFactory.getTestInstance().getOfficeVisitDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		epa = new EditPrescriptionAction(TestDAOFactory.getTestInstance());
		gen.clearAllTables();
		gen.ndCodes();
		gen.hcp0();
		gen.patient2();

	}

	public void testEditInstructions() throws Exception {
		pres = ovDAO.getPrescriptions(955).get(0);
		assertEquals("Take twice daily", pres.getInstructions());
		
		pres.setInstructions("Take thrice daily");
		
		epa.editPrescription(pres);
		
		pres.setInstructions("fail");
		
		pres = ovDAO.getPrescriptions(955).get(0);
		assertEquals("Take thrice daily", pres.getInstructions());
		
	}
	
	public void testEditDosage() throws Exception {
		pres = ovDAO.getPrescriptions(955).get(0);
		assertEquals(5, pres.getDosage());
		
		pres.setDosage(10);
		
		epa.editPrescription(pres);
		
		pres.setDosage(1);
		
		pres = ovDAO.getPrescriptions(955).get(0);
		assertEquals(10, pres.getDosage());
		
	}
	
	public void testEditVisitID() throws Exception {
		pres = ovDAO.getPrescriptions(955).get(0);
		assertEquals(5, pres.getDosage());
		
		pres.setDosage(6001);
		pres.setVisitID(11);
		
		epa.editPrescription(pres);
		
		
		pres = ovDAO.getPrescriptions(11).get(0);
		assertEquals(6001, pres.getDosage());
		
	}


}
