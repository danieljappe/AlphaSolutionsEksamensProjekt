CREATE DATABASE AlphaSolutionsDB;
use AlphaSolutionsDB;



CREATE TABLE users(
id int,
name VARCHAR(255) NOT NULL,
password VARCHAR(255) NOT NULL,
email VARCHAR(255) NOT NULL UNIQUE,
boards VARCHAR(255)
);



CREATE TABLE boards(
id int,
name VARCHAR(255) not null
);



CREATE TABLE columns(
id int,
Name VARCHAR(255) not null,
boardId int
);



CREATE TABLE cards(
id int,
title VARCHAR(255) NOT NULL,
description VARCHAR(255),
minutesEstimated INT,
hourlyRate float,
boardId int,
columnId int
);
