package com.vancone.excode.core.old.model.project;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class Internationalization {

    @JacksonXmlProperty(isAttribute = true, localName = "default")
    private String defaultValue;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "word")
    private List<Word> words = new ArrayList<>();

    @Data
    static class Word {
        @JacksonXmlProperty(isAttribute = true)
        private String lang;

        @JacksonXmlProperty(isAttribute = true)
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
