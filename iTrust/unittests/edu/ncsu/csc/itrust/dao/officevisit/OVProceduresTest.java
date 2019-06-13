package edu.ncsu.csc.itrust.dao.officevisit;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.ProcedureBean;
import edu.ncsu.csc.itrust.dao.mysql.OfficeVisitDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class OVProceduresTest extends TestCase{
	private OfficeVisitDAO ovDAO = TestDAOFactory.getTestInstance().getOfficeVisitDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.cptCodes();
		gen.officeVisit1();
	}
	public void testAddRemoveOneOVProcedure() throws Exception {
		assertEquals("no current procedures on office vist 1", 0, ovDAO.getProcedures(1).size());
		long ovPID = ovDAO.addProcedureToOfficeVisit("1270F", 1, "");
		List<ProcedureBean> procs = ovDAO.getProcedures(1);
		assertEquals("now there's 1", 1, procs.size());
		assertEquals("test the description", "Injection procedure",procs.get(0).getDescription());
		ovDAO.removeProcedureFromOfficeVisit(ovPID);
		assertEquals("now there's none", 0, ovDAO.getProcedures(1).size());
	}
}
