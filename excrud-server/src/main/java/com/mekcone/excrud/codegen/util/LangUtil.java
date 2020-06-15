package com.mekcone.excrud.codegen.util;

import com.mekcone.excrud.codegen.constant.LanguageType;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.constant.UrlPath;
import com.mekcone.excrud.codegen.controller.parser.PropertiesParser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LangUtil {
    private static List<PropertiesParser> propertiesParsers = new ArrayList<>();

    private static void load() {
        File i18nPath = new File(UrlPath.EXCRUD_HOME +
                "modules" + File.separator + ModuleType.DOCUMENT + File.separator +
                "i18n" + File.separator);
        for (File file: i18nPath.listFiles()) {
            PropertiesParser propertiesParser = PropertiesParser.readFrom(file.getAbsolutePath());
            if (propertiesParser != null) {
                propertiesParsers.add(propertiesParser);
                log.info("Import i18n file complete: {}", file.getPath());
            } else {
                log.error("Import i18n file failed: {}", file.getPath());
            }
        }
    }

    public static String get(String languageType, String key) {
        if (propertiesParsers.isEmpty()) {
            load();
        }

        for (PropertiesParser propertiesParser: propertiesParsers) {
            if (propertiesParser.getName().equals(languageType)) {
                return propertiesParser.get(key);
            }
        }
        return null;
    }

    public static String separator(String languageType) {
        switch (languageType) {
            case LanguageType.JAPANESE:
            case LanguageType.SIMPLIFIED_CHINESE:
            case LanguageType.THAI:
            case LanguageType.TRADITIONAL_CHINESE:
                return "";
        }
        return " ";
    }
}
