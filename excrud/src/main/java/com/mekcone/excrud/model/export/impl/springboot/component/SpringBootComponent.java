package com.mekcone.excrud.model.export.impl.springboot.component;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.mekcone.excrud.constant.basic.SpringBootComponentType;
import com.mekcone.excrud.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.export.impl.relationaldatabase.component.Table;
import lombok.Data;

@Data
public class SpringBootComponent {

    private JavaTemplate javaTemplate;

    private String name;

    private MethodDeclaration creatingMethod;
    private MethodDeclaration retrievingMethod;
    private MethodDeclaration retrievingListMethod;
    private MethodDeclaration updatingMethod;
    private MethodDeclaration deletingMethod;

    public SpringBootComponent(String templatePath, Project project, Table table) {
        javaTemplate = new JavaTemplate(templatePath);
        javaTemplate.preprocessForSpringBootProject(project, table);

        if (templatePath.toLowerCase().contains(SpringBootComponentType.CONTROLLER)) {
            name = table.getUpperCamelCaseName() + "Controller";
        } else if (templatePath.toLowerCase().contains(SpringBootComponentType.MAPPER)) {
            name = table.getUpperCamelCaseName() + "Mapper";
        } else if (templatePath.toLowerCase().contains(SpringBootComponentType.SERVICE_IMPL.toLowerCase())) {
            name = table.getUpperCamelCaseName() + "ServiceImpl";
        } else if (templatePath.toLowerCase().contains(SpringBootComponentType.SERVICE)) {
            name = table.getUpperCamelCaseName() + "Service";
        }
    }

    @Override
    public String toString() {
        return javaTemplate.toString();
    }
}
