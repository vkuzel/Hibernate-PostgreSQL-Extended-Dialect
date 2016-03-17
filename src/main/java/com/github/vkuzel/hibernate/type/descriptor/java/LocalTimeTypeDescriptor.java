package com.github.vkuzel.hibernate.type.descriptor.java;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeTypeDescriptor extends AbstractTypeDescriptor<LocalTime> {

    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    public static final LocalDateTypeDescriptor INSTANCE = new LocalDateTypeDescriptor();

    protected LocalTimeTypeDescriptor() {
        super(LocalTime.class);
    }

    @Override
    public String toString(LocalTime value) {
        return value.format(TIME_FORMATTER);
    }

    @Override
    public LocalTime fromString(String string) {
        return LocalTime.parse(string, TIME_FORMATTER);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(LocalTime value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        } else if (Time.class.isAssignableFrom(type)) {
            return (X) Time.valueOf(value);
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> LocalTime wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Time.class.isInstance(value)) {
            return ((Time) value).toLocalTime();
        }
        throw unknownWrap(value.getClass());
    }
}
