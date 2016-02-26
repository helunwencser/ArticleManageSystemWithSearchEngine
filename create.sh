#!/bin/bash
echo -n "Enter the mysql username: "
read username
echo -n "Enter the mysql password: "
read -s password
createDB="create database if not exists article;"
mysql -u $username -p$password -e "$createDB"
echo "create database successfully"
createCoauthor_Table="use article;create table if not exists coauthor_table (
    id int not null auto_increment,
    authorA char(128) not null,
    authorB char(128) not null,
    primary key(id)
    );"
mysql -u $username -p$password -e "$createCoauthor_Table"
echo "create coauthor_table successfully"
createArticle_table="use article;create table if not exists article_table (
    id int not null auto_increment,
    title varchar(512) not null,
    mdate date,
    keywords char(128),
    authors varchar(256),
    pages char(16),
    year char(16),
    volume char(16),
    journal char(128),
    number char(16),
    ee char(128),
    url char(128),
    primary key(id)
    );"
mysql -u $username -p$password -e "$createArticle_table"
echo "create article_table successfully"
createArticle_author_table="use article;create table if not exists article_author_table (
    id int not null auto_increment,
    title varchar(512) not null,
    author char(128),
    primary key(id)
    );"
mysql -u $username -p$password -e "$createArticle_author_table"
echo "create article_author_table successfully"
