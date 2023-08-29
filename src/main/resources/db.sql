create database library;

use library;

create table book (
id int auto_increment primary key,
isbn char(13) not null unique,
title varchar(255) not null,
year smallint not null,
publisher varchar(64) not null,
dateAdded datetime default now() not null
);

create table author (
id int auto_increment primary key,
firstName varchar(64) not null,
lastName varchar(64) not null
);

create table genre (
id int auto_increment primary key,
name varchar(32) not null
);

create table user (
id int auto_increment primary key,
username varchar(32) not null unique,
firstName varchar(64) not null,
lastName varchar(64) not null,
displayName varchar(32) not null,
password varchar(400) not null,
dateOfBirth date,
role varchar(32) not null
);

create table book_author (
bookId int not null,
authorId int not null,
primary key (bookId, authorId),
foreign key (bookId) references book (id),
foreign key (authorId) references author (id)
);

create table book_genre (
bookId int not null,
genreId int not null,
primary key (bookId, genreId),
foreign key (bookId) references book (id),
foreign key (genreId) references genre (id)
);

drop table author;
drop table book;
drop table genre;
drop table user;
drop table book_author;
drop table book_genre;

show tables;