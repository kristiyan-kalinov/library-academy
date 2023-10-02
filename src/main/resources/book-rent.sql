create table rent (
id int auto_increment primary key,
user_id int not null,
book_id int not null,
rent_date date not null default (current_date),
return_date date,
expected_return_date date not null,
foreign key (user_id) references user(id),
foreign key (book_id) references book(id)
);

alter table book
add column available_quantity int not null,
add column total_quantity int not null;

alter table user
add column has_prolonged_rents boolean not null default false;