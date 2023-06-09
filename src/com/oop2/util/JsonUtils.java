package com.oop2.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {
    private JsonUtils() {}
    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    /**
     * Convert java object to json:
     * example: String historyActionsJson = JsonUtils.pojoToJson(cacSuKienLichSu);
     * @param o
     * @return
     * @param <O>
     */
    public static <O> String pojoToJson(O o) {
        if (o == null) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert json to object
     * Example: JsonUtils.jsonToPojo(suKienLichSu, SuKienModel.class);
     * @param s
     * @param clazz
     * @return
     * @param <O>
     */
    public static <O> O jsonToPojo(String s, final Class<O> clazz) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.readValue(s, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Convert from list/array json
     * example: JsonUtils.jsonToListPojo(historyActionsJson, new TypeReference<List<SuKienModel>>() {});
     * @param s
     * @param reference
     * @return
     * @param <O>
     */
    public static <O> O jsonToListPojo(String s, final TypeReference<O> reference) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return MAPPER.readValue(s, reference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}