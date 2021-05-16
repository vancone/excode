package com.vancone.devops.codegen.model.project;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Internationalization {

    private String defaultValue;

    private List<Word> words = new ArrayList<>();

    @Data
    static class Word {
        private String lang;

        private String value;
    }

    public String getValue(String languageType) {
        for (Word word: words) {
            if (word.getLang().equals(languageType)) {
                return word.getValue();
            }
        }
        return defaultValue;
    }
}
