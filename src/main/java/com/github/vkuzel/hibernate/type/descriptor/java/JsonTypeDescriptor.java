package com.github.vkuzel.hibernate.type.descriptor.java;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class JsonTypeDescriptor extends AbstractTypeDescriptor<Map> {

    public static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    public static final JsonTypeDescriptor INSTANCE = new JsonTypeDescriptor();

    protected JsonTypeDescriptor() {
        super(Map.class);
    }

    @Override
    public String toString(Map value) {
        try {
            return JSON_MAPPER.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Map cannot be parsed to JSON!", e);
        }
    }

    @Override
    public Map fromString(String string) {
        try {
            return JSON_MAPPER.readValue(string, Map.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON cannot be parsed! " + string, e);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <X> X unwrap(Map value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        } else if (PGobject.class.isAssignableFrom(type)) {
            String json = toString(value);
            try {
                PGobject pgObject = new PGobject();
                pgObject.setValue(json);
                pgObject.setType("json");
                return (X) pgObject;
            } catch (SQLException e) {
                throw new IllegalArgumentException("JSON cannot be set to PGObject! " + json, e);
            }
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> Map wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        } else if (PGobject.class.isInstance(value)) {
            String json = ((PGobject) value).getValue();
            return fromString(json);
        }
        throw unknownWrap(value.getClass());
    }
}
