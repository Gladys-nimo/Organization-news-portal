package dao;

import models.Departments;
import models.Users;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.jupiter.api.Assertions.*;

class Sql2oUsersDaoTest {
    private static Sql2oDepartmentsDao sql2oDepartmentsDao;
    private static Sql2oUsersDao sql2oUsersDao;
    private static Connection conn;


    @BeforeEach
    void setUp() throws Exception{
        String connectionString = "jdbc:postgresql://localhost:5432/organizational_news_portal_test";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "gladys");
        sql2oDepartmentsDao = new Sql2oDepartmentsDao(sql2o);
        sql2oUsersDao = new Sql2oUsersDao(sql2o);
        System.out.println("connected to database");
        conn = sql2o.open();

    }

    @AfterEach
    void tearDown() throws Exception{
        sql2oDepartmentsDao.clearAll();
        sql2oUsersDao.clearAll();
        System.out.println("clearing database");
    }
    @AfterAll
    public static void shutDown() {
        conn.close();
        System.out.println("connection is closed");
    }

    @Test
    void addingUserAndSetUserId() {
        Users user = setUpNewUser();
        int originalId= user.getId();
        sql2oUsersDao.add(user);
        assertNotEquals(originalId,user.getId());
    }

    @Test
    void addedUserIsReturnedCorrectly() {
        Users user = setUpNewUser();
        sql2oUsersDao.add(user);
        assertEquals(user.getName(),sql2oUsersDao.findById(user.getId()).getName());
    }

    @Test
    void allInstancesAreReturned() {
        Users users=setUpNewUser();
        Users otherUser= new Users("Mary","intern","Paper work");
        sql2oUsersDao.add(users);
        sql2oUsersDao.add(otherUser);
        assertEquals(users.getName(),sql2oUsersDao.getAll().get(0).getName());
        assertEquals(otherUser.getName(),sql2oUsersDao.getAll().get(1).getName());

    }

    @Test
    void getDepartmentsUser() {
        Departments department=setUpNewDepartment();
        Departments otherDepartment=new Departments("printing","printing of books");
        sql2oDepartmentsDao.add(department);
        sql2oDepartmentsDao.add(otherDepartment);
        Users user=setUpNewUser();
        Users otherUser= new Users("Mary","intern","Paper work");
        sql2oUsersDao.add(user);
        sql2oUsersDao.add(otherUser);
        sql2oDepartmentsDao.addUserToDepartment(user,department);
        sql2oDepartmentsDao.addUserToDepartment(otherUser,department);
        sql2oDepartmentsDao.addUserToDepartment(user,otherDepartment);
        assertEquals(2,sql2oUsersDao.getAllUserDepartments(user.getId()).size());
        assertEquals(1,sql2oUsersDao.getAllUserDepartments(otherUser.getId()).size());
    }
// solution

    private Users setUpNewUser() {
        return new Users("Jane John","CEO","Manager");
    }
    private Departments setUpNewDepartment() {
        return new Departments("Editing", "editing of newspaper");
    }
    }