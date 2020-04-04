package com.mekcone.excrud.constant;

public interface SqlDataType {
    // Integer
    String TINYINT = "TINYINT";
    String SMALLINT = "SMALLINT";
    String MEDIUMINT = "MEDIUMINT";
    String INT = "INT";
    String BIGINT = "BIGINT";
    String BIT = "BIT";

    // Real
    String FLOAT = "FLOAT";
    String DOUBLE = "DOUBLE";
    String DECIMAL = "DECIMAL";

    // Text
    String CHAR = "CHAR";
    String VARCHAR = "VARCHAR";
    String TINYTEXT = "TINYTEXT";
    String TEXT = "TEXT";
    String MEDIUMTEXT = "MEDIUMTEXT";
    String LONGTEXT = "LONGTEXT";
    String JSON = "JSON";

    // Binary
    String BINARY = "BINARY";
    String VARBINARY = "VARBINARY";
    String TINYBLOB = "TINYBLOB";
    String BLOB = "BLOB";
    String MEDIUMBLOB = "MEDIUMBLOB";
    String LONGBLOB = "LONGBLOB";

    // Temporal (time)
    String DATE = "DATE";
    String TIME = "TIME";
    String YEAR = "YEAR";
    String DATETIME = "DATETIME";
    String TIMESTAMP = "TIMESTAMP";

    // Spatial (geometry)
    String POINT = "POINT";
    String LINESTRING = "LINESTRING";
    String POLYGON = "POLYGON";
    String GEOMETRY = "GEOMETRY";
    String MULTIPOINT = "MULTIPOINT";
    String MULTILINESTRING = "MULTILINESTRING";
    String MULTIPOLYGON = "MULTIPOLYGON";
    String GEOMETRYCOLLECTION = "GEOMETRYCOLLECTION";

    // Other
    String ENUM = "ENUM";
    String SET = "SET";
}
