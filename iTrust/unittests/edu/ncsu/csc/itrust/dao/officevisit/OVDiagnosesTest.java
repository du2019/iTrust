package edu.ncsu.csc.itrust.dao.officevisit;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.DiagnosisBean;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class OVDiagnosesTest extends TestCase {
	private OfficeVisitDAO ovDAO = TestDAOFactory.getTestInstance().getOfficeVisitDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.icd9cmCodes();
		gen.officeVisit1();
	}

	public void testAddRemoveOneOVDiagnosis() throws Exception {
		assertEquals("no current diagnoses on office vist 1", 0, ovDAO.getDiagnoses(1).size());
		long ovDID = ovDAO.addDiagnosisToOfficeVisit(250.1, 1);
		List<DiagnosisBean> diagnoses = ovDAO.getDiagnoses(1);
		assertEquals("now there's 1", 1, diagnoses.size());
		assertEquals("test the description", "Diabetes with ketoacidosis", diagnoses.get(0).getDescription());
		ovDAO.removeDiagnosisFromOfficeVisit(ovDID);
		assertEquals("now there's none", 0, ovDAO.getDiagnoses(1).size());
	}
}
