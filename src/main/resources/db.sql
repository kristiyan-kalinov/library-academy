create database library;

use library;

create table book (
id int auto_increment primary key,
isbn char(17) not null unique,
title varchar(255) not null,
year smallint not null,
publisher varchar(64) not null,
date_added datetime default now() not null
);

create table author (
id int auto_increment primary key,
first_name varchar(64) not null,
last_name varchar(64) not null
);

create table genre (
id int auto_increment primary key,
name varchar(64) not null unique
);

create table user (
id int auto_increment primary key,
username varchar(32) not null unique,
first_name varchar(64) not null,
last_name varchar(64) not null,
display_name varchar(32) not null,
password varchar(400) not null,
date_of_birth date,
role varchar(16) not null
);

create table book_author (
book_id int not null,
author_id int not null,
primary key (book_id, author_id),
foreign key (book_id) references book (id),
foreign key (author_id) references author (id)
);

create table book_genre (
book_id int not null,
genre_id int not null,
primary key (book_id, genre_id),
foreign key (book_id) references book (id),
foreign key (genre_id) references genre (id)
);

insert into genre (name)
values
('Mystery'),
('Science Fiction'),
('Fantasy'),
('Romance'),
('Horror'),
('Historical Fiction'),
('Biography/Autobiography'),
('Poetry'),
('Adventure'),
('Self-Help');