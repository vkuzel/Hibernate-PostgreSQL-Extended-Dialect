package com.github.vkuzel.hibernate.type;

import com.github.vkuzel.hibernate.type.descriptor.java.LocalDateTimeTypeDescriptor;
import com.github.vkuzel.hibernate.type.descriptor.sql.LocalDateTimeSqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

import java.time.LocalDateTime;

public class LocalDateTimeType extends AbstractSingleColumnStandardBasicType<LocalDateTime> {

    public static final LocalDateTimeType INSTANCE = new LocalDateTimeType();

    public LocalDateTimeType() {
        super(LocalDateTimeSqlTypeDescriptor.INSTANCE, LocalDateTimeTypeDescriptor.INSTANCE);
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String getName() {
        return "local_datetime";
    }
}
