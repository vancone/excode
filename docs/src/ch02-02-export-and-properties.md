# Export and Properties

## Export

Export's attributes:

| Attribute Name |   Type   | Required | Default Value |
|    ----        |  ----    |   ----   |    ----       |
|   type         |  string  |   true   |               |
|   enable       |  boolean |          |    true       |

Export's child elements:

|  Element Name | Required |
|  ----         | ----     |
|  properties   |          |

## Properties


Here is an example:

```xml
<export type="spring-boot-backend" enable="true">
    <properties>
        <!-- property items should be inserted here -->
    </properties>
</export>
```

