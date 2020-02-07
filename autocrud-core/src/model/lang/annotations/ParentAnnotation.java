package model.lang.annotations;

import controller.Logger;

import java.util.ArrayList;
import java.util.List;

public class ParentAnnotation extends Annotation {
    private List<Annotation> annotations = new ArrayList<>();

    public void addAnnatation(Annotation annotation) {
        this.annotations.add(annotation);
    }

    @Override
    public String toString() {
        String code = "";
        if (this.name == null || this.name.isEmpty()) {
            Logger.error("Annotation name cannot be empty");
        }
        code += "@" + this.name;
        if (!this.annotations.isEmpty()) {
            code += "({";
            for (int i = 0; i < this.annotations.size(); i ++) {
                code += this.annotations.get(i).toString();
                if (i != this.annotations.size() - 1) {
                    code += ",";
                }
//                code+= "\n";
            }
            code += "})\n";
        }
        return code;
    }
}
