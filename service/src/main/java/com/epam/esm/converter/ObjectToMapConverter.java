package com.epam.esm.converter;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.Map;
import java.util.stream.Collectors;

public class ObjectToMapConverter {
    public static Map<String, String> convertToMap(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> params = objectMapper.convertValue(object, Map.class);
        params = params.entrySet().stream().filter(l ->
                l.getValue() != null).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return params;
    }
}
