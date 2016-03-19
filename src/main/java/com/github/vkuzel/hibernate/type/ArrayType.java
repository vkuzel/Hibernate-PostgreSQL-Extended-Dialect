package com.github.vkuzel.hibernate.type;

import com.github.vkuzel.hibernate.type.descriptor.java.ArrayTypeDescriptor;
import com.github.vkuzel.hibernate.type.descriptor.sql.ArraySqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

import java.util.List;

public class ArrayType extends AbstractSingleColumnStandardBasicType<List> {

    public static final ArrayType INSTANCE = new ArrayType();

    public ArrayType() {
        super(ArraySqlTypeDescriptor.INSTANCE, ArrayTypeDescriptor.INSTANCE);
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String getName() {
        return "array";
    }
}
