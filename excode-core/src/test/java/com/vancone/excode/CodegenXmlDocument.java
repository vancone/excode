package com.vancone.excode;

import com.vancone.excode.core.old.annotation.ExtensionClass;
import com.vancone.excode.core.old.annotation.Validator;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.FileUtil;
import com.vancone.excode.core.util.MdUtil;
import com.vancone.excode.core.util.StrUtil;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class CodegenXmlDocument {

    @Test
    public void generate() {
        String content = MdUtil.heading("The ExCRUD Dev-Ops Platform", 1);
        Class moduleSetClass = ProjectOld.ModuleSet.class;
        int firstLevelNumber = 1, secondLevelNumber = 1, thirdLevelNumber = 1, fourthLevelNumber = 1;
        content += MdUtil.heading(firstLevelNumber + ". Modules", 2);
        for (Field field: moduleSetClass.getDeclaredFields()) {
            String fieldName = field.getName();
            if (fieldName.contains("Module")) {
                fieldName = fieldName.substring(0, fieldName.length() - "Module".length());
                content += MdUtil.heading((firstLevelNumber + "." + secondLevelNumber + ". ") + StrUtil.kebabCase(fieldName), 3);
                content += MdUtil.heading(firstLevelNumber + "." + secondLevelNumber + "." + thirdLevelNumber + ". Extensions", 4);

                // Extensions
                Class moduleClass = field.getType();
                for (Field moduleClassField: moduleClass.getDeclaredFields()) {
                    if (moduleClassField.getName().equals("properties")) {
                        Class propertiesClass = moduleClassField.getType();
                        for (Class propertiesInnerClass: propertiesClass.getDeclaredClasses()) {
                            if (propertiesInnerClass.getDeclaredAnnotation(ExtensionClass.class) != null) {
                                content += MdUtil.heading(firstLevelNumber + "." + secondLevelNumber + "." + thirdLevelNumber + "." + (fourthLevelNumber ++) + ". " + propertiesInnerClass.getSimpleName(), 5);
                                content += "Elements\n";
                                MdUtil.Table mdTable = new MdUtil.ComplexTable();
                                mdTable.setHeaderFields(new String[]{"Element", "Required", "Default Value", "Values", "Description"});
                                for (Field propertiesInnerClassField: propertiesInnerClass.getDeclaredFields()) {
                                    String defaultValue = "", values = "", required = "";
                                    Validator validatorAnnotation = propertiesInnerClassField.getAnnotation(Validator.class);
                                    if (validatorAnnotation != null) {
                                        required = validatorAnnotation.required() ? "true" : "";
                                        defaultValue = validatorAnnotation.defaultValue();
                                        for (int i = 0; i < validatorAnnotation.value().length; i ++) {
                                            values += validatorAnnotation.value()[i];
                                            if (i + 1 != validatorAnnotation.value().length) {
                                                values += ", ";
                                            }
                                        }
                                    }
                                    mdTable.addRow(new String[]{propertiesInnerClassField.getName(), required, defaultValue, values, " "});
                                }
                                content += mdTable;
                            }
                        }
                        break;
                    }
                }

                secondLevelNumber ++;

                content += MdUtil.lineBreak();
            }

        }
        FileUtil.write("C:\\Users\\Tenton\\Desktop\\excrud-doc.md", content);
        System.out.println(content);
    }
}
