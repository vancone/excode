package com.mekcone.excrud.model.file.javalang;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.mekcone.excrud.model.file.FileModel;
import com.mekcone.excrud.model.file.javalang.components.Bean;
import com.mekcone.excrud.util.LogUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JavaSourceFileModel extends FileModel {
    private String packageName;
    private List<String> importedItems = new ArrayList<>();
    private Bean bean;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getImportedItems() {
        return importedItems;
    }

    public void setImportedItems(List<String> importedItems) {
        this.importedItems = importedItems;
    }

    public void addImportedItem(String item) {
        this.importedItems.add(item);
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }


    @Override
    public String toString() {
        String code = "";
        code += "// " + getDescription() + "\n\n";

        if (packageName != null) {
            code += "package " + packageName + ";\n";
        }
        if (!importedItems.isEmpty()) {
            Collections.sort(importedItems);
            for (String importedItem: importedItems) {
                code += "import " + importedItem + ";";
            }
            code += "\n";
        }
        if (bean != null) {
            code += "\n" + bean.toString();
        }

        try {
            String formattedSource = new Formatter().formatSource(code);
            return formattedSource;
        } catch(FormatterException e) {
            LogUtil.warn(e.getMessage());
            return code;
        }
    }
}
