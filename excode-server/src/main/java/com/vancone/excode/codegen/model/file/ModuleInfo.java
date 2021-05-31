package com.vancone.excode.codegen.model.file;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    public static class Initial {
        @JsonProperty("dirs")
        private List<String> directories = new ArrayList<>();

        private List<String> extensions = new ArrayList<>();
        private List<String> files = new ArrayList<>();
        private Map<String, String> paths = new HashMap<>();
    }

}
