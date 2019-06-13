package edu.ncsu.csc.itrust.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class DBUtilTest extends TestCase {
	public void testCanAccessProductionDriver() throws Exception {
		// Should never be able to do this because JUnit is not running under Tomcat
		assertFalse(DBUtil.canObtainProductionInstance());
	}
	
	public void testClosingNullPS() throws Exception {
		Connection conn = TestDAOFactory.getTestInstance().getConnection();
		PreparedStatement ps = conn.prepareStatement("SHOW TABLES");
		DBUtil.closeConnection(conn, ps);
	}
}
