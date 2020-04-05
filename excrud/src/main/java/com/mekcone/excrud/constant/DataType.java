package com.mekcone.excrud.constant;

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
    String SQL_TINYINT = "TINYINT";
    String SQL_SMALLINT = "SMALLINT";
    String SQL_MEDIUMINT = "MEDIUMINT";
    String SQL_INT = "INT";
    String SQL_BIGINT = "BIGINT";
    String SQL_BIT = "BIT";

    // SQL real
    String SQL_FLOAT = "FLOAT";
    String SQL_DOUBLE = "DOUBLE";
    String SQL_DECIMAL = "DECIMAL";

    // SQL text
    String SQL_CHAR = "CHAR";
    String SQL_VARCHAR = "VARCHAR";
    String SQL_TINYTEXT = "TINYTEXT";
    String SQL_TEXT = "TEXT";
    String SQL_MEDIUMTEXT = "MEDIUMTEXT";
    String SQL_LONGTEXT = "LONGTEXT";
    String SQL_JSON = "JSON";

    // SQL binary
    String SQL_BINARY = "BINARY";
    String SQL_VARBINARY = "VARBINARY";
    String SQL_TINYBLOB = "TINYBLOB";
    String SQL_BLOB = "BLOB";
    String SQL_MEDIUMBLOB = "MEDIUMBLOB";
    String SQL_LONGBLOB = "LONGBLOB";

    // SQL temporal (time)
    String SQL_DATE = "DATE";
    String SQL_TIME = "TIME";
    String SQL_YEAR = "YEAR";
    String SQL_DATETIME = "DATETIME";
    String SQL_TIMESTAMP = "TIMESTAMP";

    // SQL spatial (geometry)
    String SQL_POINT = "POINT";
    String SQL_LINESTRING = "LINESTRING";
    String SQL_POLYGON = "POLYGON";
    String SQL_GEOMETRY = "GEOMETRY";
    String SQL_MULTIPOINT = "MULTIPOINT";
    String SQL_MULTILINESTRING = "MULTILINESTRING";
    String SQL_MULTIPOLYGON = "MULTIPOLYGON";
    String SQL_GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";

    // SQL others
    String SQL_ENUM = "ENUM";
    String SQL_SET = "SET";
}
