

INSERT INTO officevisits(ID, visitDate, HCPID, notes, PatientID, HospitalID)
VALUES
(107, '2007-03-09', 9000000004, 'Diagnose Echovirus', 2, '1'),
(108, '2007-03-10', 9000000004, 'Diagnose Echovirus', 3, '1'),
(109, '2008-01-21', 9000000005, 'Diagnose Echovirus', 4, '1'),
(110, '2001-01-01', 9000000000, 'Diagnose Echovirus', 2, '1'),
(112, '2008-02-21', 9000000004, 'Diagnose Echovirus', 2, '1'),
(113, '2008-05-05', 9000000000, 'Diagnose Echovirus', 1, '1')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO ovdiagnosis(ID, VisitID, ICDCode)
VALUES
(107, 107, 79.10),
(108, 108, 79.10),
(109, 109, 79.10),
(110, 110, 79.10),
(112, 112, 79.10),
(113, 113, 79.10)
ON DUPLICATE KEY UPDATE VisitID = VALUES(VisitID), ICDCode = VALUES(ICDCode);

INSERT INTO labprocedure (PatientMID, LaboratoryProcedureCode, Rights, Status, Commentary, Results, OfficeVisitID, UpdatedDate)
VALUES
(4, '10640-1','ALLOWED','COMPLETED','','',109,'2008-01-22 20:20:45.0');

INSERT INTO ovmedication(ID, VisitID, NDCode, StartDate, EndDate, Dosage, Instructions)
VALUES
(4, 107, '009042407', '2007-03-10', '2007-04-10', 5, 'Take twice daily'),
(5, 108, '009042407', '2007-03-11', '2007-04-11', 5, 'Take twice daily')
ON DUPLICATE KEY UPDATE id = id;

INSERT INTO ovsurvey(VisitID, SurveyDate, WaitingRoomMinutes, ExamRoomMinutes, VisitSatisfaction, TreatmentSatisfaction)
VALUES
(107, '2008-09-25 02:00:00.0', 15, 10, 2, 4),
(108, '2008-09-25 03:00:00.0', 17, 25, 4, 4)
ON DUPLICATE KEY UPDATE VisitID = VisitID;
