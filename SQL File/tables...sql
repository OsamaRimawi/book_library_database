CREATE DATABASE Library;

SET SQL_SAFE_UPDATES = 0;

USE Library;

CREATE TABLE users(

	user_id VARCHAR(8),
    user_firstName VARCHAR(10),
    user_lastName VARCHAR(10),
    user_gender CHAR(1),
    user_birthDate VARCHAR(10),
    user_finantialPalance DOUBLE,
    
    PRIMARY KEY (user_id)
);

INSERT INTO users
VALUES("00000000","Ahmad", "Mahmoud", 'M', "12-12-2000", 230);
INSERT INTO users
VALUES("00000001","Khaled", "Mahmoud", 'M', "13-1-1990", 200);
INSERT INTO users 
VALUES("01000000","Ali", "Omar", 'M', "11-2-1995", 300);
INSERT INTO users
VALUES("02000000","Bahaa", "Mahmoud", 'M', "28-6-2001", 500);
INSERT INTO users
VALUES("02000001","Omar", "Ahmad", 'M', "30-10-2000", 400);
INSERT INTO users
VALUES("09000000","Bahaa", "Assad", 'M', "28-6-2001", 1000);


CREATE TABLE phone(

    phone_number VARCHAR(10),
    user_id VARCHAR(8),
    
    PRIMARY KEY(phone_number),
    
    FOREIGN KEY (user_id) REFERENCES users (user_id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

INSERT INTO phone VALUES ("0593211040", "00000000");
INSERT INTO phone VALUES ("0595211040", "00000000");
INSERT INTO phone VALUES ("0594211145", "00000001");
INSERT INTO phone VALUES ("0597216046", "01000000");
INSERT INTO phone VALUES ("0599849141", "01000000");
INSERT INTO phone VALUES ("0594311041", "02000000");
INSERT INTO phone VALUES ("0595511004", "02000000");
INSERT INTO phone VALUES ("0598311747", "02000001");
INSERT INTO phone VALUES ("0598311749", "09000000");

CREATE TABLE login(


     user_id VARCHAR(8),
     user_password VARCHAR(30),
    
     PRIMARY KEY (user_id),
    
     FOREIGN KEY (user_id) 
     REFERENCES users(user_id) 
     ON DELETE CASCADE
);

INSERT INTO login VALUES ("00000000","1234");
INSERT INTO login VALUES ("00000001","2341");
INSERT INTO login VALUES ("01000000","3412");
INSERT INTO login VALUES ("02000000","4123");
INSERT INTO login VALUES ("02000001","4321");
INSERT INTO login VALUES ("09000000","24@123");

SELECT * FROM login;

CREATE TABLE rules(

     rule_name VARCHAR(40),
     rule_description VARCHAR(400),
     
     INDEX(rule_name),
     INDEX(rule_description),
     
     PRIMARY KEY (rule_name, rule_description)
);

INSERT INTO rules VALUES ("admin", "The adminestrator for all Library"); 
---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
-- member table

CREATE TABLE member(

     user_id VARCHAR(8),
     fees DOUBLE,
     
     PRIMARY KEY (user_id),
     
     FOREIGN KEY (user_id) 
     REFERENCES users(user_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE
);

INSERT INTO member VALUES ("00000000",20);
INSERT INTO member VALUES ("00000001",20);
---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
-- admins table

CREATE TABLE admins(
     user_id VARCHAR(8),
     employment_date VARCHAR(10),
     salary DOUBLE,
     rule_name VARCHAR(40) DEFAULT "admin",
	 rule_description VARCHAR(400) DEFAULT "The adminestrator for all Library",
     
	 PRIMARY KEY (user_id),
     
     FOREIGN KEY (user_id) 
     REFERENCES users(user_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
     
     FOREIGN KEY (rule_name)
     REFERENCES rules(rule_name)
     ON DELETE NO ACTION
     ON UPDATE CASCADE,
     
     FOREIGN KEY (rule_description)
     REFERENCES rules(rule_description)
     ON DELETE NO ACTION
     ON UPDATE CASCADE
);

INSERT INTO admins(user_id, employment_date, salary) VALUES ("09000000", "11-1-2010", 12000 );
---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
-- employee table

CREATE TABLE employee(
     user_id VARCHAR(8),
     employment_date VARCHAR(10),
     salary DOUBLE,
     
	 PRIMARY KEY (user_id),
     
     FOREIGN KEY (user_id) 
     REFERENCES users(user_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE
);

INSERT INTO employee VALUES ("01000000", "12-12-2019", 2160);
INSERT INTO employee VALUES ("02000000", "10-9-2018", 2100);
INSERT INTO employee VALUES ("02000001", "1-2-2020", 1950);
---------------------------------------------------------------------------------------------------------------
---------------------------------------------------------------------------------------------------------------
-- hourly employee table

CREATE TABLE hourly_employee(
      user_id VARCHAR(8),
      workingHours DOUBLE,
      salaryPerHour DOUBLE,
      
      PRIMARY KEY (user_id),
      
      FOREIGN KEY (user_id)
      REFERENCES employee(user_id)
      ON DELETE CASCADE
);

INSERT INTO hourly_employee VALUES("01000000", 180, 12 );
---------------------------------------------------------------------------------------------------------------
-- houerly employee salary table

CREATE TABLE salary(
     workingHours DOUBLE,
	 salaryPerHour DOUBLE, 
     salary DOUBLE,
     
     PRIMARY KEY(workingHours,salaryPerHour)
);

INSERT INTO salary VALUES( 180, 12 , workingHours * salaryPerHour );

ALTER TABLE hourly_employee ADD FOREIGN KEY(workingHours) 
REFERENCES salary (workingHours)
ON DELETE NO ACTION
ON UPDATE CASCADE;

ALTER TABLE salary ADD INDEX(salaryPerHour);

ALTER TABLE hourly_employee 
ADD FOREIGN KEY(salaryPerHour) 
REFERENCES salary (salaryPerHour)
ON DELETE NO ACTION
ON UPDATE CASCADE;
--------------------------------------------------------------------------------------------------------------
-- deduction table
CREATE TABLE deduction(
      vaction_days INT,
      allowd_vaction_days INT,
      deduction DOUBLE,
      
      INDEX(vaction_days),
      INDEX(allowd_vaction_days),
      
      PRIMARY KEY( vaction_days, allowd_vaction_days)  
);

INSERT INTO deduction VALUES(0,4, IF(vaction_days > allowd_vaction_days, (vaction_days - allowd_vaction_days)/26  ,  0));

------------------------------------------------------------------------------------------------------------------
-- monthly employee table

CREATE TABLE monthly_employee(
     user_id VARCHAR(8),
     vaction_days INT,
     allowd_vaction_days INT,
     
     PRIMARY KEY (user_id),
     
     FOREIGN KEY (user_id)
     REFERENCES employee(user_id)
     ON DELETE CASCADE,
     
     FOREIGN KEY (vaction_days)
     REFERENCES deduction(vaction_days)
     ON DELETE CASCADE,
     
     FOREIGN KEY (allowd_vaction_days)
     REFERENCES deduction(allowd_vaction_days)
     ON DELETE CASCADE
);

INSERT INTO monthly_employee VALUES("02000000",0,4);
INSERT INTO monthly_employee VALUES("02000001",0,4);

-----------------------------------------------------------------------------------------------------------------
-- employee_to_rule table
CREATE TABLE employee_to_rules(
      employee_id VARCHAR(8),
      rule_name VARCHAR(40),
	  rule_description VARCHAR(400),
     
      PRIMARY KEY (employee_id, rule_name, rule_description),
      
      FOREIGN KEY (employee_id)
      REFERENCES employee(user_id)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
      
      FOREIGN KEY (rule_name)
      REFERENCES rules(rule_name)
      ON DELETE CASCADE
      ON UPDATE CASCADE,
      
      FOREIGN KEY (rule_description)
      REFERENCES rules(rule_description)
      ON DELETE CASCADE
      ON UPDATE CASCADE
);
-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
-- publisher table

CREATE TABLE publisher(
      publisher_id INT AUTO_INCREMENT,
      publisher_name VARCHAR(20),
      
      INDEX(publisher_id),
      INDEX(publisher_name),
      
      PRIMARY KEY (publisher_id)
);
ALTER TABLE publisher AUTO_INCREMENT = 1;

INSERT INTO publisher(publisher_name) VALUES ("publisher1");
INSERT INTO publisher(publisher_name) VALUES ("publisher2");
INSERT INTO publisher(publisher_name) VALUES ("publisher3");
INSERT INTO publisher(publisher_name) VALUES ("publisher4");
INSERT INTO publisher(publisher_name) VALUES ("publisher5");

-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
-- book table

CREATE TABLE book(

     book_name VARCHAR(40),
     number_of_copy INT,
     price DOUBLE,
     borrowing_pricePerDay DOUBLE,
     book_description VARCHAR(400),
     date_of_publish VARCHAR(10),
     publisher_id INT,
     
     INDEX(book_name),
     INDEX(publisher_id),
     
     PRIMARY KEY (book_name, publisher_id),
     
     FOREIGN KEY (publisher_id)
     REFERENCES publisher(publisher_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE
);

INSERT INTO book VALUES ("book1", 20, 20, 0.5, "book1 description", "12-12-2000", 1);
INSERT INTO book VALUES ("book2", 20, 25, 0.6, "book2 description", "15-1-2001", 2);
INSERT INTO book VALUES ("book3", 20, 30, 0.8, "book3 description", "1-2-2003", 3);
INSERT INTO book VALUES ("book4", 20, 10, 0.2, "book4 description", "2-5-2005", 4);
INSERT INTO book VALUES ("book5", 20, 15, 0.4, "book5 description", "9-7-1999", 5);
INSERT INTO book VALUES ("book6", 20, 19, 0.5, "book6 description", "5-9-1994", 1);
INSERT INTO book VALUES ("book7", 20, 14, 0.4, "book7 description", "16-2-1990", 2);
INSERT INTO book VALUES ("book8", 20, 25, 0.6, "book8 description", "20-4-1987", 3);
INSERT INTO book VALUES ("book9", 20, 26, 0.6, "book9 description", "30-6-2004", 4);
INSERT INTO book VALUES ("book10", 20, 27, 0.6, "book10 description", "20-8-2010", 5);
INSERT INTO book VALUES ("book11", 20, 30, 0.8, "book11 description", "10-10-2011", 1);
INSERT INTO book VALUES ("book12", 20, 40, 1, "book12 description", "12-12-1998", 2);
INSERT INTO book VALUES ("book13", 20, 19, 0.5, "book13 description", "7-12-1996", 3);

-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
-- lating day table

CREATE TABLE lating_Day(

     end_date_of_borrow VARCHAR(10),
     return_date VARCHAR(10),
     lating_days INT,
     
	 INDEX(end_date_of_borrow),
     INDEX(return_date),
     
     PRIMARY KEY(end_date_of_borrow, return_date)
);

INSERT INTO lating_day VALUES("12-12-2021", "1-12-2021",0);
INSERT INTO lating_day VALUES("12-12-2021", "14-12-2021", 2);
INSERT INTO lating_day VALUES("14-1-2022", "",0);

-----------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------
-- borrows table

CREATE TABLE borrows(
     borrows_id INT AUTO_INCREMENT,

     user_id VARCHAR(8),
     
     book_name VARCHAR(40),
     publisher_id INT,
     
     employee_id VARCHAR(8),
     date_of_borrow VARCHAR(10),
     
     end_date_of_borrow VARCHAR(10),
     return_date VARCHAR(10),
     
     PRIMARY KEY(borrows_id),
     
     FOREIGN KEY (user_id) 
     REFERENCES users(user_id)
     ON DELETE CASCADE,
     
     FOREIGN KEY (book_name)
     REFERENCES book(book_name)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
     
     FOREIGN KEY (publisher_id)
     REFERENCES book(publisher_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
     
     FOREIGN KEY (employee_id)
     REFERENCES users(user_id)
     ON DELETE SET NULL
     ON UPDATE CASCADE,
     
     FOREIGN KEY (end_date_of_borrow)
     REFERENCES lating_day(end_date_of_borrow)
     ON DELETE NO ACTION
     ON UPDATE CASCADE,
     
     FOREIGN KEY (return_date)
     REFERENCES lating_day(return_date)
     ON DELETE NO ACTION
     ON UPDATE CASCADE
);

ALTER TABLE borrows AUTO_INCREMENT = 1;

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000000","book1",1,"01000000","12-11-2021","12-12-2021","1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000000","book2", 2,"01000000", "12-11-2021","12-12-2021", "14-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000000","book3", 3,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000001","book4", 4,"01000000", "12-11-2021","12-12-2021",  "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000001","book5", 5,"01000000", "12-11-2021", "12-12-2021", "14-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000001","book6", 1,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("01000000","book7", 2,"01000000", "12-11-2021","12-12-2021",  "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("01000000","book8", 3,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000000","book9", 4,"01000000", "12-11-2021","12-12-2021",  "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000000","book10", 5,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000001","book11", 1,"01000000", "12-11-2021", "12-12-2021", "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000001","book12",2 ,"01000000", "12-11-2021", "12-12-2021", "14-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000001","book13",3 ,"01000000", "12-11-2021", "14-1-2022", "");
-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
-- author table

CREATE TABLE author(
      author_id INT AUTO_INCREMENT,
      author_name VARCHAR(20),
      
      PRIMARY KEY (author_id)
);
ALTER TABLE author AUTO_INCREMENT = 1;

INSERT INTO author (author_name) VALUES ("author1");
INSERT INTO author (author_name) VALUES ("author2");
INSERT INTO author (author_name) VALUES ("author3");
INSERT INTO author (author_name) VALUES ("author4");
INSERT INTO author (author_name) VALUES ("author5");
INSERT INTO author (author_name) VALUES ("author6");
INSERT INTO author (author_name) VALUES ("author7");
INSERT INTO author (author_name) VALUES ("author8");
INSERT INTO author (author_name) VALUES ("author9");
INSERT INTO author (author_name) VALUES ("author10");
INSERT INTO author (author_name) VALUES ("author11");
INSERT INTO author (author_name) VALUES ("author12");
INSERT INTO author (author_name) VALUES ("author13");
INSERT INTO author (author_name) VALUES ("author14");
INSERT INTO author (author_name) VALUES ("author15");
INSERT INTO author (author_name) VALUES ("author16");
INSERT INTO author (author_name) VALUES ("author17");
INSERT INTO author (author_name) VALUES ("author18");
INSERT INTO author (author_name) VALUES ("author19");
INSERT INTO author (author_name) VALUES ("author20");
INSERT INTO author (author_name) VALUES ("author21");
-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
-- book_to_author table

CREATE TABLE book_to_auther(
     book_name VARCHAR(40),
     publisher_id INT,
     author_id INT,
     
     PRIMARY KEY (book_name, publisher_id, author_id),
     
     FOREIGN KEY (book_name)
     REFERENCES book(book_name)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
     
	 FOREIGN KEY (publisher_id)
     REFERENCES book(publisher_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
     
	 FOREIGN KEY (author_id)
     REFERENCES author(author_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE
);
 
INSERT INTO book_to_auther VALUES ("book1", 1,1);
INSERT INTO book_to_auther VALUES ("book2", 2,2);
INSERT INTO book_to_auther VALUES ("book3", 3,3);
INSERT INTO book_to_auther VALUES ("book4", 4,4);
INSERT INTO book_to_auther VALUES ("book5", 5,5);
INSERT INTO book_to_auther VALUES ("book6", 1,6);
INSERT INTO book_to_auther VALUES ("book7", 2,7);
INSERT INTO book_to_auther VALUES ("book8", 3,8);
INSERT INTO book_to_auther VALUES ("book9", 4,9);
INSERT INTO book_to_auther VALUES ("book10", 5,10);
INSERT INTO book_to_auther VALUES ("book11", 1,11);
INSERT INTO book_to_auther VALUES ("book12", 2,12);
INSERT INTO book_to_auther VALUES ("book13", 3,13);

INSERT INTO book_to_auther VALUES ("book1", 1,14);
INSERT INTO book_to_auther VALUES ("book2", 2,15);
INSERT INTO book_to_auther VALUES ("book3", 3,16);
INSERT INTO book_to_auther VALUES ("book4", 4,17);
INSERT INTO book_to_auther VALUES ("book5", 5,18);
INSERT INTO book_to_auther VALUES ("book6", 1,19);
INSERT INTO book_to_auther VALUES ("book7", 2,20);
INSERT INTO book_to_auther VALUES ("book8", 3,21);
INSERT INTO book_to_auther VALUES ("book9", 4,1);
INSERT INTO book_to_auther VALUES ("book10", 5,2);
INSERT INTO book_to_auther VALUES ("book11", 1,3);
INSERT INTO book_to_auther VALUES ("book12", 2,4);
INSERT INTO book_to_auther VALUES ("book13", 3,5);

INSERT INTO book_to_auther VALUES ("book1", 1,6);
INSERT INTO book_to_auther VALUES ("book2", 2,7);
INSERT INTO book_to_auther VALUES ("book3", 3,8);
INSERT INTO book_to_auther VALUES ("book4", 4,9);
INSERT INTO book_to_auther VALUES ("book5", 5,10);
INSERT INTO book_to_auther VALUES ("book6", 1,11);
INSERT INTO book_to_auther VALUES ("book7", 2,12);
INSERT INTO book_to_auther VALUES ("book8", 3,13);
INSERT INTO book_to_auther VALUES ("book9", 4,14);
INSERT INTO book_to_auther VALUES ("book10", 5,15);
INSERT INTO book_to_auther VALUES ("book11", 1,16);
INSERT INTO book_to_auther VALUES ("book12", 2,17);
INSERT INTO book_to_auther VALUES ("book13", 3,18);

-------------------------------------------------------------------------------------------------------
-------------------------------------------------------------------------------------------------------
-- book borrowing finantial 

CREATE TABLE borrowing_financial(
      
      id INT AUTO_INCREMENT,
      book_name VARCHAR(40),
      publisher_name VARCHAR(40),
      user_id VARCHAR (8),
      adding_date VARCHAR(10),
      financial DOUBLE,
      
      PRIMARY KEY(id),
      
      FOREIGN KEY(book_name)
      REFERENCES book (book_name)
      ON DELETE SET NULL
      ON UPDATE CASCADE,
      
	  FOREIGN KEY(publisher_name)
      REFERENCES publisher (publisher_name)
      ON DELETE SET NULL
      ON UPDATE CASCADE,
      
      FOREIGN KEY(user_id)
      REFERENCES users (user_id)
      ON DELETE SET NULL
      ON UPDATE CASCADE

);
ALTER TABLE borrowing_financial AUTO_INCREMENT = 1;

INSERT INTO borrowing_financial (book_name, publisher_name, user_id, adding_date, financial)
VALUES("book2", "publisher2", "00000000", "14-12-2021", 1.2);
INSERT INTO borrowing_financial (book_name, publisher_name, user_id, adding_date, financial)
VALUES("book5", "publisher5", "01000000", "14-12-2021", 0.8);
INSERT INTO borrowing_financial (book_name, publisher_name, user_id, adding_date, financial)
VALUES("book12", "publisher2", "02000001", "14-12-2021", 2);

SELECT * FROM borrowing_financial BF, book B, publisher P, users U
WHERE B.publisher_id = P.publisher_id AND
      B.book_name = BF.book_name AND
      P.publisher_name = BF.publisher_name AND
      U.user_id = BF.user_id;

#----------------------------------------------------------------------------
# user with 2 phone numbers
SELECT U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, P1.phone_Number, P2.phone_Number
FROM users U1, phone P1, users U2, phone P2 
WHERE U1.user_id = P1.user_id AND
      U2.user_id = P2.user_id AND 
      U1.user_id =  U2.user_id AND 
      P1.phone_number > P2.phone_number;
  
#----------------------------------------------------------------------------
# user with 1 phone numbers 
SELECT U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, P1.phone_Number 
FROM users U1, phone P1
WHERE U1.user_id = P1.user_id AND U1.user_id NOT IN (
         SELECT U1.user_id
         FROM users U1, phone P1, users U2, phone P2 
         WHERE U1.user_id = P1.user_id AND
               U2.user_id = P2.user_id AND 
               U1.user_id =  U2.user_id AND 
               P1.phone_number > P2.phone_number
       );     

#----------------------------------------------------------------------------
# member with 2 phone numbers       
SELECT U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, M.fees, P1.phone_Number, P2.phone_Number, L.user_password
FROM users U1, phone P1, users U2, phone P2, member M, login L
WHERE U1.user_id = P1.user_id AND
      U2.user_id = P2.user_id AND 
      U1.user_id =  U2.user_id AND
      M.user_id = U1.user_id AND
      L.user_id = U1.user_id AND
      P1.phone_number > P2.phone_number;

#----------------------------------------------------------------------------
# memebr with 1 phone numbers      
SELECT U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, M.fees, P1.phone_Number, L.user_password 
FROM users U1, phone P1, member M, login L
WHERE U1.user_id = P1.user_id AND 
M.user_id = U1.user_id AND
 L.user_id = U1.user_id AND
U1.user_id NOT IN (
         SELECT U1.user_id
         FROM users U1, phone P1, users U2, phone P2 
         WHERE U1.user_id = P1.user_id AND
               U2.user_id = P2.user_id AND 
               U1.user_id =  U2.user_id AND
               P1.phone_number > P2.phone_number
       );  
 
#----------------------------------------------------------------------------
# hourly employee with 2 phone numbers 

Select U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, P1.phone_number, P2.phone_number, L.user_password, E.salary, E.employment_date, HE.workingHours, HE.salaryPerHour  
FROM hourly_employee HE, employee E, users U1, phone P1, users U2, phone P2, login L
WHERE U1.user_id = U2.user_id 
      AND U1.user_id = HE.user_id 
      AND U1.user_id  = E.user_id 
      AND U1.user_id = P1.user_id
      AND U1.user_id = P2.user_id
      AND U1.user_id = L.user_id
      AND P1.phone_number > P2.phone_number;
      
#----------------------------------------------------------------------------
# hourly employee with 1 phone numbers    

Select U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, P1.phone_number, L.user_password, E.salary, E.employment_date, HE.workingHours, HE.salaryPerHour  
FROM hourly_employee HE, employee E, users U1, phone P1, login L
WHERE U1.user_id = HE.user_id 
      AND U1.user_id  = E.user_id 
      AND U1.user_id = P1.user_id
      AND U1.user_id = L.user_id
      AND U1.user_id NOT IN (
           
           Select U1.user_id  
           FROM hourly_employee HE, employee E, users U1, phone P1, users U2, phone P2, login L
           WHERE U1.user_id = U2.user_id 
				AND U1.user_id = HE.user_id 
                AND U1.user_id  = E.user_id 
                AND U1.user_id = P1.user_id
                AND U1.user_id = P2.user_id
                AND U1.user_id = L.user_id
                AND P1.phone_number > P2.phone_number
      );

#----------------------------------------------------------------------------
# monthly employee with 2 phone numbers 

Select U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, P1.phone_number, P2.phone_number, L.user_password, E.salary, E.employment_date, ME.vaction_days, ME.allowd_vaction_days, D.deduction  
FROM monthly_employee ME, employee E, users U1, phone P1, users U2, phone P2, login L, deduction D
WHERE U1.user_id = U2.user_id 
      AND U1.user_id = ME.user_id 
      AND U1.user_id  = E.user_id 
      AND U1.user_id = P1.user_id
      AND U1.user_id = P2.user_id
      AND U1.user_id = L.user_id
      AND D.vaction_days = ME.vaction_days
      AND D.allowd_vaction_days = ME.allowd_vaction_days
      AND P1.phone_number > P2.phone_number;  
  
#----------------------------------------------------------------------------
# monthly employee with 1 phone numbers    

Select U1.user_id, U1.user_firstName, U1.user_lastName, U1.user_gender, U1.user_birthDate, U1.user_finantialPalance, P1.phone_number, L.user_password, E.salary, E.employment_date, ME.vaction_days, ME.allowd_vaction_days, D.deduction 
FROM monthly_employee ME, employee E, users U1, phone P1, login L, deduction D
WHERE U1.user_id = ME.user_id 
      AND U1.user_id  = E.user_id 
      AND U1.user_id = P1.user_id
      AND U1.user_id = L.user_id
      AND D.vaction_days = ME.vaction_days
	  AND D.allowd_vaction_days = ME.allowd_vaction_days
      AND U1.user_id NOT IN (
           
          Select U1.user_id  
		  FROM monthly_employee ME, employee E, users U1, phone P1, users U2, phone P2, login L, deduction D
          WHERE U1.user_id = U2.user_id 
                 AND U1.user_id = ME.user_id 
                 AND U1.user_id  = E.user_id 
                 AND U1.user_id = P1.user_id
                 AND U1.user_id = P2.user_id
                 AND U1.user_id = L.user_id
                 AND D.vaction_days = ME.vaction_days
                 AND D.allowd_vaction_days = ME.allowd_vaction_days
                 AND P1.phone_number > P2.phone_number
      );  

 #----------------------------------------------------------------------------
 # borrows select

SELECT BR.borrows_id, B.book_name, B.number_of_copy, B.price, B.borrowing_pricePerDay, B.book_description, B.date_of_publish, P.publisher_name,
       BR.employee_id, BR.date_of_borrow, BR.end_date_of_borrow, BR.return_date, LT.lating_days
FROM book B, publisher P, borrows BR, lating_Day LT
WHERE B.publisher_id = P.publisher_id AND
      B.publisher_id = BR.publisher_id AND 
	  B.book_name = BR.book_name AND
      LT.return_date = BR.return_date AND
      BR.return_date = "" AND
	  BR.user_id = "00000000"; 
---------------------------------------------------------------------------------------------
-- book

 SELECT B.book_name, B.number_of_copy, B.price, B.borrowing_pricePerDay, B.book_description, B.date_of_publish, P.publisher_name
 FROM book B, publisher P
 WHERE B.publisher_id = P.publisher_id AND
       B.book_name = "book1" AND
       P.publisher_name = "publisher1"; 

-- book and publisher        
 SELECT B.book_name, P.publisher_name
 FROM book B, publisher P
 WHERE B.publisher_id = P.publisher_id AND
	   B.number_of_copy > 0;
 
 

---------------------------------------------------------------------------------------------
-- book's authors

SELECT DISTINCT author_name FROM book_to_auther BTA, author A, publisher P
WHERE BTA.author_id = A.author_id AND
      BTA.publisher_id = P.publisher_id AND
      BTA.book_name = "book1" AND
	  P.publisher_name = "publisher1";