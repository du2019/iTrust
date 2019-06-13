package edu.ncsu.csc.itrust.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.ReferralBean;
import edu.ncsu.csc.itrust.beans.ReferralBean.ReferralStatus;

public class ReferralBeanTest extends TestCase {



	public void testBean() {
		ReferralBean r = new ReferralBean();
		r.setId(1L);
		r.setSenderID(2L);
		r.setReceiverID(3L);
		r.setPatientID(4L);
		r.setReferralDetails("Five");
		r.setConsultationDetails("Six");
		r.setStatus(ReferralStatus.Finished);
		
		assertEquals(1, r.getId());
		assertEquals(2, r.getSenderID());
		assertEquals(3, r.getReceiverID());
		assertEquals(4, r.getPatientID());
		assertEquals("Five", r.getReferralDetails());
		assertEquals("Six", r.getConsultationDetails());
		assertEquals(ReferralStatus.Finished, r.getStatus());
	}
	
	
}
