# Column

In some cases, some column names may contain the table name as a prefix. For example, there may be a column named ``student_id`` in the table ``student``. When ExCRUD detected this, it will removed this redundant prefix when generating codes and API documents.

Column's attributes:

| Attribute Name |   Type   | Required | Default Value |
|    ----        |  ----    |   ----   |     ----      |
|   description  |  string  |          |               |
|   detail       |  boolean |          |  false        |
|   name         |  string  |   true   |               |
|   primaryKey   |  boolean |          |  false        |
|   type         |  string  |          |  varchar(50)  |

- detail  
  If the value of this attribute is ``true``, this column will be ignored when the request is retrieving the list of table items.


Here is an example:

```xml
<column name="student_id" type="varchar(50)" primaryKey="true" description="Student ID" detail="false"/>
```