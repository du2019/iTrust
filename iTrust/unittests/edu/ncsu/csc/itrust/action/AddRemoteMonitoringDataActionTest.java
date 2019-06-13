package edu.ncsu.csc.itrust.action;

import junit.framework.TestCase;
import edu.ncsu.csc.itrust.datagenerators.TestDataGenerator;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.exception.iTrustException;
import edu.ncsu.csc.itrust.testutils.TestDAOFactory;

public class AddRemoteMonitoringDataActionTest extends TestCase {
	AddRemoteMonitoringDataAction action;
	private TestDataGenerator gen;

	@Override
	protected void setUp() throws Exception {
		gen = new TestDataGenerator();
		gen.clearAllTables();
		gen.hcp0();
		gen.patient1();
		action = new AddRemoteMonitoringDataAction(TestDAOFactory.getTestInstance(), 1, 1);
	}

	public void testAddRemoteMonitoringData() throws Exception {
		try {
			action.addRemoteMonitoringData(80, 80, 80);
		} catch(FormValidationException e) {
			fail();
		}
	}
	
	public void testAddRemoteMonitoringDataUAP() throws Exception {
		gen.uap1();
		gen.remoteMonitoringUAP();
		AddRemoteMonitoringDataAction actionUAP = new AddRemoteMonitoringDataAction(TestDAOFactory.getTestInstance(),Long.parseLong("8000000009"),2);
		try {
			actionUAP.addRemoteMonitoringData(80, 80, 80);
		} catch(FormValidationException e) {
			fail();
		}
	}
	
	
	public void testAddRemoteMonitoringDataGlucoseOnly() throws Exception {
		try {
			action.addRemoteMonitoringData(80);
		} catch(FormValidationException e) {
			fail();
		}
	}

	public void testAddRemoteMonitoringDataGlucoseOnlyUAP() throws Exception {
		gen.uap1();
		gen.remoteMonitoringUAP();
		AddRemoteMonitoringDataAction actionUAP = new AddRemoteMonitoringDataAction(TestDAOFactory.getTestInstance(),Long.parseLong("8000000009"),2);
		try {
			actionUAP.addRemoteMonitoringData(80);
		} catch(FormValidationException e) {
			fail();
		}
	}

	public void testAddRemoteMonitoringDataBloodPressureOnly() throws Exception {
		try {
			action.addRemoteMonitoringData(100,80);
		} catch(FormValidationException e) {
			fail();
		}
	}

	public void testAddRemoteMonitoringDataBloodPressureOnlyUAP() throws Exception {
		gen.uap1();
		gen.remoteMonitoringUAP();
		AddRemoteMonitoringDataAction actionUAP = new AddRemoteMonitoringDataAction(TestDAOFactory.getTestInstance(),Long.parseLong("8000000009"),2);
		try {
			actionUAP.addRemoteMonitoringData(100, 80);
		} catch(FormValidationException e) {
			fail();
		}
	}

	public void testAddBadRemoteMonitoringData() throws Exception {
		try {
			action.addRemoteMonitoringData(39, 100, 0);
			fail();
		} catch(FormValidationException e) {
			
		}
		try {
			action.addRemoteMonitoringData(240, 151, 100);
			fail();
		} catch(FormValidationException e) {
			
		}
		try {
			action.addRemoteMonitoringData(40, 150, 1000);
			fail();
		} catch(FormValidationException e) {
			
		}
		try {
			action.addRemoteMonitoringData(-5, 20, 0);
			fail();
		} catch(FormValidationException e) {
			
		}
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
			action.addRemoteMonitoringData(100, 80, 100);
		
		try {
			action.addRemoteMonitoringData(100, 80, 100);
			fail(); //Should throw an exception - 11 entries.
		} catch(iTrustException e) {
			assertEquals("Patient entries for today cannot exceed 10.",e.getMessage());
		}
	}


}
