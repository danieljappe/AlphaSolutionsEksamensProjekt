CREATE DATABASE AlphaSolutionsDB;
use AlphaSolutionsDB;



CREATE TABLE users(
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL
);



CREATE TABLE board(
                      id int PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) not null UNIQUE,
                      password VARCHAR(255) NOT NULL
);



CREATE TABLE ColumnBoard(
                            id int PRIMARY KEY AUTO_INCREMENT,
                            columnname VARCHAR(255) not null UNIQUE
);



CREATE TABLE Card(
                     id int PRIMARY KEY AUTO_INCREMENT,
                     title VARCHAR(255) NOT NULL UNIQUE,
                     minutesEstimated INT,
                     hourlyRate int
);