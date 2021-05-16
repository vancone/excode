# Table

Table's attributes:

| Attribute Name |   Type   | Required | Default Value |
|    ----        |  ----    |   ----   |     ----      |
|   description  |  string  |          |               |
|   name         |  string  |   true   |               |

Table's child elements:

|  Element Name | Required |
|  ----         |   ----   |
|  columns      |   true   |


Here is an example:

```xml
<table name="student" description="Student Object">
    <columns>
        <!-- column items should be inserted here -->
    </columns>
</table>
```