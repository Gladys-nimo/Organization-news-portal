# Organization-news-Api
# Author
Gladys Kanyora

# Description
REST API for querying and retrieving scoped news and information. There should be news/articles/posts that are available to all employees without navigating into any department, and others that are housed/classified within departments (The “Further Exploration” section below explains the need for this set up).
# Set-up Instructions
1. **Git clone**
2. Use **IntelliJ** and **Java**
# Spec
1. ## User
- **creating** a **user**
{
  "user_id": "1",
  "department_id": "2",
  "name": "Janet",
  "position": "manager"'
  "staff_role": "supervising"
  }
  - **View** users by 
    **https://gladys-news-portal.herokuapp.com/users**
 2. ## Departments
    - **creating** a department
      {
      "name": "editing",
      "description": "editing new files",
      "size": "1"
      }
      - **view** the departments
        **https://gladys-news-portal.herokuapp.com/departments**
3.  ## News
- **creating** news
- **general news**
{
  "title":"Meeting",
  "description":"Discussion about expanding",
  "user_id":1
  }
  - **departmental_news**
    {
    "title":"Meeting",
    "description":"Discussion about expanding",
    "department_id":1,
    "user_id":1
    }
    -**view**
    **https://gladys-news-portal.herokuapp.com/news**
    # Technology used
    - Java
    - Json
    - Postman
    # licence
    MIT
    