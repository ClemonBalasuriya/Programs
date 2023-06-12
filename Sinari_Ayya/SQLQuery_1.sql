CREATE DATABASE Uniquely_Auto_Parts;

CREATE SCHEMA Parts;


--create a table for vehicle type
CREATE TABLE Parts.Vehicle_Type(
    Vehicle_Code CHAR(25) NOT NULL PRIMARY KEY ,
    Brand CHAR(25) NOT NULL,
    Model CHAR(25) NOT NULL,
    Model_Year INT NOT NULL,
    Vehicle_Type CHAR(25) NOT NULL,
    Gasoline_Type CHAR(25) NOT NULL
);

--Insert data to Vehicle type table
INSERT INTO Parts.Vehicle_Type VALUES 
    ('Pr2013','Toyota','Premio',2013,'Sedan','Petrol');

--create body parts table
CREATE TABLE parts.Body_Parts(
    Item_Code CHAR(50) NOT NULL PRIMARY KEY,
    Vehicle_Code CHAR(25) NOT NULL,
    Item VARCHAR (255) NOT NULL,
    Unit_Price DECIMAL(10,2) NOT NULL,
    Qty INT NOT NULL
);


--Insert data to body parts table
INSERT INTO Parts.Body_Parts VALUES 
    ('Hl2013','Pr2013','Head lamp',53000,10);



--create Engine parts table
CREATE TABLE parts.Engine_Parts(
    Item_Code CHAR(50) NOT NULL PRIMARY KEY,
    Vehicle_Code CHAR(25) NOT NULL,
    Item VARCHAR (255) NOT NULL,
    Unit_Price DECIMAL(10,2) NOT NULL,
    Qty INT NOT NULL
);


--Insert data to Engine parts table
INSERT INTO Parts.Engine_Parts VALUES 
    ('Al2013','Pr2013','Alternators',40000,15);



--create Suspention parts table
CREATE TABLE parts.Suspention_Parts(
    Item_Code CHAR(50) NOT NULL PRIMARY KEY,
    Vehicle_Code CHAR(25) NOT NULL,
    Item VARCHAR (255) NOT NULL,
    Unit_Price DECIMAL(10,2) NOT NULL,
    Qty INT NOT NULL
);



--Insert data to Suspention parts table
INSERT INTO Parts.Suspention_Parts VALUES 
    ('Sm2013','Pr2013','Shock mount',15000,15);


--create Electrical parts table
CREATE TABLE parts.Electrical_Parts(
    Item_Code CHAR(50) NOT NULL PRIMARY KEY,
    Vehicle_Code CHAR(25) NOT NULL,
    Item VARCHAR (255) NOT NULL,
    Unit_Price DECIMAL(10,2) NOT NULL,
    Qty INT NOT NULL
);



--Insert data to Electrical parts table
INSERT INTO Parts.Electrical_Parts VALUES 
    ('TCU2013','Pr2013','TCU',15000,10);















