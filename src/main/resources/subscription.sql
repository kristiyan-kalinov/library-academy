create table subscription(
id int auto_increment primary key,
tier varchar(32),
cost decimal(12,2),
max_rent_days int,
max_rent_books int
);

insert into subscription(tier, cost, max_rent_days, max_rent_books)
values ('BRONZE', 10.00, 30, 3), 
('SILVER', 15.00, 45, 4),
('GOLD', 20.00, 60, 5);

alter table user
add column balance decimal(12,2) not null default 0.00;

alter table user
add column cancelled_subscription boolean not null default false;

alter table user
add column subscription_id int default null,
add constraint fk_user_subscription
foreign key (subscription_id)
references subscription(id);