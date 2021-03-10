package dao;

import models.Department_News;
import models.Departments;
import models.News;
import models.Users;
import org.eclipse.jetty.http.HttpParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oDepartmentsDaoTest {

    private static Sql2oDepartmentsDao sql2oDepartmentsDao;
    private static Sql2oNewsDao sql2oNewsDao;
    private static Sql2oUsersDao sql2oUsersDao;
    private static Connection conn;

    @BeforeEach
    void setUp() throws Exception {
        String connectionString = "jdbc:postgresql://localhost:5432/organizational_news_portal";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "gladys");
        sql2oDepartmentsDao = new Sql2oDepartmentsDao(sql2o);
        sql2oUsersDao = new Sql2oUsersDao(sql2o);
        sql2oNewsDao = new Sql2oNewsDao(sql2o);
        System.out.println("connected to database");
        conn = sql2o.open();
    }

    @AfterEach
    void tearDown() throws Exception {
        sql2oDepartmentsDao.clearAll();
        sql2oNewsDao.clearAll();
        sql2oUsersDao.clearAll();
        System.out.println("clear the database");
    }

    @AfterAll
    public static void shutDown() throws Exception {
       conn.close();
        System.out.println("connection is closed");
    }

    @Test
    void idSetForAddedDepartment() {
        Departments department = setUpNewDepartment();
        int originalId = department.getId();
        sql2oDepartmentsDao.add(department);
        assertNotEquals(originalId, department.getId());
    }

    @Test
    void addUserToADepartment() {
        Departments department=setUpNewDepartment();
        sql2oDepartmentsDao.add(department);
        Users user=setUpNewUser();
        Users otherUser= new Users("Mary","intern","Paper work");
        sql2oUsersDao.add(user);
        sql2oUsersDao.add(otherUser);
        sql2oDepartmentsDao.addUserToDepartment(user,department);
        sql2oDepartmentsDao.addUserToDepartment(otherUser,department);
        assertEquals(2,sql2oDepartmentsDao.getAllUsersInDepartment(department.getId()).size());
        assertEquals(2,sql2oDepartmentsDao.findById(department.getId()).getSize());
    }

    @Test
    void getAll() {
        Departments department=setUpNewDepartment();
        Departments otherDepartment=new Departments("printing","printing of books");
        sql2oDepartmentsDao.add(department);
        sql2oDepartmentsDao.add(otherDepartment);
        assertEquals(department,sql2oDepartmentsDao.getAll().get(0));
        assertEquals(otherDepartment,sql2oDepartmentsDao.getAll().get(1));
    }

    @Test
    void correctDepartmentIsReturnUsingFindById() {
        Departments department=setUpNewDepartment();
        Departments otherDepartment=new Departments("printing","printing of books");
        sql2oDepartmentsDao.add(department);
        sql2oDepartmentsDao.add(otherDepartment);
        assertEquals(department,sql2oDepartmentsDao.findById(department.getId()));
        assertEquals(otherDepartment,sql2oDepartmentsDao.findById(otherDepartment.getId()));
    }

    @Test
    void getAllUsersInADepartment() {
        Departments department=setUpNewDepartment();
        sql2oDepartmentsDao.add(department);
        Users user=setUpNewUser();
        Users otherUser= new Users("Mary","intern","Paper work");
        sql2oUsersDao.add(user);
        sql2oUsersDao.add(otherUser);
        sql2oDepartmentsDao.addUserToDepartment(user,department);
        sql2oDepartmentsDao.addUserToDepartment(otherUser,department);
        assertEquals(2,sql2oDepartmentsDao.getAllUsersInDepartment(department.getId()).size());
        assertEquals(2,sql2oDepartmentsDao.findById(department.getId()).getSize());

    }

    @Test
    void getDepartmentNews() {
        Users users=setUpNewUser();
        sql2oUsersDao.add(users);
        Departments departments=setUpNewDepartment();
        sql2oDepartmentsDao.add(departments);
        Department_News department_news =new Department_News("Meeting","To nominate new chairman",departments.getId()
                ,users.getId());
        sql2oNewsDao.addDepartmentNews(department_news);
        News news=new News("Meeting","Meeting to set activities for team building",users.getId());
        sql2oNewsDao.addNews(news);

        assertEquals(department_news.getTitle(),sql2oDepartmentsDao.getDepartmentNews(department_news.getId()).get(0).getTitle());

    }

    // solution
    private Departments setUpNewDepartment() {
        return new Departments("Editing", "edit of newspapers");
    }
    private Users setUpNewUser() {
        return new Users("Jane John","CEO","Manager");
    }
}