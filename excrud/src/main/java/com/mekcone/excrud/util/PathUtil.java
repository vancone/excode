package com.mekcone.excrud.util;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PathUtil {
    private static List<Pair> pathList = new ArrayList<>();

    public static String getPath(String key) {
        for (Pair path: pathList) {
            if(path.getKey().equals(key)) {
                return path.getValue().toString();
            }
        }
        return null;
    }


    public static void addPath(String key, String value) {
        for (Pair path: pathList) {
            if(path.getKey().equals(key)) {
                pathList.remove(path);
                break;
            }
        }
        pathList.add(new Pair(key, value));
    }

    public static String getProjectModelPath() {
        return getPath("PROJECT_MODEL_PATH");
    }

    public static String getProgramPath() {
        return getPath("PROGRAM_PATH");
    }
}
