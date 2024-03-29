# 快速上手

## 常用命令

```bash
# 生成项目
excode-cli gen path/your-project.xml
```

## 演示项目：e-School

```xml
<project>
    <name>e-school</name>
    <version>0.1</version>

    <middleware>
        <mysql name="ds" port="3306" />
    </middleware>

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

    <templates>
        <template type="spring-boot-mybatis" external="true">
            <plugins>
                <plugin name="lombok" enabled="true" />
            </plugins>

            <properties>
                <property name="project.groupId" value="com.vancone" />
                <property name="project.artifactId" value="eschool" />
                <property name="spring.application.name" value="e-school" />
                <property name="server.port" value="8080" />
                <property name="cross-origin.allowed-methods" value="*" />
                <property name="cross-origin.allowed-headers" value="*" />
            </properties>
        </template>
    </templates>
</project>
```

