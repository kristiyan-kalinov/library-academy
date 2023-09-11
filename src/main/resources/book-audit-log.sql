create table book_audit_log (
id int auto_increment primary key,
action_performed varchar(16) not null,
book_id int not null,
timestamp timestamp not null default now(),
performed_by varchar(32) not null,
old_title_value varchar(255),
new_title_value varchar(255),
old_publisher_value varchar(64),
new_publisher_value varchar(64),
foreign key (book_id) references book(id),
foreign key (performed_by) references user(username)
);