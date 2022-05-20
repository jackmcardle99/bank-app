CREATE TABLE customers (
       custID smallint not null,
       prefix char(3),
       forename varchar(15),
       surname varchar(15),
       gender char(6),
       dob date,

       constraint  PK_customers primary key (custID)
);