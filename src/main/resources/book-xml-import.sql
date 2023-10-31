create table book_xml_import_history(
id int primary key auto_increment,
zip_file_name varchar(255) not null,
event_date timestamp not null default now(),
xml_file_name varchar(255),
message text not null
);