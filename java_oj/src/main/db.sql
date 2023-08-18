drop database if exists oj_database;
create database oj_database DEFAULT CHARACTER SET utf8;

use oj_database;

drop table if exists oj_table;

create table oj_table (
    id int primary key auto_increment,
    title varchar(50),
    level varchar(50),
    description varchar(4096),
    templateCode varchar(4096),
    testCode varchar(4096),
    likeCount int default 0
);

drop table if exists userinfo;

create table userinfo (
    id int primary key auto_increment,
    username varchar(25) not null unique ,
    password varchar(65) not null
);

create table comment (
    id int primary key auto_increment,
    username varchar(25) not null ,
    pid int,
    content varchar(512) not null ,
    likeCount int default 0
)

# create table praise(
#     id int primary key auto_increment
#     uname varchar(50) not null ,
#     pid varchar(50) not null ,
#     stat int default 0
# )
