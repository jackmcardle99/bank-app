CREATE TABLE credentials (
       username varchar(15) not null,
       passeord varchar(15) not null,
       custID smallint not null,
       
       constraint  PK_credentials primary key (username),
       constraint FK_credentials foreign key (custID) references customers(custID)
);