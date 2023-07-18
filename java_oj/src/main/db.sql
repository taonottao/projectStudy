drop database if exists oj_database;
create database oj_database DEFAULT CHARACTER SET utf8mb4;

use oj_database;

drop table if exists oj_table;

create table oj_table (
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

