package edu.ncsu.csc.itrust.validate.bean;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.LabProcedureBean;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.validate.LabProcedureValidator;
import edu.ncsu.csc.itrust.validate.ValidationFormat;

public class LabProcedureValidatorTest extends TestCase {
	public void testLabProcedureAllCorrect() throws Exception {
		LabProcedureBean l = new LabProcedureBean();
		l.setCommentary("This is it");;
		l.setLoinc("00000-0");
		l.setPid(1L);
		l.statusPending();
		l.allow();
		new LabProcedureValidator().validate(l);
	}

	public void testLabProcedureAllErrors() throws Exception {
		LabProcedureBean l = new LabProcedureBean();
		l.setCommentary("This is it");;
		l.setLoinc("0000-00");
		l.setPid(1L);
		l.setResults("\'");
		l.setCommentary("#");
		l.setStatus("not allowed");
		l.setRights("DENIED");
		
		try {
			new LabProcedureValidator().validate(l);
			fail("exception should have been thrown");
		} catch (FormValidationException e) {
			assertEquals("LOINC: " + ValidationFormat.LOINC.getDescription(), e.getErrorList().get(0));
			assertEquals("Commentary: " + ValidationFormat.COMMENTS.getDescription(), e.getErrorList().get(1));
			assertEquals("Results: " + ValidationFormat.COMMENTS.getDescription(), e.getErrorList().get(2));
			assertEquals("Status: " + ValidationFormat.LAB_STATUS.getDescription(), e.getErrorList().get(3));
			assertEquals("Rights: " + ValidationFormat.LAB_RIGHTS.getDescription(), e.getErrorList().get(4));
			assertEquals("number of errors", 5, e.getErrorList().size());
		}
	}
}
