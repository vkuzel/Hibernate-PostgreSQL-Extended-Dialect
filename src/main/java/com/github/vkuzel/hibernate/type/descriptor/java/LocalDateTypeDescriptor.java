package com.github.vkuzel.hibernate.type.descriptor.java;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateTypeDescriptor extends AbstractTypeDescriptor<LocalDate> {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final LocalDateTypeDescriptor INSTANCE = new LocalDateTypeDescriptor();

    protected LocalDateTypeDescriptor() {
        super(LocalDate.class);
    }

    @Override
    public String toString(LocalDate value) {
        return value.format(DATE_FORMATTER);
    }

    @Override
    public LocalDate fromString(String string) {
        return LocalDate.parse(string, DATE_FORMATTER);
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public <X> X unwrap(LocalDate value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        } else if (Date.class.isAssignableFrom(type)) {
            return (X) Date.valueOf(value);
        }
        throw unknownUnwrap(type);
    }

    @Override
    public <X> LocalDate wrap(X value, WrapperOptions options) {
        if (value == null) {
            return null;
        }
        if (Date.class.isInstance(value)) {
            return ((Date) value).toLocalDate();
        }
        throw unknownWrap(value.getClass());
    }
}
