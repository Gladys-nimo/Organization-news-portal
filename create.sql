CREATE DATABASE organizational_news_portal;
\c organizational_news_portal

CREATE TABLE departments ( id SERIAL PRIMARY KEY, name VARCHAR, description VARCHAR, size int);
CREATE TABLE news (id SERIAL PRIMARY KEY, news_type VARCHAR, department_id INT, user_id INT, title VARCHAR, description VARCHAR);
CREATE TABLE staff (id SERIAL PRIMARY KEY, name VARCHAR, position VARCHAR, staff_role VARCHAR);
CREATE DATABASE organizational_news_portal_test WITH TEMPLATE organizational_news_portal;
\q