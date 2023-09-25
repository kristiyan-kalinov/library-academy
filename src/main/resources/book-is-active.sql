alter table book
add column is_active boolean default true,
add column deactivation_reason varchar(32) default null