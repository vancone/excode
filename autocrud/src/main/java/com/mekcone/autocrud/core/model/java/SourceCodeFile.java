package com.mekcone.autocrud.core.model.java;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.mekcone.autocrud.core.controller.Logger;

import java.util.ArrayList;
import java.util.List;

public class SourceCodeFile {
    public String description;
    public String packageName;
    public List<String> importedItems = new ArrayList<>();
    public Bean bean;

    @Override
    public String toString() {
        String code = "";
        if (description != null) {
            code += "// " + description + "\n\n";
        }
        if (packageName != null) {
            code += "package " + packageName + ";\n";
        }
        if (!importedItems.isEmpty()) {
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
            Logger.warn(e.getMessage());
            return code;
        }
    }
}
