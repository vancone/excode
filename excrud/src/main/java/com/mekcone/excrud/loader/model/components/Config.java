package com.mekcone.excrud.loader.model.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Config {
    @JacksonXmlProperty(isAttribute = true)
    private String key;

    @JacksonXmlText
    private String singleValue;

    @JacksonXmlElementWrapper(localName = "values")
    @JacksonXmlProperty(localName = "value")
    private List<String> values;

    @JsonIgnore
    public List<String> getConfigItems() {
        List<String> values = new ArrayList<>();
        if (singleValue != null && !singleValue.isEmpty()) {
            values.add(singleValue);
        } else {
            values.addAll(this.values);
        }
        return values;
    }

}