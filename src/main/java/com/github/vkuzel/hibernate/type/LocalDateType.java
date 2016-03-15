package com.github.vkuzel.hibernate.type;

import com.github.vkuzel.hibernate.type.descriptor.java.LocalDateTypeDescriptor;
import com.github.vkuzel.hibernate.type.descriptor.sql.LocalDateSqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

import java.time.LocalDate;

public class LocalDateType extends AbstractSingleColumnStandardBasicType<LocalDate> {

    public static final LocalDateType INSTANCE = new LocalDateType();

    public LocalDateType() {
        super(LocalDateSqlTypeDescriptor.INSTANCE, LocalDateTypeDescriptor.INSTANCE);
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String getName() {
        return "local_date";
    }
}
