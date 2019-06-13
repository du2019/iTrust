package edu.ncsu.csc.itrust.action;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.SurveyResultBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class ViewSurveyResultsActionTest extends TestCase {

	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private ViewSurveyResultAction action = new ViewSurveyResultAction(factory, 2L);
	private TestDataGenerator gen;
	
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();
		gen.surveyResults();
	}
	
	public void testGetResultsByZipAndSpecialty() throws Exception {
		SurveyResultBean bean = new SurveyResultBean();
		bean.setHCPzip("12388");
		bean.setHCPspecialty(SurveyResultBean.SURGEON_SPECIALTY);
		List<SurveyResultBean> list = action.getSurveyResultsForZip(bean);
		SurveyResultBean bean0 = list.get(0);
		assertEquals("12345-1234", bean0.getHCPzip());
		assertEquals("Doctor", bean0.getHCPLastName());
		assertEquals("surgeon", bean0.getHCPspecialty()); //hardcoded surgeon b/c of the capitalization difference
			
	}
	
	public void testGetResultsByHopsitalID() throws Exception {
		SurveyResultBean bean = new SurveyResultBean();
		bean.setHCPhospital("9191919191");
		bean.setHCPspecialty(SurveyResultBean.ANY_SPECIALTY);
		List<SurveyResultBean> list = action.getSurveyResultsForHospital(bean);
		SurveyResultBean bean0 = list.get(0);
		assertEquals("12345-1234", bean0.getHCPzip());
		assertEquals("Doctor", bean0.getHCPLastName());
		assertEquals("surgeon", bean0.getHCPspecialty()); //hardcoded surgeon b/c of the capitalization difference
	}
	
	
	
}
