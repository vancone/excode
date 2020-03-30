# Table

Table's attributes:

| Parameter Name |   Type   | Required | Default Value |
|    ----        |  ----    |   ----   |     ----      |
|   description  |  string  |          |               |
|   name         |  string  |   true   |               |

Table's child nodes:

|   Node Name   | Required |
|   ----        |   ----   |
|   columns     |   true   |


Here is an example:

```xml
<table name="student" description="Student Object">
    <columns>
        <!-- column items should be inserted here -->
    </columns>
</table>
```