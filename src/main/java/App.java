import com.google.gson.Gson;
import dao.Sql2oDepartmentsDao;
import dao.Sql2oNewsDao;
import dao.Sql2oUsersDao;
import exceptions.ApiException;
import models.Departments;
import models.Users;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Sql2oNewsDao sql2oNewsDao;
        Sql2oUsersDao sql2oUsersDao;
        Sql2oDepartmentsDao sql2oDepartmentsDao;
        Connection conn;
        Gson gson = new Gson();
        staticFileLocation("/public");

        String connectionString = "jdbc:postgresql://localhost:5432/organizational_news_portal_test";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "gladys");


     sql2oDepartmentsDao = new Sql2oDepartmentsDao(sql2o);
        sql2oUsersDao = new Sql2oUsersDao(sql2o);
        sql2oNewsDao = new Sql2oNewsDao(sql2o);
        conn = sql2o.open();

//get and create users

        post("/users/new","application/json",(request, response) -> {
            Users users = gson.fromJson(request.body(),Users.class);
            sql2oUsersDao.add(users);
            response.status(201);
            return gson.toJson(users);
        });

        get("/users", "application/json", (request, response) -> {
            if (sql2oDepartmentsDao.getAll().size() > 0){
                return gson.toJson(sql2oUsersDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no users are listed in the database.\"}";
            }
        });
        get("/user/:id/departments","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oUsersDao.getAllUserDepartments(id).size()>0){
                return gson.toJson(sql2oUsersDao.getAllUserDepartments(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but user is in no department.\"}";
            }
        });

        get("/user/:id", "application/json",(request, response) -> {
                    int id = Integer.parseInt(request.params("id"));
                    if (sql2oUsersDao.findById(id) == null) {
                        throw new ApiException(404, String.format("No user with the id: \"%s\" exists",
                                request.params("id")));
                    } else {
                        return gson.toJson(sql2oUsersDao.findById(id));
                    }
                });

// departments

        post("departments/new", "application/json",(request, response) -> {
            Departments departments = gson.fromJson(request.body(),Departments.class);
            sql2oDepartmentsDao.add(departments);
            response.status(201);
            return gson.toJson(departments);
        });
        get("/department/:id/users","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentsDao.getAllUsersInDepartment(id).size()>0){
                return gson.toJson(sql2oDepartmentsDao.getAllUsersInDepartment(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but department has no users.\"}";
            }
        });
        get("/department/:id","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(sql2oDepartmentsDao.findById(id)==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("id")));
            }
            else {
                return gson.toJson(sql2oDepartmentsDao.findById(id));
            }
        });
        get("/news/department/:id","application/json",(request, response) -> {

            int id=Integer.parseInt(request.params("id"));
            Departments departments=sql2oDepartmentsDao.findById(id);
            if(departments==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("id")));
            }
            if(sql2oDepartmentsDao.getDepartmentNews(id).size()>0){
                return gson.toJson(sql2oDepartmentsDao.getDepartmentNews(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but no news in this department.\"}";
            }
        });
    }


}
