package dao;

import models.Department_News;
import models.News;

import java.util.List;

public interface NewsDao {

    void addNews(News news);
    void addDepartmentNews(Department_News department_news);

    List<News> getAll();

    News findById(int id);

    void clearAll();
}
