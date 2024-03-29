package edu.ncsu.csc.itrust.dao.officevisit;

import java.text.SimpleDateFormat;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.OfficeVisitBean;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class UpdateOfficeVisitTest extends TestCase{
	private OfficeVisitDAO ovDAO = TestDAOFactory.getTestInstance().getOfficeVisitDAO();

	public void testUpdateNewOfficeVisit() {
		try {
			OfficeVisitBean ov = new OfficeVisitBean();
			long newOVID = ovDAO.add(ov);
			ov = new OfficeVisitBean(newOVID);
			ov.setNotes("some notes");
			ov.setVisitDateStr("07/07/2007");
			ov.setPatientID(65);
			ov.setHcpID(5);
			ov.setHospitalID("9191919191");
			ovDAO.update(ov);
			ov = ovDAO.getOfficeVisit(newOVID);
			assertEquals("some notes",ov.getNotes());
			assertEquals("07/07/2007", new SimpleDateFormat("MM/dd/yyyy").format(ov.getVisitDate()));
			assertEquals(65, ov.getPatientID());
			assertEquals(5, ov.getHcpID());
			assertEquals("9191919191",ov.getHospitalID());
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
