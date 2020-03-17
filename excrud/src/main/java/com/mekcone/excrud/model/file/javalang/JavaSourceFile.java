package com.mekcone.excrud.model.file.javalang;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.mekcone.excrud.model.file.File;
import com.mekcone.excrud.model.file.javalang.components.Bean;
import com.mekcone.excrud.util.LogUtil;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@Component
public class JavaSourceFile extends File {
    private String packageName;
    private List<String> importedItems = new ArrayList<>();
    private Bean bean;

    public void addImportedItem(String item) {
        this.importedItems.add(item);
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
