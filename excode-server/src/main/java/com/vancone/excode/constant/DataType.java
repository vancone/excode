package com.vancone.excode.constant;

/**
 * @author Tenton Lien
 */
public interface DataType {
    // Java basic types
    String JAVA_BYTE = "byte";
    String JAVA_BOOLEAN = "boolean";
    String JAVA_SHORT = "short";
    String JAVA_CHAR = "char";
    String JAVA_INT = "int";
    String JAVA_LONG = "long";
    String JAVA_FLOAT = "float";
    String JAVA_DOUBLE = "double";

    // Java others
    String JAVA_VOID = "void";
    String JAVA_STRING = "String";
    String JAVA_OBJECT = "Object";

    // SQL integer
    String SQL_INT_TINYINT = "TINYINT";
    String SQL_INT_SMALLINT = "SMALLINT";
    String SQL_INT_MEDIUMINT = "MEDIUMINT";
    String SQL_INT_INT = "INT";
    String SQL_INT_BIGINT = "BIGINT";
    String SQL_INT_BIT = "BIT";

    // SQL real
    String SQL_REAL_FLOAT = "FLOAT";
    String SQL_REAL_DOUBLE = "DOUBLE";
    String SQL_REAL_DECIMAL = "DECIMAL";

    // SQL text
    String SQL_TXT_CHAR = "CHAR";
    String SQL_TXT_VARCHAR = "VARCHAR";
    String SQL_TXT_TINYTEXT = "TINYTEXT";
    String SQL_TXT_TEXT = "TEXT";
    String SQL_TXT_MEDIUMTEXT = "MEDIUMTEXT";
    String SQL_TXT_LONGTEXT = "LONGTEXT";
    String SQL_TXT_JSON = "JSON";

    // SQL binary
    String SQL_BIN_BINARY = "BINARY";
    String SQL_BIN_VARBINARY = "VARBINARY";
    String SQL_BIN_TINYBLOB = "TINYBLOB";
    String SQL_BIN_BLOB = "BLOB";
    String SQL_BIN_MEDIUMBLOB = "MEDIUMBLOB";
    String SQL_BIN_LONGBLOB = "LONGBLOB";

    // SQL temporal (time)
    String SQL_TIME_DATE = "DATE";
    String SQL_TIME_TIME = "TIME";
    String SQL_TIME_YEAR = "YEAR";
    String SQL_TIME_DATETIME = "DATETIME";
    String SQL_TIME_TIMESTAMP = "TIMESTAMP";

    // SQL spatial (geometry)
    String SQL_GEO_POINT = "POINT";
    String SQL_GEO_LINESTRING = "LINESTRING";
    String SQL_GEO_POLYGON = "POLYGON";
    String SQL_GEO_GEOMETRY = "GEOMETRY";
    String SQL_GEO_MULTIPOINT = "MULTIPOINT";
    String SQL_GEO_MULTILINESTRING = "MULTILINESTRING";
    String SQL_GEO_MULTIPOLYGON = "MULTIPOLYGON";
    String SQL_GEO_GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";

    // SQL others
    String SQL_OTHER_ENUM = "ENUM";
    String SQL_OTHER_SET = "SET";
}
