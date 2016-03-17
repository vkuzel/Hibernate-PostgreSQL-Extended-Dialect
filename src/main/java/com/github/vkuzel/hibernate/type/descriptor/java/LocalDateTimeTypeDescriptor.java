package com.github.vkuzel.hibernate.type.descriptor.java;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeDescriptor extends AbstractTypeDescriptor<LocalDateTime> {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final LocalDateTimeTypeDescriptor INSTANCE = new LocalDateTimeTypeDescriptor();

    protected LocalDateTimeTypeDescriptor() {
        super(LocalDateTime.class);
    }

    @Override
    public String toString(LocalDateTime value) {
        return value.format(DATE_TIME_FORMATTER);
    }

    @Override
    public LocalDateTime fromString(String string) {
        return LocalDateTime.parse(string, DATE_TIME_FORMATTER);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(LocalDateTime value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        } else if (Timestamp.class.isAssignableFrom(type)) {
            return (X) Timestamp.valueOf(value);
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> LocalDateTime wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Date.class.isInstance(value)) {
            return ((Timestamp) value).toLocalDateTime();
        }
        throw unknownWrap(value.getClass());
    }
}