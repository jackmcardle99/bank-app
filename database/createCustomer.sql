CREATE TABLE customers (
       custID smallint not null,
       prefix char(3),
       forename varchar(15),
       surname varchar(15),
       gender char(6),

       constraint  PK_customers primary key (custID)
);