package controller.generators;

import model.project.Project;

public class PomGenerator {
    public PomGenerator(Project project, String path) {
        /*String code = "";
        code += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
        code += "<!--" + ProjectBuilder.getDescription() + "-->\n";
        code += "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n";
        code += "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n";
        code += "    <modelVersion>4.0.0</modelVersion>\n";
        code += "    <parent>\n";
        code += "        <groupId>org.springframework.boot</groupId>\n";
        code += "        <artifactId>spring-boot-starter-parent</artifactId>\n";
        code += "        <version>2.2.1.RELEASE</version>\n";
        code += "        <relativePath/> <!-- lookup parent from repository -->\n";
        code += "    </parent>\n";
        code += "    <groupId>" + project.getGroupId() + "</groupId>\n";
        code += "    <artifactId>" + project.getArtifactId() + "</artifactId>\n";
        code += "    <version>" + project.getVersion() + "</version>\n";
        code += "    <name>" + project.getArtifactId() + "</name>\n";
        code += "    <description>" + project.getDescription() + "</description>\n\n";
        code += "    <properties>\n";
        code += "        <java.version>1.8</java.version>\n";
        code += "    </properties>\n\n";
        code += "    <dependencies>\n";

        //code += "\n";

        for (Dependency dependency: project.getDependencies()) {
            if (dependency.getDependencyId() != null && !dependency.getDependencyId().isEmpty()) {
                code += dependency.toString();
            }
        }

        code += "    </dependencies>\n\n";

        code += "    <build>\n";
        code += "        <plugins>\n";
        code += "            <plugin>\n";
        code += "                <groupId>org.springframework.boot</groupId>\n";
        code += "                <artifactId>spring-boot-maven-plugin</artifactId>\n";
        code += "            </plugin>\n";
        code += "        </plugins>\n";
        code += "    </build>\n\n";
        code += "</project>\n";

        FileManager.write(path + "/pom.xml", code);
        Logger.output(LogType.INFO, "Output model bean file \"" + path + "/pom.xml" + "\"");*/
    }
}
