package com.github.vkuzel.hibernate.type;

import com.github.vkuzel.hibernate.type.descriptor.java.JsonTypeDescriptor;
import com.github.vkuzel.hibernate.type.descriptor.sql.JsonSqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

import java.util.Map;

public class JsonType extends AbstractSingleColumnStandardBasicType<Map> {

    public static final JsonType INSTANCE = new JsonType();

    public JsonType() {
        super(JsonSqlTypeDescriptor.INSTANCE, JsonTypeDescriptor.INSTANCE);
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String getName() {
        return "json";
    }
}
