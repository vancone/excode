package com.mekcone.excrud.service;

import com.mekcone.excrud.model.project.components.Plugin;
import com.mekcone.excrud.model.project.components.Table;

public interface SpringBootProjectService {
    boolean build();
    void generate();
    boolean run();
    String stringifyApplicationEntry();
    String stringifyApplicationProperties();
    String stringifyConfig(Plugin plugin);
    String stringifyController(Table table);
    String stringifyEntity(Table table);
    String stringifyMybatisMapper(Table table);
    String stringifyPomXml();
    String stringifyService(Table table);
    String stringifyServiceImpl(Table table);
    String stringifyUtil(String utilName);
    String stringifyVO(String VOName);
}
