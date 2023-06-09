-- 一般对于建表的 SQL 都会单独搞个 .sql 文件来保存
-- 后续程序可能需要在不同的主机上部署, 部署的时候就需要在对应的主机上把数据库也给创建好
-- 把建表 sql 保存好, 方便在不同的主机上进行建库建表.

create database if not exists java107_blog_system;

use java107_blog_system;

drop table if exists blog;
create table blog(
    blogId int primary key auto_increment,
    title varchar(128),
    content varchar(4096),
    userId int,
    postTime datetime
);

drop table if exists user;
create table user(
    userId int primary key auto_increment,
    username varchar(50) unique,
    password varchar(50)
);

insert into user values(null, 'zhangsan', '123'), (null, 'lisi', '123');

insert into blog values(null, '我的第一篇博客', '这是博客正文', 1, '2023-06-04 12:00:00');
insert into blog values(null, '我的第二篇博客', '这是博客正文', 1, '2023-06-05 12:00:00');