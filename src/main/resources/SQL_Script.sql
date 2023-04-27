CREATE DATABASE AlphaSolutionsDB;
use AlphaSolutionsDB;



CREATE TABLE users(
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      boards VARCHAR(255) NOT NULL
);



CREATE TABLE boards(
                      id int PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(255) not null UNIQUE,
                      password VARCHAR(255) NOT NULL
);



CREATE TABLE columns(
                        id int PRIMARY KEY AUTO_INCREMENT,
                        Name VARCHAR(255) not null UNIQUE,
                        boardId int
);



CREATE TABLE cards(
                     id int PRIMARY KEY AUTO_INCREMENT,
                     title VARCHAR(255) NOT NULL UNIQUE,
                     minutesEstimated INT,
                     hourlyRate int,
                     boardId int,
                     columnId int
);