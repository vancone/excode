package com.mekcone.excrud.enums;

public enum ErrorEnum {
    // General errors (E000000 ~ E000999)
    UNKNOWN_ERROR("E000000", "Unknown error"),

    // ProjectInitializer errors (E001000 ~ E001999)

    // ProjectLoader errors (E002000 ~ E002999)
    PROJECT_FILE_NOT_FOUND("E002000", "Project file \"$val\" not found"),
    PARSE_XML_FAILED("E002001", "Parse XML failed ($val)"),
    DATABASE_UNDEFINED("E002002", "Database undefined"),
    TABLE_UNDEFINED("E002003", "Table undefined"),
    EXCRUD_HOME_ENV_VARIABLE_NOT_SET("E002004", "Environment variable \"EXCRUD_HOME\" not set"),
    GENERATE_PROJECT_FAILED("E002005", "Generate project failed"),

    // BaseGenerator errors (E003000 ~ E003999)
    INITIAL_DATA_NOT_FOUND("E003000", "Initial data for generators \"Initial.json\"  not found"),
    PATHS_NOT_A_VALID_OBJECT("E003001", "\"paths\" is not a valid object to be parsed"),

    // SpringBootBackendGenerator Errors (E004000 ~ E004999)
    SBP_DIRECTORIES_TXT_NOT_FOUND("E004000", "\"$val\" not found");

    // Export vue-admin-project Errors (2001 ~ 3000)

    private String code;
    private String message;

    ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void insertValue(String value) {
        message = message.replace("$val", value);
    }
}
