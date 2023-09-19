create table book_audit_log (
id int auto_increment primary key,
action_performed varchar(64) not null,
book_id int not null,
timestamp timestamp not null default now(),
performed_by varchar(32),
old_value varchar(255),
new_value varchar(255),
foreign key (book_id) references book(id),
foreign key (performed_by) references user(username)
);