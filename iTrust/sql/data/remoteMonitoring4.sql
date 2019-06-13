DELETE FROM Representatives where representerMID = '2';
DELETE FROM RemoteMonitoringData where PatientID = '1';

INSERT INTO Representatives(representerMID, representeeMID)
					VALUES (2, 1);


