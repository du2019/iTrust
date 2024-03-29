package edu.ncsu.csc.itrust.dao.allergies;

import java.util.List;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.beans.AllergyBean;
import edu.ncsu.csc.itrust.dao.DAOFactory;
import edu.ncsu.csc.itrust.dao.mysql.AllergyDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class AddAllergiesTest extends TestCase {
	private DAOFactory factory = TestDAOFactory.getTestInstance();
	private AllergyDAO allergyDAO = factory.getAllergyDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.patient2();
	}

	public void testGetAllergiesFor2() throws Exception {
		assertEquals(2, allergyDAO.getAllergies(2L).size());
		allergyDAO.addAllergy(2, "Aspirin");
		List<AllergyBean> allergies = allergyDAO.getAllergies(2L);
		assertEquals(3, allergies.size());
		assertEquals("Aspirin",allergies.get(0).getDescription());
	}
}
