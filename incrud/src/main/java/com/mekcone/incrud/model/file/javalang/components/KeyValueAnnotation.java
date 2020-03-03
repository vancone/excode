package com.mekcone.incrud.model.file.javalang.components;


import com.mekcone.incrud.util.LogUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class KeyValueAnnotation extends Annotation {
    private List<Pair<String, String>> keyValuePairs = new ArrayList<>();

    public void addKeyValue(String key, String value) {
        this.keyValuePairs.add(new Pair<String, String>(key, value));
    }

    @Override
    public String toString() {
        String code = "";
        if (this.name == null || this.name.isEmpty()) {
            LogUtil.error("Annotation name cannot be null");
        }
        code += "@" + this.name;

        if (!this.keyValuePairs.isEmpty()) {
            code += "(";
            for (int i = 0; i < this.keyValuePairs.size(); i ++) {
                Pair keyValuePair = this.keyValuePairs.get(i);
                if (keyValuePair.getValue().equals("true") || keyValuePair.getValue().equals("false")) {
                    code += keyValuePair.getKey() + " = " + keyValuePair.getValue();
                } else {
                    code += keyValuePair.getKey() + " = \"" + keyValuePair.getValue() + "\"";
                }

                if ((i != this.keyValuePairs.size() - 1)) {
                    code += ", ";
                }
            }
            code += ")\n";
        }
        return code;
    }
}
