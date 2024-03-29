package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.beans.HCPDiagnosisBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class MyDiagnosisActionTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen = new TestDataGenerator();
	private MyDiagnosisAction action;

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		gen.patient_hcp_vists();
		gen.hcp_diagnosis_data();
		
	}

	public void testGetDiagnoses() throws Exception {
		action = new MyDiagnosisAction(factory, 2L);
		List<DiagnosisBean> diagnoses = action.getDiagnoses();
		assertEquals(9, diagnoses.size());
		// further testing should be done in the patientDAO
	}

	public void testGetHCPByDiagnosis() throws Exception {
		action = new MyDiagnosisAction(factory, 1L);
		List<HCPDiagnosisBean> diags = action.getHCPByDiagnosis("79.1");
		assertEquals(9000000004L, diags.get(0).getHCP());
		assertTrue(diags.get(0).getHCPName().equals("Jason Frankenstein"));
		assertEquals(2, diags.get(0).getNumPatients());
		assertEquals(1, diags.get(0).getMedList().size());
		assertEquals(0, diags.get(0).getLabList().size());
		assertEquals("3.0", diags.get(0).getVisitSatisfaction());
		assertEquals("4.0", diags.get(0).getTreatmentSatisfaction());
	}

}
