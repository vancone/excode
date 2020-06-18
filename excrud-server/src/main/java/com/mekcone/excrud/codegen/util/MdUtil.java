package com.mekcone.excrud.codegen.util;

public class MdUtil {
    public static String codeBlock(String content) {
        return "```\n" + content + "\n```";
    }

    public static String heading(String content, int level) {
        StringBuilder contentBuilder = new StringBuilder(" " + content);
        while (level > 0) {
            contentBuilder.insert(0, "#");
            level --;
        }
        content = contentBuilder.toString();
        return content;
    }

    public static String list(String content) {
        return "- " + content;
    }

    public static String underline(String content) {
        return "<u>" + content + "</u>";
    }
}
