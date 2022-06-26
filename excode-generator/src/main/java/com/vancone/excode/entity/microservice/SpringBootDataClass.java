package com.vancone.excode.entity.microservice;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.vancone.excode.constant.DataType;
import com.vancone.excode.entity.DataStoreOld;
import com.vancone.excode.entity.ProjectOld;
import com.vancone.excode.util.StrUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class SpringBootDataClass {
    private CompilationUnit compilationUnit;
    private ClassOrInterfaceDeclaration entityClassDeclaration;
    private DataStoreOld store;
    private String name;
    private List<MemberVariable> memberVariables = new ArrayList<>();
    private boolean getterAndSetterAvailable = true;

    @Data
    class MemberVariable {
        String type;
        String name;
    }

    public SpringBootDataClass(ProjectOld.DataAccess.Solution.JavaSpringBoot module, DataStoreOld store) {
        this.store = store;
        name = StrUtil.toPascalCase(store.getName());

        String groupId = StringUtils.isBlank(module.getGroupId())? "com.vancone": module.getGroupId();
        String artifactId = StringUtils.isBlank(module.getArtifactId())? "excode-project": module.getArtifactId();

        compilationUnit = new CompilationUnit();
        compilationUnit.setPackageDeclaration(groupId + "." + artifactId.replace('-', '.') + "." + "entity");
        entityClassDeclaration =
                compilationUnit.addClass(StrUtil.toPascalCase(store.getName()), Modifier.Keyword.PUBLIC);

        for (DataStoreOld.Node node: store.getNodes()) {
            String type = node.getType();
            if (!type.equals(DataType.JAVA_INT)) {
                type = DataType.JAVA_STRING;
            }
            entityClassDeclaration.addField(type, node.getCamelCaseName(store.getName()), Modifier.Keyword.PRIVATE);
        }
    }

    private void addGetterAndSetter() {
        for (DataStoreOld.Node node: store.getNodes()) {
            // Getter
            MethodDeclaration getterMethodDeclaration =
                    entityClassDeclaration.addMethod("get" + node.getUpperCamelCaseName(store.getName()), Modifier.Keyword.PUBLIC);
            getterMethodDeclaration.setType(DataType.JAVA_STRING);
            BlockStmt getterMethodBody = new BlockStmt();
            getterMethodBody.addAndGetStatement(new ReturnStmt(node.getCamelCaseName(store.getName())));
            getterMethodDeclaration.setBody(getterMethodBody);

            // Setter
            MethodDeclaration setterMethodDeclaration =
                    entityClassDeclaration.addMethod("set" + node.getUpperCamelCaseName(store.getName()), Modifier.Keyword.PUBLIC);
            setterMethodDeclaration.setType(DataType.JAVA_VOID);
            setterMethodDeclaration.addParameter(DataType.JAVA_STRING, node.getCamelCaseName(store.getName()));
            BlockStmt setterMethodBody = new BlockStmt();
            AssignExpr assignExpr = new AssignExpr();
            assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
            assignExpr.setTarget(new FieldAccessExpr(new NameExpr("this"), node.getCamelCaseName(store.getName())));
            assignExpr.setValue(new NameExpr(node.getCamelCaseName(store.getName())));
            setterMethodBody.addAndGetStatement(assignExpr);
            setterMethodDeclaration.setBody(setterMethodBody);
        }
    }

    @Override
    public String toString() {
        if (getterAndSetterAvailable) {
            addGetterAndSetter();
        }
        return compilationUnit.toString();
    }
}
