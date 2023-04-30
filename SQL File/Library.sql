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
VALUES("00000000","Osama", "Rihami", 'M', "15-1-2002", 230);
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

INSERT INTO phone VALUES ("0592504655", "00000000");
INSERT INTO phone VALUES ("0599117611", "00000000");
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
INSERT INTO member VALUES ("00000001",0);
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
     
     INDEX(workingHours),
     INDEX(salaryPerHour),
     
     PRIMARY KEY(workingHours,salaryPerHour)
);



INSERT INTO salary VALUES( 180, 12, workingHours * salaryPerHour );

ALTER TABLE hourly_employee
ADD FOREIGN KEY(salaryPerHour, workingHours) 
REFERENCES salary (salaryPerHour, workingHours)
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
     
     FOREIGN KEY (vaction_days, allowd_vaction_days)
     REFERENCES deduction(vaction_days, allowd_vaction_days)
     ON DELETE NO ACTION
     ON UPDATE CASCADE
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

INSERT INTO publisher(publisher_name) VALUES ("Kian");
INSERT INTO publisher(publisher_name) VALUES ("DAR EL ADAB");
INSERT INTO publisher(publisher_name) VALUES ("DAR EL SHOROUK");
INSERT INTO publisher(publisher_name) VALUES ("Markaz Althaqafaa");
INSERT INTO publisher(publisher_name) VALUES ("Darak");

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

INSERT INTO book VALUES ("Arnee Andur elyk", 3, 20, 0.5, "Literature", "12-12-2020", 1);
INSERT INTO book VALUES ("In Search of Lost Time", 1, 25, 0.6, "Literature", "15-1-1927", 2);
INSERT INTO book VALUES ("Ulysses", 2, 30, 0.8, "Novel", "1-2-1920", 3);
INSERT INTO book VALUES ("Don Quixote", 1, 10, 0.2, "Novel", "2-5-1612", 4);
INSERT INTO book VALUES ("One Hundred Years of Solitude", 3, 15, 0.4, "Novel", "9-7-1970", 5);
INSERT INTO book VALUES ("The Great Gatsby", 2, 19, 0.5, "Tragedy", "10-4-1925", 1);
INSERT INTO book VALUES ("Moby Dick", 3, 14, 0.4, "Novel", "18-10-1851", 2);
INSERT INTO book VALUES ("War and Peace", 2, 25, 0.6, "Novel", "20-4-1869", 3);
INSERT INTO book VALUES ("Hamlet", 2, 26, 0.6, "Tragedy", "30-6-1603", 4);
INSERT INTO book VALUES ("The Odyssey", 1, 27, 0.6, "Poetry", "20-8-1614", 5);
INSERT INTO book VALUES ("Madame Bovary", 2, 30, 0.8, "Novel", "15-10-1856", 1);
INSERT INTO book VALUES ("The Divine Comedy", 1, 40, 1, "Poetry", "12-12-1472", 2);
INSERT INTO book VALUES ("Lolita", 1, 19, 0.5, "Novel", "7-12-1955", 3);

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
     
     FOREIGN KEY (book_name, publisher_id)
     REFERENCES book(book_name, publisher_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
     
     FOREIGN KEY (employee_id)
     REFERENCES users(user_id)
     ON DELETE SET NULL
     ON UPDATE CASCADE,
     
     FOREIGN KEY (end_date_of_borrow, return_date)
     REFERENCES lating_day(end_date_of_borrow, return_date)
     ON DELETE NO ACTION
     ON UPDATE CASCADE
);

ALTER TABLE borrows AUTO_INCREMENT = 1;

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000000","Arnee Andur elyk",1,"01000000","12-11-2021","12-12-2021","1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000000","In Search of Lost Time", 2,"01000000", "12-11-2021","12-12-2021", "14-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000000","Ulysses", 3,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000001","Don Quixote", 4,"01000000", "12-11-2021","12-12-2021",  "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000001","One Hundred Years of Solitude", 5,"01000000", "12-11-2021", "12-12-2021", "14-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("00000001","The Great Gatsby", 1,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("01000000","Moby Dick", 2,"01000000", "12-11-2021","12-12-2021",  "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("01000000","War and Peace", 3,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000000","Hamlet", 4,"01000000", "12-11-2021","12-12-2021",  "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000000","The Odyssey", 5,"01000000", "12-11-2021", "14-1-2022", "");

INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000001","Madame Bovary", 1,"01000000", "12-11-2021", "12-12-2021", "1-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000001","The Divine Comedy",2 ,"01000000", "12-11-2021", "12-12-2021", "14-12-2021");
INSERT INTO borrows(user_id, book_name, publisher_id, employee_id, date_of_borrow, end_date_of_borrow, return_date ) VALUES("02000001","Lolita",3 ,"01000000", "12-11-2021", "14-1-2022", "");
-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
-- author table

CREATE TABLE author(
      author_id INT AUTO_INCREMENT,
      author_name VARCHAR(40),
      
      PRIMARY KEY (author_id)
);
ALTER TABLE author AUTO_INCREMENT = 1;

INSERT INTO author (author_name) VALUES ("Khawla Hamdi");
INSERT INTO author (author_name) VALUES ("Marcel Proust");
INSERT INTO author (author_name) VALUES ("James Joyce");
INSERT INTO author (author_name) VALUES ("Miguel de Cervantes");
INSERT INTO author (author_name) VALUES ("Gabriel Marquez");
INSERT INTO author (author_name) VALUES ("F. Scott Fitzgerald");
INSERT INTO author (author_name) VALUES ("Herman Melville");
INSERT INTO author (author_name) VALUES ("Leo Tolstoy");
INSERT INTO author (author_name) VALUES ("William Shakespeare");
INSERT INTO author (author_name) VALUES ("Homer");
INSERT INTO author (author_name) VALUES ("Gustave Flaubert");
INSERT INTO author (author_name) VALUES ("Dante Alighieri");
INSERT INTO author (author_name) VALUES ("Vladimir Nabokov");

INSERT INTO author (author_name) VALUES ("Stephen Hudson");
INSERT INTO author (author_name) VALUES ("Shakespeare");
INSERT INTO author (author_name) VALUES ("Francisco de Robles");
INSERT INTO author (author_name) VALUES ("Editorial Sudamericana");
INSERT INTO author (author_name) VALUES ("Charles Scribner's Sons");

-----------------------------------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------------------------------
-- book_to_author table

CREATE TABLE book_to_auther(
     book_name VARCHAR(40),
     publisher_id INT,
     author_id INT,
     
     PRIMARY KEY (book_name, publisher_id, author_id),
     
     FOREIGN KEY (book_name, publisher_id)
     REFERENCES book(book_name, publisher_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE,
     
	 FOREIGN KEY (author_id)
     REFERENCES author(author_id)
     ON DELETE CASCADE
     ON UPDATE CASCADE
);
 
INSERT INTO book_to_auther VALUES ("Arnee Andur elyk", 1,1);
INSERT INTO book_to_auther VALUES ("In Search of Lost Time", 2,2);
INSERT INTO book_to_auther VALUES ("Ulysses", 3,3);
INSERT INTO book_to_auther VALUES ("Don Quixote", 4,3);
INSERT INTO book_to_auther VALUES ("One Hundred Years of Solitude", 5,4);
INSERT INTO book_to_auther VALUES ("The Great Gatsby", 1,5);
INSERT INTO book_to_auther VALUES ("Moby Dick", 2,6);
INSERT INTO book_to_auther VALUES ("War and Peace", 3,7);
INSERT INTO book_to_auther VALUES ("Hamlet", 4,8);
INSERT INTO book_to_auther VALUES ("The Odyssey", 5,9);
INSERT INTO book_to_auther VALUES ("Madame Bovary", 1,10);
INSERT INTO book_to_auther VALUES ("The Divine Comedy", 2,11);
INSERT INTO book_to_auther VALUES ("Lolita", 3,12);

INSERT INTO book_to_auther VALUES ("In Search of Lost Time", 2,14);
INSERT INTO book_to_auther VALUES ("Ulysses", 3,15);
INSERT INTO book_to_auther VALUES ("Don Quixote", 4,16);
INSERT INTO book_to_auther VALUES ("One Hundred Years of Solitude", 5,17);
INSERT INTO book_to_auther VALUES ("The Great Gatsby", 1,18);

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
VALUES("In Search of Lost Time", "DAR EL ADAB", "00000000", "14-12-2021", 1.2);
INSERT INTO borrowing_financial (book_name, publisher_name, user_id, adding_date, financial)
VALUES("One Hundred Years of Solitude", "Darak", "01000000", "14-12-2021", 0.8);
INSERT INTO borrowing_financial (book_name, publisher_name, user_id, adding_date, financial)
VALUES("The Divine Comedy", "DAR EL ADAB", "02000001", "14-12-2021", 2);
