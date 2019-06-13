package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.forms.VisitReminderReturnForm;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class GetVisitRemindersActionTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private GetVisitRemindersAction action;
	private TestDataGenerator gen = new TestDataGenerator();

	@Override
	protected void setUp() throws Exception {
		gen.clearAllTables();
		gen.standardData();
		action = new GetVisitRemindersAction(factory, 9000000000L);
	}

	public void testNormalReturn() throws Exception {
		action.getVisitReminders(GetVisitRemindersAction.ReminderType.DIAGNOSED_CARE_NEEDERS);
		action.getVisitReminders(GetVisitRemindersAction.ReminderType.FLU_SHOT_NEEDERS);
	}

	public void testNoSubAction() throws Exception {
		assertEquals(3, action.getVisitReminders(GetVisitRemindersAction.ReminderType.FLU_SHOT_NEEDERS).size());
	}
	
	public void testGetReminderType() throws Exception {
		assertEquals(GetVisitRemindersAction.ReminderType.DIAGNOSED_CARE_NEEDERS, GetVisitRemindersAction.ReminderType.getReminderType("Diagnosed Care Needers"));
	}
	
	public void testBadReminderType() throws Exception {
		try {
			action.getVisitReminders(null);
			fail("testBadReminderType: bad reminder type not caught");
		} catch(iTrustException e) {
			assertEquals("Reminder Type DNE", e.getMessage());
		}
	}

	public void testGetImmunizationNeeders() throws iTrustException, FormValidationException {
		List<VisitReminderReturnForm> vrList = action.getVisitReminders(GetVisitRemindersAction.ReminderType.IMMUNIZATION_NEEDERS);
		
		assertEquals(2, vrList.size());
	}

	public void testTestHPV() {
		assertEquals("90649 Human Papillomavirus (9 years) ", GetVisitRemindersAction.testHPV(0, 468L, 0L));
		assertEquals("90649 Human Papillomavirus (9 years, 2 months) ", GetVisitRemindersAction.testHPV(1, 476L, 0L));
		assertEquals("90649 Human Papillomavirus (9 years, 6 months) ", GetVisitRemindersAction.testHPV(2, 494L, 0L));
	}
	
	public void testTestHepA() {
		assertEquals("90633 Hepatits A (12 months) ", GetVisitRemindersAction.testHepA(0, 52L, 0L));
		assertEquals("90633 Hepatits A (18 months) ", GetVisitRemindersAction.testHepA(1, 78L, 0L));
	}
	
	public void testTestVaricella() {
		assertEquals("90396 Varicella (12 months) ", GetVisitRemindersAction.testVaricella(0, 52L, 0L));
		assertEquals("90396 Varicella (4 years) ", GetVisitRemindersAction.testVaricella(1, 208L, 0L));

	}

	public void testTestMeasles() {
		assertEquals("90707 Measles, Mumps, Rubekka (12 months) ", GetVisitRemindersAction.testMeasles(0, 52L, 0L));
		assertEquals("90707 Measles, Mumps, Rubekka (4 years) ", GetVisitRemindersAction.testMeasles(1, 208L, 0L));
	}

	public void testTestPolio() {
		assertEquals("90712 Poliovirus (6 weeks) ", GetVisitRemindersAction.testPolio(0, 6L, 0L));
		assertEquals("90712 Poliovirus (4 months) ", GetVisitRemindersAction.testPolio(1, 16L, 0L));
		assertEquals("90712 Poliovirus (6 months) ", GetVisitRemindersAction.testPolio(2, 26L, 0L));
	}
	
	public void testTestPneumo() {
		assertEquals("90669 Pneumococcal (6 weeks) ", GetVisitRemindersAction.testPneumo(0, 6L, 0L, 0L));
		assertEquals("90669 Pneumococcal (4 months) ", GetVisitRemindersAction.testPneumo(1, 16L, 0L, 0L));
//		assertEquals("90669 Pneumococcal (6 months) ", GetVisitRemindersAction.testPneumo(2, 26L, 0L));
//		assertEquals("90669 Pneumococcal (12 months) ", GetVisitRemindersAction.testPneumo(2, 52L, 0L));
	}
	
	public void testTestHaemoFlu() {
		assertEquals("90645 Haemophilus influenzae (6 weeks) ", GetVisitRemindersAction.testHaemoFlu(0, 6L, 0L, 0L));
		assertEquals("90645 Haemophilus influenzae (4 months) ", GetVisitRemindersAction.testHaemoFlu(1, 16L, 0L, 0L));
		assertEquals("90645 Haemophilus influenzae (6 months) ", GetVisitRemindersAction.testHaemoFlu(2, 26L, 0L, 0L));
	}
	
	public void testTestDipTet() {
		assertEquals("90696 Diphtheria, Tetanus, Pertussis (6 weeks) ", GetVisitRemindersAction.testDipTet(0, 6L, 0L));
		assertEquals("90696 Diphtheria, Tetanus, Pertussis (4 months) ", GetVisitRemindersAction.testDipTet(1, 16L, 0L));
		assertEquals("90696 Diphtheria, Tetanus, Pertussis (6 months) ", GetVisitRemindersAction.testDipTet(2, 26L, 0L));
		assertEquals("90696 Diphtheria, Tetanus, Pertussis (15 weeks) ", GetVisitRemindersAction.testDipTet(3, 15L, 0L));
		assertEquals("90696 Diphtheria, Tetanus, Pertussis (4 years) ", GetVisitRemindersAction.testDipTet(4, 208L, 0L));
		assertEquals("90696 Diphtheria, Tetanus, Pertussis (11 years) ", GetVisitRemindersAction.testDipTet(5, 572L, 0L));
	}
	
	public void testTestRotaVirus() {
		assertEquals("90681 Rotavirus (6 weeks) ", GetVisitRemindersAction.testRotaVirus(0, 6L, 0L));
		assertEquals("90681 Rotavirus (4 months) ", GetVisitRemindersAction.testRotaVirus(1, 16L, 0L));
		assertEquals("90681 Rotavirus (6 months) ", GetVisitRemindersAction.testRotaVirus(2, 26L, 0L));
	}
	
	public void testTestHepB() {
		assertEquals("90371 Hepatitis B (birth) ", GetVisitRemindersAction.testHepB(0, 1L, 0L));
		assertEquals("90371 Hepatitis B (1 month) ", GetVisitRemindersAction.testHepB(1, 4L, 0L));
		assertEquals("90371 Hepatitis B (6 months) ", GetVisitRemindersAction.testHepB(2, 26L, 0L));
	}

}
