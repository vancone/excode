package com.vancone.excode.service.microservice;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.vancone.excode.entity.Output;
import com.vancone.excode.entity.Template;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.enums.OrmType;
import com.vancone.excode.enums.SpringProfile;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.service.TemplateService;
import com.vancone.excode.util.CompilationUnitUtil;
import com.vancone.excode.util.StrUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author Tenton Lien
 * @since 2022/05/19
 */
@Service
public class SpringBootMicroserviceGenerator {

    @Autowired
    private ProjectObjectModelService projectObjectModelService;

    @Autowired
    private ApplicationPropertyService applicationPropertyService;

    @Autowired
    private TemplateService templateService;

    public Output createPom(SpringBootMicroservice microservice) {
        return projectObjectModelService.generate(microservice);
    }

    public Output createProperty(SpringBootMicroservice microservice) {

        return applicationPropertyService.generate(SpringProfile.DEV, microservice);
    }

    public Output createApplicationEntry(SpringBootMicroservice microservice) {
        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_APPLICATION_ENTRY);
        templateService.preProcess(microservice, template);
        return new Output(TemplateType.SPRING_BOOT_APPLICATION_ENTRY,
                getPackagePath(microservice) + StrUtil.toPascalCase(microservice.getArtifactId()) + "Application.java",
                template.getContent());
    }

    public Output createController(SpringBootMicroservice microservice, String dataStoreName) {
        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_CONTROLLER);
        templateService.preProcess(microservice, template);
        template.replace("Table", StrUtil.toPascalCase(dataStoreName));
        template.replace("table", StrUtil.toCamelCase(dataStoreName));
        template.replace("primaryKey", "id");
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getNodes().get(0).getName()));

        if (microservice.getOrmType() == OrmType.MYBATIS_ANNOTATION) {
            template.replace("pagition", "PageInfo");
            template.replace("pagitionImport", "com.github.pagehelper.PageInfo");
        } else if (microservice.getOrmType() == OrmType.SPRING_DATA_JPA) {
            template.replace("pagition", "Page");
            template.replace("pagitionImport", "org.springframework.data.domain.Page");
        }

//        for (DataStore.Node node: store.getNodes()) {
//            String filterMode = ""; // node.getFilter();
//            if (StringUtils.isNotBlank(filterMode)) {
//                CompilationUnit unit = template.parseJavaSource();
//                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
//                List<MethodDeclaration> methods = clazz.getMethodsByName("retrievePage");
//                if (!methods.isEmpty()) {
//                    MethodDeclaration method = methods.get(0);
//
//                    // Add parameters
//                    NodeList<Parameter> params = method.getParameters();
//                    Parameter param = new Parameter();
//                    param.setName(node.getName());
//                    param.setType("String");
//                    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
//                    annotation.setName("RequestParam");
//                    annotation.addPair("required", "false");
//                    param.addAnnotation(annotation);
//                    params.add(param);
////                    log.info("Params: {}", params);
//
//                    // Add args of method call
//                    Optional<BlockStmt> body = method.getBody();
//                    if (body.isPresent()) {
//                        Statement statement = body.get().getStatement(0);
//                        List<Node> nodes = statement.getChildNodes();
//                        if (!nodes.isEmpty()) {
//                            VariableDeclarationExpr expr = (VariableDeclarationExpr) nodes.get(0);
//                            Optional<Expression> initializer = expr.getVariable(0).getInitializer();
//                            if (initializer.isPresent()) {
//                                NodeList<Expression> args = initializer.get().asMethodCallExpr().getArguments();
//                                args.add(new NameExpr((node.getName())));
//                            }
//                        }
//                    }
//                }
//                template.updateJavaSource(unit);
//            }
//        }

        return new Output(TemplateType.SPRING_BOOT_CONTROLLER,
                getPackagePath(microservice) + "controller/" + StrUtil.toPascalCase(dataStoreName) + "Controller.java",
                template);
    }

    private String getPackagePath(SpringBootMicroservice microservice) {
        return microservice.getName() + "/src/main/java/" +
                microservice.getGroupId().replace(".", "/") + "/" +
                microservice.getArtifactId().replace(".", "/").replace('-', '/') + "/";
    }
}
