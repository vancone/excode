package com.vancone.excode.generator.util;

import com.vancone.excode.generator.entity.Output;
import com.vancone.excode.generator.entity.Template;
import com.vancone.excode.generator.enums.TemplateType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/03/09
 */
public class OutputUtil {
    public static void addOutput(List<Output> outputs, TemplateType type, String target, String content) {
        outputs.add(new Output(type, target, content));
    }

    public static void addOutput(List<Output> outputs, TemplateType type, String target, Template template) {
        outputs.add(new Output(type, target, template));
    }

    public static List<Output> getOutputsByType(List<Output> outputs, TemplateType type) {
        List<Output> results = new ArrayList<>();
        for (Output output: outputs) {
            if (output.getType().equals(type)) {
                results.add(output);
            }
        }
        return results;
    }

    public static Output getOutputByName(List<Output> outputs, String name) {
        for (Output output: outputs) {
            // This operation is not accurate enough
            if (output.getPath().contains(name)) {
                return output;
            }
        }
        return null;
    }
}
