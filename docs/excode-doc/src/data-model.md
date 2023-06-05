# Data Model

目前的数据模型主要基于关系型数据关系。

```xml
<models>
    <model name="student" source="ds" tablePrefix="school_">
        <field name="id" type="number" dbType="BIGINT" primary="true" comment="Student ID"/>
        <field name="name" type="string" dbType="VARCHAR" notNull="true"/>
        <field name="gender" type="enum" dbType="BIT"/>
        <field name="birthday" type="date" dbType="DATETIME"/>
    </model>
    <model name="teacher" source="ds">
        <field name="id" type="number" dbType="BIGINT" primary="true" comment="Teacher ID"/>
    </model>
</models>
```



## Model Attributes

| Name | Description | Type | Default |
| ---- | ----------- | ---- | ---- |
| name |             | `string` |      |
| source |             | `string` |      |
| tablePrefix |             | `string` |      |

## Field Attributes

| Name    | Description | Type                                          | Default |
| ------- | ----------- | --------------------------------------------- | ------- |
| name    |             | `string`                                      |         |
| type    |             | `enum` (number \| string \| enum \| date)     | string  |
| dbType  |             | `enum` (BIGINT \| VARCHAR \| DATETIME \| BIT) | VARCHAR |
| length  |             | `number`                                      | 255     |
| primary |             | `boolean`                                     | false   |
| notNull |             | `boolean`                                     | false   |
| comment |             | `string`                                      |         |
