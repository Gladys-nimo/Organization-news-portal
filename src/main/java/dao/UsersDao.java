package dao;

import models.Departments;
import models.Users;

import java.util.List;

public interface UsersDao {

    void add(Users users);

    List<Users> getAll();
    List<Departments> getAllDepartments(int user_id);
    Users findById(int id);

    void clearAll();
}
