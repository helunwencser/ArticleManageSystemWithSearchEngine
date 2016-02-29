This is the read me file for project ArticleManageSystemWithSearchEngine.

Author: Lunwen He
Andrew ID: lunwenh
Date: 02/28/2016

This project is based on project ArticleManageSystemWithMySQL. It adds search engine as new feature using Apache Lucene.

Runtime Environment:
    java: 1.8.0
    jdbc: 5.0.8
    lucene: 5.5.0

How to run:
    1. import project into eclipse workspace.
    2. run ./create.sh to create database
    3. run ./src/org/cmu/edu/etl/ETL.java as Java application to load data into MySQL database
    4. run ./src/org/cmu/edu/driver/DriverForBasicSearch.java as java application to do basic search
    5. run ./src/org/cmu/edu/driver/DriverForSpatialSearch.java as java application to do spatial search

Spatial data and spatial index:
    year is choosed as spatial data, it's type is POINT(latitude longitude), we set all longitude to 0, so that region is specified by start (year, 0) and end (year, 0).
