package com.mekcone.autocrud.core.model.java.annotations;


import com.mekcone.autocrud.core.model.project.Value;
import com.mekcone.autocrud.core.controller.Logger;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class KeyValueAnnotation extends Annotation {
    private List<Pair<String, Value>> keyValuePairs = new ArrayList<>();

    public void addKeyValue(String key, String value) {
        this.keyValuePairs.add(new Pair<String, Value>(key, Value.String(value)));
    }

    public void addKeyValue(String key, Value value) {
        this.keyValuePairs.add(new Pair<String, Value>(key, value));
    }

    @Override
    public String toString() {
        String code = "";
        if (this.name == null || this.name.isEmpty()) {
            Logger.error("Annotation name cannot be null");
        }
        code += "@" + this.name;

        if (!this.keyValuePairs.isEmpty()) {
            code += "(";
            for (int i = 0; i < this.keyValuePairs.size(); i ++) {
                code += this.keyValuePairs.get(i).getKey() + " = " + this.keyValuePairs.get(i).getValue();
                if ((i != this.keyValuePairs.size() - 1)) {
                    code += ", ";
                }
            }
            code += ")\n";
        }
        return code;
    }
}
