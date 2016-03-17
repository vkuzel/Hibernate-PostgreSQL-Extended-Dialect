package com.github.vkuzel.hibernate.type;

import com.github.vkuzel.hibernate.type.descriptor.java.LocalTimeTypeDescriptor;
import com.github.vkuzel.hibernate.type.descriptor.sql.LocalTimeSqlTypeDescriptor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

import java.time.LocalDate;

public class LocalTimeType extends AbstractSingleColumnStandardBasicType<LocalDate> {

    public static final LocalTimeType INSTANCE = new LocalTimeType();

    public LocalTimeType() {
        super(LocalTimeSqlTypeDescriptor.INSTANCE, LocalTimeTypeDescriptor.INSTANCE);
    }

    @Override
    protected boolean registerUnderJavaType() {
        return true;
    }

    @Override
    public String getName() {
        return "local_time";
    }
}
