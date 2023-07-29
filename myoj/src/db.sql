drop database if exists ojdb;
create database ojdb DEFAULT CHARACTER SET utf8;

use ojdb;

drop table if exists probleminfo;

create table probleminfo (
                          id int primary key auto_increment,
                          title varchar(50),
                          level varchar(50),
                          description varchar(4096),
                          templateCode varchar(4096),
                          testCode varchar(4096)
);

drop table if exists userinfo;

create table userinfo (
                          id int primary key auto_increment,
                          username varchar(25) not null unique ,
                          password varchar(65) not null
);