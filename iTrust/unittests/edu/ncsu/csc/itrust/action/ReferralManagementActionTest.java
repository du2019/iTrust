package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.ReferralBean;
import edu.ncsu.csc.itrust.beans.ReferralBean.ReferralStatus;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.testutils.EvilDAOFactory;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ReferralManagementActionTest extends TestCase {
	private ReferralManagementAction action;
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private long hcp0 = 9000000000L;
	private long hcp3 = 9000000003L;
	private long patient1 = 1L;


	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		gen.hcp3();
		gen.patient1();
	}

	public void testSend() throws Exception {
		action = new ReferralManagementAction(factory, hcp0);
		ReferralBean r = new ReferralBean();
		r.setId(1);
		r.setSenderID(hcp0);
		r.setReceiverID(hcp3);
		r.setPatientID(patient1);
		r.setReferralDetails("This is a referral");
		
		action.sendReferral(r);
		
		List<ReferralBean> list = action.getReferralsSentFromMe();
		
		assertEquals(1, list.size());
		r.setId(list.get(0).getId());//don't care about the ID - it's auto-increment
		assertEquals(r, list.get(0));
		
	}

	public void testSendAndReceive() throws Exception {
		action = new ReferralManagementAction(factory, hcp0);
		ReferralBean r = new ReferralBean();
		r.setSenderID(hcp0);
		r.setReceiverID(hcp3);
		r.setPatientID(patient1);
		r.setReferralDetails("This is a referral");
		
		action.sendReferral(r);
		
		action = new ReferralManagementAction(factory, hcp3);
		
		List<ReferralBean> list = action.getReferralsSentToMe();
		
		assertEquals(1, list.size());
		r.setId(list.get(0).getId());//don't care about the ID - it's auto-increment
		assertEquals(r, list.get(0));
		
	}
	
	public void testEdit() throws Exception {
		action = new ReferralManagementAction(factory, hcp0);
		ReferralBean r = new ReferralBean();
		r.setId(1);
		r.setSenderID(hcp0);
		r.setReceiverID(hcp3);
		r.setPatientID(patient1);
		r.setReferralDetails("This is a referral");
		
		action.sendReferral(r);
		
		List<ReferralBean> list = action.getReferralsSentFromMe();
		
		assertEquals(1, list.size());
		r.setId(list.get(0).getId());//don't care about the ID - it's auto-increment
		assertEquals(r, list.get(0));
		
		r.setConsultationDetails("These are consulation details");
		r.setStatus(ReferralStatus.Finished);
		
		action.updateReferral(r);
		
		list = action.getReferralsSentFromMe();
		
		assertEquals(1, list.size());
		assertEquals("These are consulation details", list.get(0).getConsultationDetails());
		assertEquals(ReferralStatus.Finished, list.get(0).getStatus());
		
		action = new ReferralManagementAction(EvilDAOFactory.getEvilInstance(), 0l);
		

		try {
			action.getReferralsSentFromMe();
			fail();
		} catch (DBException e) {
			
		}
		
		try {
			action.getReferralsSentToMe();
			fail();
		} catch (DBException e) {
			
		}
	}
	
}
