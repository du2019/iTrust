INSERT INTO Hospitals(HospitalID, HospitalName) VALUES
('9191919191','Test Hospital 9191919191'),
('8181818181','Test Hospital 8181818181'),
('1','Test Hospital 1'),
('','Z Empty Hospital') ON DUPLICATE KEY UPDATE HospitalID = HospitalID;