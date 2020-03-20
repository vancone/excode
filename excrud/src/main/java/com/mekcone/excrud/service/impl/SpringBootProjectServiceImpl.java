package com.mekcone.excrud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.constant.JavaWords;
import com.mekcone.excrud.constant.SpringBootProject;
import com.mekcone.excrud.model.Template;
import com.mekcone.excrud.model.file.File;
import com.mekcone.excrud.model.file.javalang.JavaSourceFile;
import com.mekcone.excrud.model.file.javalang.components.Bean;
import com.mekcone.excrud.model.file.javalang.components.Expression;
import com.mekcone.excrud.model.file.javalang.components.Method;
import com.mekcone.excrud.model.file.javalang.components.Variable;
import com.mekcone.excrud.model.file.javalang.components.annotations.Annotation;
import com.mekcone.excrud.model.file.javalang.components.annotations.KeyValueAnnotation;
import com.mekcone.excrud.model.file.sql.SqlFile;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.*;
import com.mekcone.excrud.model.springboot.PluginInfo;
import com.mekcone.excrud.service.ProjectService;
import com.mekcone.excrud.service.SpringBootProjectService;
import com.mekcone.excrud.util.*;
import javafx.application.Platform;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpringBootProjectServiceImpl implements SpringBootProjectService {

    private final String EXPORT_TYPE = "spring-boot-project";

    @Autowired
    private ProjectService projectService;

    private String springBootProjectPath;

    private Export export;

    private Runnable buildRunnable = new Runnable() {
        @Override
        public void run() {
            java.io.File file = new java.io.File(springBootProjectPath);

            // Compiling
            try {
                Process child = Runtime.getRuntime().exec("mvn.cmd clean compile", null, file);

                InputStream child_in = child.getInputStream();
                int c;
                while (true) {
                    if (!((c = child_in.read()) != -1)) break;
                    final int d = c;
                    Platform.runLater(() -> LogUtil.print(String.valueOf((char) d)));
                }
            } catch (IOException e) {
                LogUtil.warn("Compiling project failed");
                return;
            }

            // Packaging
            try {
                Process child = Runtime.getRuntime().exec("mvn.cmd clean package", null, file);

                InputStream child_in = child.getInputStream();
                int c;
                while (true) {
                    if (!((c = child_in.read()) != -1)) break;
                    final int d = c;
                    Platform.runLater(() -> LogUtil.print(String.valueOf((char) d)));
                }
            } catch (IOException e) {
                LogUtil.warn("Packaging project failed");
            }
        }
    };

    private Runnable runRunnable = new Runnable() {
        @Override
        public void run() {
            java.io.File file = new java.io.File(springBootProjectPath + "target/");

            // Running
            try {
                Process child = Runtime.getRuntime().exec("java -jar " + projectService.getProject().getArtifactId() + "-" + projectService.getProject().getVersion() + ".jar", null, file);

                InputStream child_in = child.getInputStream();
                int c;
                while (true) {
                    if (!((c = child_in.read()) != -1)) break;
                    final int d = c;
                    Platform.runLater(() -> LogUtil.print(String.valueOf((char) d)));
                }
            } catch (IOException e) {
                LogUtil.warn("Running project failed");
                return;
            }
        }
    };

    @Override
    public boolean build() {
        Thread thread = new Thread(buildRunnable);
        thread.start();
        return true;
    }

    private void createDirectories() {
        List<String> paths = FileUtil.readLine("exports/spring-boot-project/directories.txt");
        springBootProjectPath = PathUtil.getPath("EXPORT_PATH") + "/" + projectService.getProject().getArtifactId() + "-backend/";
        String sourcePath = springBootProjectPath + "src/main/java/" + (projectService.getProject().getGroupId() + "." +
                projectService.getProject().getArtifactId()).replace('.', '/');
        for (String path : paths) {
            path = path.replace("SRC_PATH", sourcePath);
            FileUtil.checkDirectory(path);
        }
    }

    private void preprocessTemplate(Template template, Table table) {
        Project project = projectService.getProject();
        String templateText = template.toString();
        if (templateText.indexOf("## " + SpringBootProject.GROUP_ID + " ##") > -1) {
            template.insert(SpringBootProject.GROUP_ID, project.getGroupId());
        }
        if (templateText.indexOf("## " + SpringBootProject.ARTIFACT_ID + " ##") > -1) {
            template.insert(SpringBootProject.ARTIFACT_ID, project.getArtifactId());
        }

        if (table == null) return;

        if (templateText.indexOf("## Table ##") > -1) {
            template.insert("Table", table.getCapitalizedCamelName());
        }

        if (templateText.indexOf("## table ##") > -1) {
            template.insert("table", table.getCamelName());
        }

        if (templateText.indexOf("## primaryKey ##") > -1) {
            template.insert("primaryKey", table.getCamelPrimaryKey());
        }

        if (templateText.indexOf("## primary_key ##") > -1) {
            template.insert("primary_key", table.getPrimaryKey());
        }
    }

    private void preprocessTemplate(Template template) {
        preprocessTemplate(template, null);
    }

    @Override
    public void generate() {
        createDirectories();
        springBootProjectPath = PathUtil.getPath("EXPORT_PATH") + "/" + projectService.getProject().getArtifactId() + "-backend/";
        String resourcesPath = springBootProjectPath + "src/main/resources/";
        String sourcePath = springBootProjectPath + "src/main/java/" + (projectService.getProject().getGroupId() + "." + projectService.getProject().getArtifactId()).replace('.', '/') + "/";

        FileUtil.checkDirectory(resourcesPath);

        FileUtil.write(springBootProjectPath + "pom.xml", stringifyPomXml());
        FileUtil.write(resourcesPath + "application.properties", stringifyApplicationProperties());
        FileUtil.write(sourcePath + StringUtil.capitalizedCamel(projectService.getProject().getArtifactId()) + "Application.java", stringifyApplicationEntry());
        FileUtil.write(springBootProjectPath + projectService.getProject().getArtifactId() + ".sql", stringifySql());

        String controllerPath = sourcePath + "controller/";
        String entityPath = sourcePath + "entity/";
        String mybatisMapperPath = sourcePath + "mapper/";
        String servicePath = sourcePath + "service/";
        String serviceImplPath = sourcePath + "service/impl/";

        for (Table table : projectService.getProject().getDatabases().get(0).getTables()) {
            FileUtil.write(controllerPath + table.getCapitalizedCamelName() + "Controller.java", stringifyController(table));
            FileUtil.write(entityPath + table.getCapitalizedCamelName() + ".java", stringifyEntity(table));
            FileUtil.write(mybatisMapperPath + table.getCapitalizedCamelName() + "Mapper.java", stringifyMybatisMapper(table));
            FileUtil.write(servicePath + table.getCapitalizedCamelName() + "Service.java", stringifyService(table));
            FileUtil.write(serviceImplPath + table.getCapitalizedCamelName() + "ServiceImpl.java", stringifyServiceImpl(table));
        }

        // Config
        String configPath = sourcePath + "config/";
        for (Plugin plugin : export.getPlugins()) {
            String configText = stringifyConfig(plugin);
            if (configText != null) {
                FileUtil.write(configPath + StringUtil.capitalizedCamel(plugin.getName()) + "Config.java", configText);
            }
        }

        // Utils
        String utilPath = sourcePath + "util/";
        FileUtil.write(utilPath + "ResultVOUtil.java", stringifyUtil("ResultVOUtil"));

        // VOs
        String VOPath = sourcePath + "VO/";
        FileUtil.write(VOPath + "ResultVO.java", stringifyVO("ResultVO"));

        // throw new ExportException(ExportExceptionEnums.GENERATE_PROJECT_FAILED);
    }

    @Override
    public boolean run() {
        Thread thread = new Thread(runRunnable);
        thread.start();
        return true;
    }

    @Override
    public String stringifyApplicationEntry() {
        Project project = projectService.getProject();
        Template template = new Template(EXPORT_TYPE, "Application.java");
        preprocessTemplate(template);
        template.insert("ArtifactId", StringUtil.capitalize(project.getArtifactId()));
        return template.toString();
    }

    @Override
    public String stringifyApplicationProperties() {
        String applicationPropertiesText = "";

        for (Plugin plugin : export.getPlugins()) {
            String pluginText = FileUtil.read(PathUtil.getProgramPath() + "exports/spring-boot-project/plugins/" + plugin.getName() + ".json");
            if (pluginText == null) {
                continue;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                PluginInfo pluginInfo = objectMapper.readValue(pluginText, PluginInfo.class);
                List<Property> properties = pluginInfo.getProperties();
                if (properties != null) {
                    applicationPropertiesText += "# " + plugin.getName() + "\n";
                    for (Property property : properties) {
                        applicationPropertiesText += property.getKey() + " = ";
                        if (property.getDefaultValue() != null) {
                            applicationPropertiesText += property.getDefaultValue();
                        }
                        applicationPropertiesText += "\n";
                    }
                    applicationPropertiesText += "\n";
                }
            } catch (JsonProcessingException e) {
                LogUtil.warn(e.getMessage());
            }
        }

        // Database properties
        applicationPropertiesText += "# database\n";
        if (export.getPlugin("mybatis") != null) {
            Database database = projectService.getProject().getDatabases().get(0);
            applicationPropertiesText += "spring.datasource.url = jdbc:" + database.getType() + "://" + database.getHost() + "\n";
            applicationPropertiesText += "spring.datasource.username = " + database.getUsername() + "\n";
            applicationPropertiesText += "spring.datasource.password = " + database.getPassword() + "\n\n";
        } else if (export.getPlugin("hibernate") != null) {
            LogUtil.warn("Hibernate have not be supported yet.");
        } else {
            LogUtil.warn("No Mybatis or Hibernate module found.");
        }

        return applicationPropertiesText;
    }

    @Override
    public String stringifyConfig(Plugin plugin) {
        Project project = projectService.getProject();
        if (plugin.getName().equals("cross-origin")) {
            Template template = new Template(EXPORT_TYPE, "config/CrossOriginConfig.java");
            preprocessTemplate(template);

            Config config = plugin.getConfig("allowedOrigins");
            if (config != null) {
                String allowedOriginsText = "";
                List<String> configItems = config.getConfigItems();

                for (int i = 0; i < configItems.size(); i++) {
                    allowedOriginsText += "\"" + configItems.get(i) + "\"";
                    if (i + 1 != configItems.size()) {
                        allowedOriginsText += ",";
                    }
                }

                template.insert("allowedOrigins", ".allowedOrigins(" + allowedOriginsText + ")");
            } else {
                template.remove("allowedOrigins");
            }

            config = plugin.getConfig("allowedHeaders");
            if (config != null) {
                String allowedHeadersText = "";
                List<String> configItems = config.getConfigItems();
                for (int i = 0; i < configItems.size(); i++) {
                    allowedHeadersText += "\"" + configItems.get(i) + "\"";
                    if (i + 1 != configItems.size()) {
                        allowedHeadersText += ",";
                    }
                }
                template.insert("allowedHeaders", ".allowedHeaders(" + allowedHeadersText + ")");
            } else {
                template.remove("allowedHeaders");
            }

            config = plugin.getConfig("allowedMethods");
            if (config != null) {
                String allowedMethodsText = "";
                List<String> configItems = config.getConfigItems();
                for (int i = 0; i < configItems.size(); i++) {
                    LogUtil.info(configItems.get(i));
                    allowedMethodsText += "\"" + configItems.get(i) + "\"";
                    if (i + 1 != configItems.size()) {
                        allowedMethodsText += ",";
                    }
                }
                LogUtil.debug("METHOD FOUND -----------------------------------------------");
                template.insert("allowedMethods", ".allowedMethods(" + allowedMethodsText + ")");
            } else {
                LogUtil.debug("METHOD NOT FOUND -----------------------------------------------");
                template.remove("allowedMethods");
            }

            return template.toString();
        } else if (plugin.getName().equals(SpringBootProject.PLUGIN_SWAGGER2) && plugin.isEnable()) {
            Template template = new Template(EXPORT_TYPE, "config/Swagger2Config.java");
            preprocessTemplate(template);

            String title = project.getApiDocument().getTitle();
            if (title != null) {
                template.insert("title", title);
            } else if (project.getName() != null) {
                template.insert("title", project.getName());
            } else {
                template.insert("title", StringUtil.capitalize(project.getArtifactId()));
            }

            String description = project.getApiDocument().getDescription();
            if (description != null) {
                template.insert("description", description);
            } else {
                template.insert("description", "API Documents of " + project.getName());
            }

            template.insert("version", project.getVersion());

            String swaggerTags = "";
            List<Table> tables = project.getDatabases().get(0).getTables();
            for (int i = 0; i < tables.size(); i++) {
                swaggerTags += "new Tag(\"" + tables.get(i).getCapitalizedCamelName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
                if (i + 1 == tables.size()) {
                    swaggerTags += "\n";
                } else {
                    swaggerTags += ",\n";
                }
            }
            template.insert("tags", swaggerTags);

            return template.toString();
        }
        return null;
    }

    @Override
    public String stringifyController(Table table) {
        Template template = new Template(EXPORT_TYPE, "controller/Controller.java");
        preprocessTemplate(template, table);
        Plugin swagger2Plugin = export.getPlugin(SpringBootProject.PLUGIN_SWAGGER2);
        if (swagger2Plugin != null && swagger2Plugin.isEnable()) {
            ApiDocument apiDocument = projectService.getProject().getApiDocument();
            template.insert("swagger2Tag", "@Api(tags={\"" + table.getCapitalizedCamelName() + "\"})");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getCreateKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getRetrieveKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getRetrieveListKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getUpdateKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getDeleteKeyword() + table.getDescription() + "\")");
        } else {
            template.remove("swagger2Tag");
            template.remove("swagger2ApiOperation");
        }

        Plugin pageHelperPlugin = export.getPlugin(SpringBootProject.PLUGIN_PAGE_HELPER);
        String tableName = table.getCamelName();
        String TableName = table.getCapitalizedCamelName();
        Method method = new Method();
        method.addAnnotation(Annotation.simpleAnnotation("GetMapping"));
        method.setAccessModifier(JavaWords.PUBLIC);
        method.setReturnType("ResultVO");
        method.setName("retrieveList");

        if (pageHelperPlugin != null && pageHelperPlugin.isEnable()) {
            template.insert("importPageHelper", "import com.github.pagehelper.PageInfo;");

            KeyValueAnnotation pageNoAnnotation = new KeyValueAnnotation("RequestParam");
            pageNoAnnotation.addKeyValue("value", "pageNo");
            pageNoAnnotation.addKeyValue("defaultValue", "1");
            Variable pageNoVariable = Variable.simpleVariable(JavaWords.INT, "pageNo");
            pageNoVariable.addAnnotation(pageNoAnnotation);
            method.addParam(pageNoVariable);

            KeyValueAnnotation pageSizeAnnotation = new KeyValueAnnotation("RequestParam");
            pageSizeAnnotation.addKeyValue("value", "pageSize");
            pageSizeAnnotation.addKeyValue("defaultValue", "5");
            Variable pageSizeVariable = Variable.simpleVariable(JavaWords.INT, "pageSize");
            pageSizeVariable.addAnnotation(pageSizeAnnotation);

            method.addParam(pageSizeVariable);
            method.addExpression(Expression.simpleExpression("PageInfo<" + TableName + "> " + tableName + "List = " +
                    tableName + "Service.retrieveList(pageNo, pageSize);"));
        } else {
            template.remove("importPageHelper");

            method.addExpression(Expression.simpleExpression("List<" + TableName + "> " + tableName + "List = " +
                    tableName + "Service.retrieveList();"));
        }
        method.addExpression(Expression.returnExpression(
                Expression.simpleExpression("ResultVOUtil.success(" + tableName + "List)")));
        template.insert("retrieveListMethod", method.toString());

        return JavaFormatUtil.format(template.toString());
    }

    @Override
    public String stringifyEntity(Table table) {
        Project project = projectService.getProject();
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();

        boolean lombokEnabled = false;
        Plugin plugin = export.getPlugin(SpringBootProject.PLUGIN_LOMBOK);
        if (plugin != null) {
            if (plugin.isEnable()) {
                lombokEnabled = true;
            }
        }

        JavaSourceFile entityJavaSourceFile = new JavaSourceFile();

        Bean bean = new Bean(JavaWords.PUBLIC, table.getCapitalizedCamelName());
        if (lombokEnabled) {
            bean.addAnnotation(Annotation.simpleAnnotation("Data"));
            entityJavaSourceFile.addImportedItem("lombok.Data");
        }
        for (Column column : table.getColumns()) {
            String type = column.getType();
            if (!type.equals(JavaWords.INT)) {
                type = JavaWords.STRING;
            }

            Variable variable = Variable.privateVariable(type, column.getCamelName());
            bean.addVariable(variable);
            if (!lombokEnabled) {
                bean.addMethod(Method.getter(variable));
                bean.addMethod(Method.setter(variable));
            }
        }

        entityJavaSourceFile.setPackageName(groupId + "." + artifactId + ".entity");
        entityJavaSourceFile.setBean(bean);

        return entityJavaSourceFile.toString();
    }

    @Override
    public String stringifyMybatisMapper(Table table) {
        if (table.isPrimaryKeyBlank()) {
            LogUtil.warn("Mapper interface cannot be generated from a table without a primary key");
        }

        Template template = new Template(EXPORT_TYPE, "mapper/Mapper.java");
        preprocessTemplate(template);
        preprocessTemplate(template, table);

        String columnKeys = "";
        String columnTags = "";
        String resultAnnotations = "";
        String columnKeyTagExpressions = "";

        for (int i = 0; i < table.getColumns().size(); i++) {
            Column column = table.getColumns().get(i);
            if (!column.isPrimaryKey()) {
                columnKeys += column.getName();
                columnTags += "#{" + column.getCamelName() + "}";
                columnKeyTagExpressions += column.getName() + " = " + "#{" + column.getCamelName() + "}";
                if (i + 1 != table.getColumns().size()) {
                    columnKeys += ", ";
                    columnTags += ", ";
                    columnKeyTagExpressions += ", ";
                }
            }
            resultAnnotations += "@Result(property = \"" + column.getCamelName() + "\", column = \"" + column.getName() + "\")";
            if (i + 1 != table.getColumns().size()) {
                resultAnnotations += ",";
            }
            resultAnnotations += "\n";
        }

        template.insert("columnKeys", columnKeys);
        template.insert("columnTags", columnTags);
        template.insert("resultAnnotations", resultAnnotations);
        template.insert("columnKeyTagExpressions", columnKeyTagExpressions);

        return JavaFormatUtil.format(template.toString());
    }

    @Override
    public String stringifyPomXml() {
        if (export == null) {
            export = projectService.getProject().getSpringBootProjectExport();
        }

        Project project = projectService.getProject();

        // Root
        Element rootElement = new Element("project");
        Document document = new Document(rootElement);
        rootElement.addContent(new Comment(File.getDescription()));
        Namespace xmlns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
        rootElement.setNamespace(xmlns);
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(xsi);
        rootElement.setAttribute("schemaLocation", "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd", xsi);
        rootElement.addContent(new Element("modelVersion", xmlns).setText("4.0.0"));

        // Parent
        Element parentElement = new Element("parent", xmlns);
        rootElement.addContent(parentElement);
        parentElement.addContent(new Element(SpringBootProject.GROUP_ID, xmlns).setText("org.springframework.boot"));
        parentElement.addContent(new Element(SpringBootProject.ARTIFACT_ID, xmlns).setText("spring-boot-starter-parent"));
        parentElement.addContent(new Element(SpringBootProject.VERSION, xmlns).setText("2.2.1.RELEASE"));
        parentElement.addContent(new Element("relativePath", xmlns));

        rootElement.addContent(new Element(SpringBootProject.GROUP_ID, xmlns).setText(project.getGroupId()));
        rootElement.addContent(new Element(SpringBootProject.ARTIFACT_ID, xmlns).setText(project.getArtifactId()));
        rootElement.addContent(new Element(SpringBootProject.VERSION, xmlns).setText(project.getVersion()));
        rootElement.addContent(new Element("name", xmlns).setText(project.getArtifactId()));
        rootElement.addContent(new Element("description", xmlns).setText(
                StringUtil.capitalize(project.getArtifactId()) +
                        " Project auto-generated by MekCone ExCRUD"
        ));

        // Properties
        Element propertiesElement = new Element("properties", xmlns);
        rootElement.addContent(propertiesElement);
        propertiesElement.addContent(new Element("java.version", xmlns).setText("1.8"));

        // Dependencies
        List<Dependency> dependencies = new ArrayList<>();

        for (Plugin plugin : export.getPlugins()) {
            String pluginText = FileUtil.read(PathUtil.getProgramPath() + "exports/spring-boot-project/plugins/" + plugin.getName() + ".json");
            if (pluginText == null) {
                continue;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                PluginInfo pluginInfo = objectMapper.readValue(pluginText, PluginInfo.class);
                if (pluginInfo.getDependencies() != null) {
                    dependencies.addAll(pluginInfo.getDependencies());
                }
            } catch (JsonProcessingException e) {
                LogUtil.warn(e.getMessage());
            }
        }

        Element dependenciesElement = new Element("dependencies", xmlns);
        rootElement.addContent(dependenciesElement);

        for (Dependency dependency : dependencies) {
            Element dependencyElement = new Element("dependency", xmlns);
            dependencyElement.addContent(new Element(SpringBootProject.GROUP_ID, xmlns).setText(dependency.getGroupId()));
            dependencyElement.addContent(new Element(SpringBootProject.ARTIFACT_ID, xmlns).setText(dependency.getArtifactId()));
            if (dependency.getVersion() != null) {
                dependencyElement.addContent(new Element(SpringBootProject.VERSION, xmlns).setText(dependency.getVersion()));
            }
            if (dependency.getScope() != null) {
                dependencyElement.addContent(new Element(SpringBootProject.SCOPE, xmlns).setText(dependency.getScope()));
            }
            if (dependency.getExclusions() != null) {
                Element exclusionsElement = new Element("exclusions", xmlns);
                dependencyElement.addContent(exclusionsElement);
                Element exclusionElement = new Element("exclusion", xmlns);
                exclusionsElement.addContent(exclusionElement);
                exclusionElement.addContent(new Element("groupId", xmlns).setText("org.junit.vintage"));
                exclusionElement.addContent(new Element("artifactId", xmlns).setText("junit-vintage-engine"));
            }
            dependenciesElement.addContent(dependencyElement);
        }

        // <build></build>
        Element buildElement = new Element("build", xmlns);
        rootElement.addContent(buildElement);
        Element pluginsElement = new Element("plugins", xmlns);
        buildElement.addContent(pluginsElement);
        Element pluginElement = new Element("plugin", xmlns);
        pluginsElement.addContent(pluginElement);
        pluginElement.addContent(new Element("groupId", xmlns).setText("org.springframework.boot"));
        pluginElement.addContent(new Element("artifactId", xmlns).setText("spring-boot-maven-plugin"));

        // Generate string
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        XMLOutputter xmlOutputter = new XMLOutputter(format);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            xmlOutputter.output(document, byteArrayOutputStream);
        } catch (IOException e) {
            LogUtil.warn(e.getMessage());
        }
        return byteArrayOutputStream.toString();
    }

    @Override
    public String stringifyService(Table table) {
        Template template = new Template(EXPORT_TYPE, "service/Service.java");
        preprocessTemplate(template, table);
        Plugin pageHelperPlugin = export.getPlugin(SpringBootProject.PLUGIN_PAGE_HELPER);
        if (pageHelperPlugin != null && pageHelperPlugin.isEnable()) {
            template.insert("importPageHelper", "import com.github.pagehelper.PageInfo;");
            Method method = new Method();
            method.setReturnType("PageInfo<" + table.getCapitalizedCamelName() + ">");
            method.setName("retrieveList");
            method.addParam(Variable.simpleVariable(JavaWords.INT, "pageNo"));
            method.addParam(Variable.simpleVariable(JavaWords.INT, "pageSize"));
            method.setHasBody(false);
            template.insert("retrieveListWithPageHelper", method.toString());
        } else {
            template.remove("importPageHelper");
            template.remove("retrieveListWithPageHelper");
        }
        return JavaFormatUtil.format(template.toString());
    }

    @Override
    public String stringifyServiceImpl(Table table) {
        Template template = new Template(EXPORT_TYPE, "service/impl/ServiceImpl.java");
        preprocessTemplate(template, table);

        Plugin pageHelperPlugin = export.getPlugin(SpringBootProject.PLUGIN_PAGE_HELPER);
        if (pageHelperPlugin != null && pageHelperPlugin.isEnable()) {
            template.insert("importPageHelper", "import com.github.pagehelper.PageHelper;\nimport com.github.pagehelper.PageInfo;");
            Method method = new Method();
            method.addAnnotation(Annotation.simpleAnnotation("Override"));
            method.setAccessModifier(JavaWords.PUBLIC);
            method.setReturnType("PageInfo<" + table.getCapitalizedCamelName() + ">");
            method.setName("retrieveList");
            method.addParam(Variable.simpleVariable(JavaWords.INT, "pageNo"));
            method.addParam(Variable.simpleVariable(JavaWords.INT, "pageSize"));
            method.addExpression(Expression.simpleExpression("PageHelper.startPage(pageNo, pageSize);"));
            method.addExpression(Expression.simpleExpression("List<" + table.getCapitalizedCamelName() + "> " + table.getCamelName() + "List = retrieveList();"));
            method.addExpression(Expression.simpleExpression("PageInfo<" + table.getCapitalizedCamelName() + "> pageInfo = new PageInfo<>(" + table.getCamelName() + "List);"));
            method.addExpression(Expression.returnExpression(Expression.simpleExpression("pageInfo")));
            template.insert("retrieveListWithPageHelper", method.toString());
        } else {
            template.remove("importPageHelper");
            template.remove("retrieveListWithPageHelper");
        }

        return JavaFormatUtil.format(template.toString());
    }

    @Override
    public String stringifyUtil(String utilName) {
        Template template = new Template(EXPORT_TYPE, "util/" + utilName + ".java");
        preprocessTemplate(template);
        return template.toString();
    }

    @Override
    public String stringifyVO(String VOName) {
        Template template = new Template(EXPORT_TYPE, "VO/" + VOName + ".java");
        preprocessTemplate(template);
        return template.toString();
    }

    public String stringifySql() {
        SqlFile sqlFileModel = new SqlFile();
        sqlFileModel.setTables(projectService.getProject().getDatabases().get(0).getTables());
        return sqlFileModel.toString();
    }
}
