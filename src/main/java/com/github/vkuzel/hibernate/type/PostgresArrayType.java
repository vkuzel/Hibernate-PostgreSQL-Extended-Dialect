package com.github.vkuzel.hibernate.type;

import com.github.vkuzel.hibernate.type.descriptor.java.PostgresArrayTypeDescriptor;
import com.github.vkuzel.hibernate.type.descriptor.sql.PostgresArraySqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

import java.util.List;

public class PostgresArrayType extends AbstractSingleColumnStandardBasicType<List> {

    public static final PostgresArrayType INSTANCE = new PostgresArrayType();

    public PostgresArrayType() {
        super(PostgresArraySqlTypeDescriptor.INSTANCE, PostgresArrayTypeDescriptor.INSTANCE);
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String getName() {
        return "pg_array";
    }
}
