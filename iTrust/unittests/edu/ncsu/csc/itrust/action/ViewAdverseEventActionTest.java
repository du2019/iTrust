package edu.ncsu.csc.itrust.action;

import java.util.List;
import edu.ncsu.csc.itrust.beans.AdverseEventBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;
import junit.framework.TestCase;

public class ViewAdverseEventActionTest extends TestCase {
	private ViewAdverseEventAction action;
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private TestDataGenerator gen;
	
	protected void setUp() throws Exception {
		action = new ViewAdverseEventAction(factory);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.standardData();	
		gen.adverseEvent1();
	}
	
	public void testGetAdverseEvent() throws Exception{
		AdverseEventBean bean = action.getAdverseEvent(1);
		assertNotNull(bean);
	}
	
	public void testGetUnremovedAdverseEventsByCode() throws Exception {
		List<AdverseEventBean> beanList = action.getUnremovedAdverseEventsByCode("548684985");
		assertNotNull(beanList);
		assertFalse(beanList.isEmpty());
		
		assertEquals(beanList.get(0).getDescription(),"Stomach cramps and migraine headaches after taking this drug");
		
	}
	
	public void testGetNameForCode() throws Exception {
		String name = action.getNameForCode("548684985");
		assertEquals(name,"Citalopram Hydrobromide");
	}
}
