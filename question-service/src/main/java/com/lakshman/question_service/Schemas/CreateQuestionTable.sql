create table Questions(
id SERIAL primary key,
question varchar(255) not null,
option1 varchar(255) not null,
option2 varchar(255) not null,
option3 varchar(255) not null,
option4 varchar(255) not null,
answer varchar(255) not null,
category varchar(255) not null,
question_level varchar(255) not null
);

alter table questions add is_deleted boolean default false;