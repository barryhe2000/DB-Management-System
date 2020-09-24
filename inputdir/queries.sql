SELECT * FROM Sailors WHERE Sailors.B < Sailors.C AND Sailors.C > 200;
SELECT * FROM Sailors WHERE Sailors.B > Sailors.C;
SELECT * FROM Sailors;
SELECT Sailors.A, Sailors.C FROM Sailors;
SELECT Sailors.A, Sailors.C FROM Sailors WHERE Sailors.B > Sailors.C;
SELECT * FROM Sailors WHERE Sailors.A > 5 AND Sailors.B < 1000;
SELECT * FROM Sailors, Reserves WHERE Sailors.A = Reserves.G;