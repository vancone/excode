# Database

ExCRUD supports multi database source in a project. However, so far only the first data source will be recognized.

Database's attributes:

| Parameter Name |   Type   | Required | Default Value |
|    ----        |  ----    |   ----   |     ----      |
|    host        |  string  |   true   |               |
|    password    |  string  |   true   |               |
|    type        |  string  |          |     mysql     |
|    username    |  string  |          |     root      |

Database's child nodes:

|   Node Name   | Required |
|   ----        |   ----   |
|   tables      |   true   |


Here is an example:

```xml
<database type="mysql" host="localhost:3306" username="root" password="123456">
    <tables>
        <!-- table items should be inserted here -->
    </tables>
</database>
```