# 快速上手

Hello World 示例

```xml
<project>
	<name>Demo</name>
    <version>1.0.0</version>
    
    <middleware>
    	<mysql id="demo">
            <host>10.10.10.1</host>
            <port>3306</port>
            <database>demo</database>
            <user>root</user>
            <password>ENC(LJdKL23rwiheOIFHLdf)</password>
        </mysql>
    </middleware>
    
    <models>
    	<model name="student" source="demo">
            <field name="id" type="string" primary="true" />
        	<field name="name" type="string" length="50" />
        </model>
    </models>
    
    <generators>
        <generator name="spring-boot">
            <plugins>
                <plugin name="lombok" enable="true"/>
            </plugins>
            
            <properties>
                <property name="project.groupId" value="com.vancone"/>
                <property name="project.artifactId" value="e-school"/>
            </properties>
    	</generator>
    </generators>
</project>
```

