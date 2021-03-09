package dao;

import models.Departments;
import models.News;
import models.Users;

import java.util.List;

public interface DepartmentsDao {
    void add(Departments departments);
    void addUserToDepartment(Users user, Departments departments);
    List<Departments> getAll();
    List<Users> getAllUsersInDepartment(int DepartmentId);
    List<News> getDepartmentNews(int id);
    Departments findById(int id);

    void clearAll();
}
