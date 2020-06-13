package com.mekcone.excrud.codegen.model.module;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class ModuleInfo {

    private String name;

    private Map<String, String> version = new HashMap<>();

    private Initial initial = new Initial();

    @Data
    public class Initial {

        private Map<String, String> paths = new HashMap<>();

        private Map<String, String> components = new HashMap<>();

        private List<String> extensions = new ArrayList<>();
    }

}
