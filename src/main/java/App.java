import com.google.gson.Gson;
import dao.*;
import exceptions.ApiException;
import models.Departments;
import models.News;
import models.Users;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main(String[] args) {
        Sql2oNewsDao NewsDao;
        Sql2oUsersDao UsersDao;
        Sql2oDepartmentsDao DepartmentsDao;
        Connection conn;
        Gson gson = new Gson();
        staticFileLocation("/public");

        String connectionString = "jdbc:postgresql://localhost:5432/organizational_news_portal";
        Sql2o sql2o = new Sql2o(connectionString, "postgres", "gladys");


    DepartmentsDao = new Sql2oDepartmentsDao(sql2o);
       UsersDao = new Sql2oUsersDao(sql2o);
      NewsDao = new Sql2oNewsDao(sql2o);
        conn = sql2o.open();

//get and create users

        post("/users/new","application/json",(request, response) -> {
            Users users = gson.fromJson(request.body(),Users.class);
          UsersDao.add(users);
            response.status(201);
            return gson.toJson(users);
        });

        get("/users", "application/json", (request, response) -> {
            if (DepartmentsDao.getAll().size() > 0){
                return gson.toJson(UsersDao.getAll());
            }
            else {
                return "{\"message\":\"I'm sorry, but no users are listed in the database.\"}";
            }
        });
        get("/user/:id/departments","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(UsersDao.getAllUserDepartments(id).size()>0){
                return gson.toJson(UsersDao.getAllUserDepartments(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but user is in no department.\"}";
            }
        });

        get("/user/:id", "application/json",(request, response) -> {
                    int id = Integer.parseInt(request.params("id"));
                    if (UsersDao.findById(id) == null) {
                        throw new ApiException(404, String.format("No user with the id: \"%s\" exists",
                                request.params("id")));
                    } else {
                        return gson.toJson(UsersDao.findById(id));
                    }
                });

// departments

        post("departments/new", "application/json",(request, response) -> {
            Departments departments = gson.fromJson(request.body(),Departments.class);
            DepartmentsDao.add(departments);
            response.status(201);
            return gson.toJson(departments);
        });


        get("/department/:id/users","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(DepartmentsDao.getAllUsersInDepartment(id).size()>0){
                return gson.toJson(DepartmentsDao.getAllUsersInDepartment(id));
            }
            else {
                return "{\"message\":\"I'm sorry, but department has no users.\"}";
            }
        });


        get("/department/:id","application/json",(request, response) -> {
            int id=Integer.parseInt(request.params("id"));
            if(DepartmentsDao.findById(id)==null){
                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
                        request.params("id")));
            }
            else {
                return gson.toJson(DepartmentsDao.findById(id));
            }
        });
        get("/departments","application/json", (request, response) -> {
            response.type("application/json");
            return gson.toJson(DepartmentsDao.getAll());//send it back to be displayed
        });


        post("/news/new","application/json", (request, response) -> {
            News news = gson.fromJson(request.body(),News.class);
            NewsDao.addNews(news);
            response.status(201);
            return gson.toJson(news);
        });
        get("/news","application/json", (request, response) -> {
            System.out.println(NewsDao.getAll());
            return gson.toJson(NewsDao.getAll());
        });

//        post("/news/new","application/json",(request, response) -> {
//            News department_news =gson.fromJson(request.body(),News.class);
//            Departments departments=sql2oDepartmentsDao.findById(department_news.getDepartment_id());
//            Users users=sql2oUsersDao.findById(department_news.getUser_id());
//            if(departments==null){
//                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
//                        request.params("id")));
//            }
//            if(users==null){
//                throw new ApiException(404, String.format("No user with the id: \"%s\" exists",
//                        request.params("id")));
//            }int id=Integer.parseInt(request.params("id"));
////            Departments departments=sql2oDepartmentsDao.findById(id);
////            if(departments==null){
////                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
////                        request.params("id")));
////            }
////            if(sql2oDepartmentsDao.getDepartmentNews(id).size()>0){
////                return gson.toJson(sql2oDepartmentsDao.getDepartmentNews(id));
////            }
////            else {
////                return "{\"message\
//            sql2oNewsDao.addNews(department_news);
//            response.status(201);
//            return gson.toJson(department_news);
//        });
        get("/news/:id","application/json",(request, response) -> {
            int newsId = Integer.parseInt(request.params("id"));
            News newsToFind = NewsDao.findById(newsId);
            if (newsToFind==null){
                throw new ApiException(404, String.format("No news with the id: \"%s\" exists",request.params("id")));
            }
            return gson.toJson(newsToFind);
                });

//

        // news get and post


        post("/news/new/general","application/json",(request, response) -> {

            News news =gson.fromJson(request.body(), News.class);
            NewsDao.addNews(news);
            response.status(201);
            return gson.toJson(news);
        });
//        post("/add/user/:user_id/department/:department_id","application/json",(request, response) -> {
//
//            int user_id=Integer.parseInt(request.params("user_id"));
//            int department_id=Integer.parseInt(request.params("department_id"));
//            Departments departments=DepartmentsDao.findById(department_id);
//            Users users=UsersDao.findById(user_id);
//            if(departments==null){
//                throw new ApiException(404, String.format("No department with the id: \"%s\" exists",
//                        request.params("department_id")));
//            }
//            if(users==null){
//                throw new ApiException(404, String.format("No user with the id: \"%s\" exists",
//                        request.params("user_id")));
//            }
//           DepartmentsDao.addUserToDepartment(users,departments);
//
//            List<Users> departmentUsers=DepartmentsDao.getAllUsersInDepartment(departments.getId());
//
//            response.status(201);
//            return gson.toJson(departmentUsers);
//        });

        exception(ApiException.class, (exception, request, response) -> {
            ApiException err = exception;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            response.type("application/json");
            response.status(err.getStatusCode());
            response.body(gson.toJson(jsonMap));
        });


        after((request, response) ->{
            response.type("application/json");
        });
    }


}
