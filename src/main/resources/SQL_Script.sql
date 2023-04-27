CREATE DATABASE AlphaSolutionsDB;
use AlphaSolutionsDB;



CREATE TABLE users(
                      id INT,
                      name VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      boards VARCHAR(255)
);



CREATE TABLE boards(
                      id int,
                      name VARCHAR(255) not null UNIQUE,
                      password VARCHAR(255) NOT NULL
);



CREATE TABLE columns(
                        id int,
                        Name VARCHAR(255) not null UNIQUE,
                        boardId int
);



CREATE TABLE cards(
                     id int,
                     title VARCHAR(255) NOT NULL UNIQUE,
                     minutesEstimated INT,
                     hourlyRate int,
                     boardId int,
                     columnId int
);