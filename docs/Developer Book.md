# 1. Database Table Design

- extension

  | Name      | Type    | Length | Not null | Key     | Comment |
  | --------- | ------- | ------ | -------- | ------- | ------- |
  | id        | varchar | 255    | Y        | Primary |         |
  | name      | varchar | 255    |          |         |         |
  | type      | varchar | 255    |          |         |         |
  | module_id | varchar | 255    | Y        |         |         |
  |           |         |        |          |         |         |

- module

  | Name       | Type     | Length | Not null | Key     | Comment |
  | ---------- | -------- | ------ | -------- | ------- | ------- |
  | id         | varchar  | 255    | Y        | Primary |         |
  | name       | varchar  | 255    |          |         |         |
  | type       | varchar  | 255    |          |         |         |
  | project_id | varchar  | 255    |          |         |         |
  | properties | longtext |        |          |         |         |