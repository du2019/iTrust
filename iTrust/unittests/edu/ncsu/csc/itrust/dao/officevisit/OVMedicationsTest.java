package edu.ncsu.csc.itrust.dao.officevisit;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.MedicationBean;
import edu.ncsu.csc.itrust.beans.PrescriptionBean;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class OVMedicationsTest extends TestCase {
	private OfficeVisitDAO ovDAO = TestDAOFactory.getTestInstance().getOfficeVisitDAO();
	private PrescriptionBean pres;

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.ndCodes();
		gen.officeVisit1();
		pres = new PrescriptionBean();
		MedicationBean medication = new MedicationBean();
		medication.setDescription("Tetracycline");
		medication.setNDCode("009042407");
		pres.setMedication(medication );
		pres.setDosage(50);
		pres.setStartDateStr("05/19/2007");
		pres.setEndDateStr("05/19/2010");
		pres.setVisitID(1L);
		pres.setInstructions("Take twice daily");

	}

	public void testAddRemoveOneOVPrescription() throws Exception {
		assertEquals("no current prescriptions on office vist 1", 0, ovDAO.getPrescriptions(1).size());
		long ovMedID = ovDAO.addPrescription(pres);
		List<PrescriptionBean> meds = ovDAO.getPrescriptions(1);
		assertEquals("now there's 1", 1, meds.size());
		assertEquals(pres, meds.get(0));
		ovDAO.removePrescription(ovMedID);
		assertEquals("now there's none", 0, ovDAO.getPrescriptions(1).size());
	}

	public void testAddMultipleRemoveSingle() throws Exception {
		assertEquals("no current prescriptions on office vist 1", 0, ovDAO.getPrescriptions(1).size());
		long ovMedID = ovDAO.addPrescription(pres);
		ovDAO.addPrescription(pres);
		List<PrescriptionBean> meds = ovDAO.getPrescriptions(1);
		assertEquals("now there's 2", 2, meds.size());
		assertEquals(pres, meds.get(0));
		ovDAO.removePrescription(ovMedID);
		assertEquals("now there's one", 1, ovDAO.getPrescriptions(1).size());
	}

	public void testRemoveNonExistant() throws Exception {
		assertEquals("no current prescriptions on office vist 1", 0, ovDAO.getPrescriptions(1).size());
		ovDAO.removePrescription(50L);
		assertEquals("no current prescriptions on office vist 1", 0, ovDAO.getPrescriptions(1).size());
	}

}
