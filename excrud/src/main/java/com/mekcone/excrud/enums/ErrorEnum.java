package com.mekcone.excrud.enums;

public interface ErrorEnum {
    // ExCRUD Errors (1 ~ 1000)
    BaseEnum PROJECT_FILE_NOT_FOUND = new BaseEnum(1, "Project file \"excrud.xml\" not found");
    BaseEnum PARSE_XML_FAILED = new BaseEnum(2, "Parse XML failed");
    BaseEnum DATABASE_UNDEFINED = new BaseEnum(3, "Database undefined");
    BaseEnum TABLE_UNDEFINED = new BaseEnum(4, "Table undefined");
    BaseEnum EXCRUD_HOME_ENV_VARIABLE_NOT_SET = new BaseEnum(5, "Environment variable \"EXCRUD_HOME\" not set");
    BaseEnum GENERATE_PROJECT_FAILED = new BaseEnum(999, "Generate project failed");

    // Export spring-boot-project Errors (1001 ~ 2000)
    BaseEnum SBP_DIRECTORIES_TXT_NOT_FOUND = new BaseEnum(1001, "\"Directories.txt\" not found");

    // Export vue-admin-project Errors (2001 ~ 3000)
}
