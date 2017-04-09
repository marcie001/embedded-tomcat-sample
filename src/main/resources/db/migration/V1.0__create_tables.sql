create table employees (
    id char(36) not null primary key,
    full_name varchar(255) not null,
    birth_day date not null,
    salary integer not null default 0
);
