package edu.ncsu.csc.itrust.dao.access;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import junit.framework.TestCase;
import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.dao.mysql.AccessDAO;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class GetSessionTimeoutTest extends TestCase {
	private AccessDAO accessDAO = TestDAOFactory.getTestInstance().getAccessDAO();
	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.timeout();
	}

	public void testChangeTimeout() throws Exception {
		assertEquals(20, accessDAO.getSessionTimeoutMins());
		accessDAO.setSessionTimeoutMins(5);
		assertEquals(5, accessDAO.getSessionTimeoutMins());
	}

	public void testUpdateBadTimeout() throws Exception {
		deleteTimeout();
		assertEquals(20, accessDAO.getSessionTimeoutMins());
		deleteTimeout();
		accessDAO.setSessionTimeoutMins(5);
		assertEquals(5, accessDAO.getSessionTimeoutMins());
	}

	private void deleteTimeout() throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		try{
			conn = TestDAOFactory.getTestInstance().getConnection();
			ps = conn.prepareStatement("DELETE FROM GlobalVariables WHERE Name='Timeout'");
			ps.executeUpdate();
		}
		catch (SQLException ex){
			throw ex;
		}
		finally{
		DBUtil.closeConnection(conn,ps);
		}
	}
}
