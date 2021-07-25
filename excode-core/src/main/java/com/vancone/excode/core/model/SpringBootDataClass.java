package com.vancone.excode.core.model;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import com.vancone.excode.core.constant.DataType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class SpringBootDataClass {
    private CompilationUnit compilationUnit;
    private ClassOrInterfaceDeclaration entityClassDeclaration;
    private MysqlDataSource.Table table;
    private String name;
    private List<MemberVariable> memberVariables = new ArrayList<>();
    private boolean getterAndSetterAvailable = true;

    @Data
    class MemberVariable {
        String type;
        String name;
    }

    public SpringBootDataClass(Project project, MysqlDataSource.Table table) {
        this.table = table;
        name = table.getUpperCamelCaseName();

        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();

        compilationUnit = new CompilationUnit();
        compilationUnit.setPackageDeclaration(groupId + "." + artifactId + "." + "entity");
        entityClassDeclaration =
                compilationUnit.addClass(table.getUpperCamelCaseName(), Modifier.Keyword.PUBLIC);

        for (MysqlDataSource.Table.Column column : table.getColumns()) {
            String type = column.getType();
            if (!type.equals(DataType.JAVA_INT)) {
                type = DataType.JAVA_STRING;
            }
            entityClassDeclaration.addField(type, column.getCamelCaseName(table.getName()), Modifier.Keyword.PRIVATE);
        }
    }

    private void addGetterAndSetter() {
        for (MysqlDataSource.Table.Column column : table.getColumns()) {
            // Getter
            MethodDeclaration getterMethodDeclaration =
                    entityClassDeclaration.addMethod("get" + column.getUpperCamelCaseName(table.getName()), Modifier.Keyword.PUBLIC);
            getterMethodDeclaration.setType(DataType.JAVA_STRING);
            BlockStmt getterMethodBody = new BlockStmt();
            getterMethodBody.addAndGetStatement(new ReturnStmt(column.getCamelCaseName(table.getName())));
            getterMethodDeclaration.setBody(getterMethodBody);

            // Setter
            MethodDeclaration setterMethodDeclaration =
                    entityClassDeclaration.addMethod("set" + column.getUpperCamelCaseName(table.getName()), Modifier.Keyword.PUBLIC);
            setterMethodDeclaration.setType(DataType.JAVA_VOID);
            setterMethodDeclaration.addParameter(DataType.JAVA_STRING, column.getCamelCaseName(table.getName()));
            BlockStmt setterMethodBody = new BlockStmt();
            AssignExpr assignExpr = new AssignExpr();
            assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
            assignExpr.setTarget(new FieldAccessExpr(new NameExpr("this"), column.getCamelCaseName(table.getName())));
            assignExpr.setValue(new NameExpr(column.getCamelCaseName(table.getName())));
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