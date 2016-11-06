DROP ALL OBJECTS;
CREATE TABLE   WORKERS (id   INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
                    short_name VARCHAR(100) NOT NULL,
                    name VARCHAR(100) NOT NULL,
                    password  VARCHAR(20) NOT NULL,
                    photo_url VARCHAR(100),
                    UNIQUE (short_name));

//Workers Table
INSERT INTO Users (short_name,name,password,photo_url) VALUES ('vasya','Vasily Pupkin', '1','/avatars/vasya.jpg');
INSERT INTO Users (short_name,name,password,photo_url) VALUES ('petya','Petr Pupkin', '1','/avatars/petya.jpg');
INSERT INTO Users (short_name,name,password,photo_url) VALUES ('slava','Slava Pupkin', '1','/avatars/petya.jpg');
INSERT INTO Users (short_name,name,password,photo_url) VALUES ('kostya','Kostya Pupkin', '1','/avatars/petya.jpg');
INSERT INTO Users (short_name,name,password,photo_url) VALUES ('Oleg','Oleg Pupkin', '1','/avatars/petya.jpg');

//Clients
CREATE TABLE   WORKERS (id   INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        short_name VARCHAR(100) NOT NULL,
                        name VARCHAR(100) NOT NULL,
                        password  VARCHAR(20) NOT NULL,
                        photo_url VARCHAR(100),
UNIQUE (short_name));



CREATE TABLE   DEVICES (id   INT     NOT NULL AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(100) NOT NULL,
                    serial  VARCHAR(20) NOT NULL,
                    mount_place VARCHAR(30),
                    last_verification DATE,
                    next_verification DATE,
                    passport_url VARCHAR (100),
                    UNIQUE (serial,name));

//Devices Table
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('Молоток', '1','PLACE_1','2012-01-01','2016-12-20');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_1', '2','PLACE_1','2013-01-01','2021-10-30');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_2', '3','PLACE_2','2017-01-01','2020-05-20');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '4','PLACE_2','2014-01-01','2016-01-01');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '5','PLACE_1','2012-01-01','2019-04-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '6','PLACE_1','2014-01-01','2018-03-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '7','PLACE_5','2011-01-01','2016-08-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '8','PLACE_3','2015-01-01','2018-03-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '9','PLACE_2','2009-01-01','2016-09-15');

INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_2', '10','PLACE_2','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_1', '11','PLACE_1','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_4', '12','PLACE_3','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_6', '13','PLACE_7','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_8', '14','PLACE_3','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_9', '15','PLACE_9','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_5', '16','PLACE_9','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_2', '17','PLACE_3','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_8', '18','PLACE_1','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '19','PLACE_5','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_7', '20','PLACE_4','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_8', '21','PLACE_2','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_0', '22','PLACE_9','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '23','PLACE_0','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_1', '24','PLACE_3','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_5', '25','PLACE_6','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_7', '26','PLACE_4','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_8', '27','PLACE_1','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_9', '28','PLACE_1','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_9', '29','PLACE_9','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_3', '30','PLACE_5','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_1', '31','PLACE_6','2009-01-01','2016-09-15');
INSERT INTO DEVICES (name, serial,mount_place,last_verification,next_verification) VALUES ('DEVICE_TYPE_9', '32','PLACE_7','2009-01-01','2016-09-15');
