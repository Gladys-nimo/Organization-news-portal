package dao;

import models.Department_News;
import models.Departments;
import models.News;
import models.Users;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oNewsDaoTest {
    private static Sql2oUsersDao sql2oUsersDao;
    private static Sql2oNewsDao sql2oNewsDao;
    private static Sql2oDepartmentsDao sql2oDepartmentsDao;
    private static Connection conn;

    @BeforeEach
    void setUp() throws Exception{
        String connectionString = "jdbc:postgresql://ec2-54-161-239-198.compute-1.amazonaws.com:5432/d439h64au1jmgi"; //!
        Sql2o sql2o = new Sql2o(connectionString, "hdxebpuatbedtu", "0de3585a26b037643e97aa6838f478f9aa7e7c268ad1600a21d6ec5b233d07f9"); //!

//        String connectionString = "jdbc:postgresql://localhost:5432/organizational_news_portal";
//        Sql2o sql2o = new Sql2o(connectionString, "postgres", "gladys");
//        sql2oDepartmentsDao = new Sql2oDepartmentsDao(sql2o);
        sql2oUsersDao = new Sql2oUsersDao(sql2o);
        sql2oNewsDao = new Sql2oNewsDao(sql2o);
        System.out.println("connected to database");
        conn = sql2o.open();

    }

    @AfterEach
    void tearDown() throws Exception{
        sql2oDepartmentsDao.clearAll();
        sql2oNewsDao.clearAll();
        sql2oUsersDao.clearAll();
        System.out.println("clear the database");
    }
    @AfterAll
    public static void shutDown() throws Exception{
        conn.close();
        System.out.println("connection is closed");
    }

    @Test
    void addNews() {
        Users users=setUpNewUsers();
        sql2oUsersDao.add(users);
        Departments departments=setUpDepartment();
        sql2oDepartmentsDao.add(departments);
        News news=new News("Meeting","Meeting to set activities for team building",users.getId());
        sql2oNewsDao.addNews(news);

        assertEquals(users.getId(),sql2oNewsDao.findById(news.getId()).getUser_id());
        assertEquals(news.getDepartment_id(),sql2oNewsDao.findById(news.getId()).getDepartment_id());
    }

    @Test
    void addDepartmentNews() {
        Users users=setUpNewUsers();
        sql2oUsersDao.add(users);
        Departments departments=setUpDepartment();
        sql2oDepartmentsDao.add(departments);
        Department_News department_news =new Department_News("Meeting","To nominate new chairman",departments.getId()
                ,users.getId());
        sql2oNewsDao.addDepartmentNews(department_news);
        assertEquals(users.getId(),sql2oNewsDao.findById(department_news.getId()).getUser_id());
        assertEquals(department_news.getDepartment_id(),sql2oNewsDao.findById(department_news.getId()).getDepartment_id());

    }

    @Test
    void getAll() {
        Users users=setUpNewUsers();
        sql2oUsersDao.add(users);
        Departments departments=setUpDepartment();
        sql2oDepartmentsDao.add(departments);
        Department_News department_news =new Department_News("Meeting","To nominate new chairman",departments.getId()
                ,users.getId());
        sql2oNewsDao.addDepartmentNews(department_news);
        News news=new News("Meeting","Meeting to set activities for team building",users.getId());
        sql2oNewsDao.addNews(news);
        assertEquals(2,sql2oNewsDao.getAll().size());
    }


// solution

    private Users setUpNewUsers() {
        return new Users("Jane John","CEO","Manager");
    }
    private Departments setUpDepartment() {
        return new Departments("Editing","edit of news paper");
    }
}