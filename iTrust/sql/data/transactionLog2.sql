DELETE FROM TransactionLog where secondaryMID = '2';

INSERT INTO TransactionLog(loggedInMID, secondaryMID, transactionCode, timeLogged, addedInfo) 
                    VALUES (9000000000, 2, 14,'2008-03-04 10:15:00','Identified risk factors of chronic diseases'),
                           (9000000000, 2, 39,'2008-09-07 16:30:00','Updated office visit'),
                           (9000000003, 2, 19,'2008-07-15 13:13:00','Viewed prescription report'),
                           (9000000006, 2, 22,'2008-11-14 09:32:00','Viewed emergency report ');


