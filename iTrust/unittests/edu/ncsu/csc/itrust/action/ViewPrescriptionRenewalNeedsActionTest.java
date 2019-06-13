package edu.ncsu.csc.itrust.action;

import static edu.ncsu.csc.itrust.testutils.JUnitiTrustUtils.assertTransactionOnly;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.PatientBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.TransactionType;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import java.util.List;


public class ViewPrescriptionRenewalNeedsActionTest extends TestCase {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private ViewPrescriptionRenewalNeedsAction action;
	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hospitals();
		gen.hcp2();
		gen.hcp1();
		gen.hcp3();
		gen.hcp0();
		gen.ndCodes();
		gen.icd9cmCodes();
		gen.patient9();
		gen.patient11();
		gen.patient14();
		
		
		gen.UC32Acceptance();
	}

	public void testTwoPatients() throws Exception {
		action = new ViewPrescriptionRenewalNeedsAction(factory, 9000000003L);
		List<PatientBean> patients = action.getRenewalNeedsPatients();
		assertEquals("Andy", patients.get(0).getFirstName());
		assertEquals("Koopa", patients.get(0).getLastName());
		assertEquals("919-212-3433", patients.get(1).getPhone());
		assertEquals("prince@gmail.com", patients.get(1).getEmail());
		
		
		assertTransactionOnly(TransactionType.VIEW_RENEWAL_NEEDS_PATIENTS, 9000000003L, 0L,
				"9000000003 viewed renewal needs patients");
	}

	public void testThreePatients() throws Exception {
		action = new ViewPrescriptionRenewalNeedsAction(factory, 9900000000L);
		List<PatientBean> patients = action.getRenewalNeedsPatients();
		assertEquals(3, patients.size());
		assertEquals("Zack", patients.get(0).getFirstName());
		assertEquals("Darryl", patients.get(1).getFirstName());
		assertEquals("Marie", patients.get(2).getFirstName());
		
		
		assertTransactionOnly(TransactionType.VIEW_RENEWAL_NEEDS_PATIENTS, 9900000000L, 0L,
				"9900000000 viewed renewal needs patients");
	}
	
	public void testZeroPatients() throws Exception {
		action = new ViewPrescriptionRenewalNeedsAction(factory, 9990000000L);
		List<PatientBean> patients = action.getRenewalNeedsPatients();
		assertNotNull(patients);
		assertEquals(0, patients.size());
		
		
		assertTransactionOnly(TransactionType.VIEW_RENEWAL_NEEDS_PATIENTS, 9990000000L, 0L,
				"9990000000 viewed renewal needs patients");
	}
	
	public void testDBException() throws Exception {
		action = new ViewPrescriptionRenewalNeedsAction(factory, -1L);
		List<PatientBean> patients = action.getRenewalNeedsPatients();
		assertNull(patients);
		
	}

}
