#!/bin/bash
echo -n "Enter the mysql username: "
read username
echo -n "Enter the mysql password: "
read -s password
createDB="create database if not exists article;"
mysql -u $username -p$password -e "$createDB"
echo "create database successfully"
createArticle_table="use article;create table if not exists article_table (
    id int not null auto_increment,
    title varchar(512) not null,
    mdate date,
    keywords char(128),
    authors varchar(256),
    pages char(16),
    year GEOMETRY not null,
    volume char(16),
    journal char(128),
    number char(16),
    ee char(128),
    url char(128),
    primary key(id),
    SPATIAL INDEX(year)
    )
    ENGINE=MyISAM;"
mysql -u $username -p$password -e "$createArticle_table"
echo "create article_table successfully"
