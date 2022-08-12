package ru.vk.competition.minbenchmark.utils;

import java.sql.JDBCType;
import java.sql.Types;

public final class TypeSerializer {

    private TypeSerializer() {}

    public static String serialize(int type, int length) {
        JDBCType typeName = JDBCType.valueOf(type);
        switch (type) {
            case Types.BINARY:
            case Types.VARCHAR:
            case Types.CHAR:
                return typeName.getName() + "(" + length + ")";
//                return "varchar(" + length + ")";
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.NUMERIC:
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                return typeName.getName();
        }
        throw new IllegalArgumentException("Unknown type " + type + " with length " + length);
    }

}
