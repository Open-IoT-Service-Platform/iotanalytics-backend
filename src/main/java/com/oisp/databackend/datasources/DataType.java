package com.oisp.databackend.datasources;


import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.stream.Collectors;

public final class DataType {
    public enum Types {
        Boolean,
        Number,
        String,
        ByteArray
    }

    private static Map<Types, String> typeString = ImmutableMap.of(
            Types.Boolean, "Boolean",
            Types.Number, "Number",
            Types.String, "String",
            Types.ByteArray, "ByteArray"
    );

    private DataType() {

    }

    public static Map<Types, String> getTypeString() {
        return typeString;
    }

    public static void setTypeString(Map<Types, String> typeString) {
        DataType.typeString = typeString;
    }

    public static List<Types> getUncoveredDataTypes(List<Types> dataTypes) {
        Map<Types, Boolean> uncovered = new HashMap<>();
        uncovered.put(Types.Boolean, true);
        uncovered.put(Types.Number, true);
        uncovered.put(Types.String, true);
        uncovered.put(Types.ByteArray, true);

        dataTypes.forEach(type ->
            {
                uncovered.put(type, false);
            });
        return uncovered.entrySet()
                .stream()
                .filter(entry -> entry.getValue())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static String getTypeString(Types type) {
        return typeString.get(type);
    }

    public static List<String> getTypesStringList(List<Types> types) {
        return types.stream()
                .map(type -> getTypeString(type))
                .collect(Collectors.toList());
    }

    public static Types getType(String type) {
        return typeString.entrySet()
                .stream()
                .filter(entry -> type.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList()).get(0);
    }
    public static List<Types> getAllTypes() {
        return Arrays.asList(Types.values());
    }
}
