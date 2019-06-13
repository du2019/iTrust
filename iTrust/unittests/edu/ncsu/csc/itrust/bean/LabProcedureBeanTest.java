package edu.ncsu.csc.itrust.bean;

import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;

public class LabProcedureBeanTest  extends TestCase{
	LabProcedureBean l;
	@Override
	protected void setUp() throws Exception {
		l = new LabProcedureBean();
		l.setPid(0000000001);
		l.setProcedureID(10);
		l.setLoinc("12345-6");
		l.statusNotReceived();
		l.setCommentary("Their blood is purple and orange.");
		l.setResults("Please call us for your results.");
		l.setOvID(10023);
		Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("03/28/2008 12:00");
		l.setTimestamp(new java.sql.Timestamp(date.getTime()));
		l.allow();
	}
	
	public void testBaseCaseBean() throws Exception {
		assertEquals(0000000001, l.getPid());
		assertEquals(10, l.getProcedureID());
		assertEquals("12345-6", l.getLoinc());
		assertEquals(LabProcedureBean.Not_Received, l.getStatus());
		assertEquals("Their blood is purple and orange.", l.getCommentary());
		assertEquals("Please call us for your results.", l.getResults());
		assertEquals(10023, l.getOvID());
		Date date = new SimpleDateFormat("MM/dd/yyyy HH:mm").parse("03/28/2008 12:00");
		assertEquals(date.getTime(), l.getTimestamp().getTime());
		assertEquals(LabProcedureBean.Allow, l.getRights());
	}
}
