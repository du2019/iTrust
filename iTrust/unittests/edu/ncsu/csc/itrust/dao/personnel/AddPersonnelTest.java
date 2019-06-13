package edu.ncsu.csc.itrust.dao.personnel;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.dao.mysql.PersonnelDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.enums.Role;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class AddPersonnelTest extends TestCase {
	PersonnelDAO personnelDAO = TestDAOFactory.getTestInstance().getPersonnelDAO();

	@Override
	protected void setUp() throws Exception {
		TestDataGenerator gen = new TestDataGenerator();
		gen.clearAllTables();
	}

	public void testAddEmptyPersonnel() throws Exception {
		long mid = personnelDAO.addEmptyPersonnel(Role.HCP);
		assertEquals(" ", personnelDAO.getName(mid));
		assertEquals(Role.HCP, personnelDAO.getPersonnel(mid).getRole());
		assertTrue("hcp MID is greater or equal to 9 billion, actual:" + mid, mid >= 9000000000L);
	}

	public void testAddEmptyER() throws Exception {
		long mid = personnelDAO.addEmptyPersonnel(Role.ER);
		assertEquals(" ", personnelDAO.getName(mid));
		assertEquals("er", personnelDAO.getPersonnel(mid).getRole().getUserRolesString());
	}

	public void testDoesNotExist() throws Exception {
		try {
			personnelDAO.getName(0L);
			fail("exception should have been thrown");
		} catch (iTrustException e) {
			assertEquals("User does not exist", e.getMessage());
		}
	}
}
