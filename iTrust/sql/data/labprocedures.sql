INSERT INTO LabProcedure
(PatientMID,LaboratoryProcedureCode,Rights,Status,Commentary,Results,OfficeVisitID, UpdatedDate)
VALUES
(1,'10763-1','ALLOWED','NOT YET RECEIVED','','',11,'2008-05-19'),
(1,'10666-6','RESTRICTED','PENDING','Performed the procedure','',11,'2008-05-18'),
(1,'10640-1','ALLOWED','COMPLETED','Performed the procedure','Patient checks out ok',11,'2007-05-17'),
(2,'10640-1','ALLOWED', 'COMPLETED','Performed the procedure','Patient needs more tests',952,'2007-05-19'),
(2,'10763-1','RESTRICTED', 'NOT YET RECEIVED','Performed the procedure','Patient needs more tests',955, '2007-07-20'),
(4,'10763-1','ALLOWED','COMPLETED','','',955,'2007-07-20');

UPDATE LabProcedure SET UpdatedDate=(SYSDATE() - INTERVAL 1 DAY) WHERE PatientMID=4;

INSERT INTO OfficeVisits(
	id,
	visitDate,
	HCPID,
	notes,
	HospitalID,
	PatientID
)
VALUES (902,'2005-10-10',9000000000,'Generated for Death for Patient: 1','1',1),
(905,'2005-10-10',9000000000,'Generated for Death for Patient: 1','1',1),
(918,'2005-10-10',9000000000,'Generated for Death for Patient: 1','1',1)
ON DUPLICATE KEY UPDATE ID = ID;

