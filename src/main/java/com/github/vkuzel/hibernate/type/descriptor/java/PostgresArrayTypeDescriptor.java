package com.github.vkuzel.hibernate.type.descriptor.java;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PostgresArrayTypeDescriptor extends AbstractTypeDescriptor<List> {

    public static final PostgresArrayTypeDescriptor INSTANCE = new PostgresArrayTypeDescriptor();

    public PostgresArrayTypeDescriptor() {
        super(List.class);
    }

    @Override
    public String toString(List value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List fromString(String collection) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X> X unwrap(List value, Class<X> type, WrapperOptions options) {
        throw new UnsupportedOperationException();
    }

    public Object unwrap(Connection connection, List list) {
        return createPgArray(connection, list);
    }

    private Object createPgArray(Connection connection, List list) {
        String elementType = findElementType(list);
        if (elementType == null) {
            // Because generic types are not available at runtime I need to
            // detect list's type from it's elements. This is not possible if
            // list is empty or full of nulls. So for a list with unknown type
            // I create an object of following PgUnknownTypeArray which is
            // will be passed to Postgres with UNSPECIFIED type OID. Postgre's
            // implicit type conversion will take care of it.
            return new PgUnknownTypeArray(list);
        }

        try {
            return createArrayOf(connection, elementType, toArray(list));
        } catch (SQLException e) {
            throw new IllegalArgumentException("List cannot be converted to Postgres array!", e);
        }
    }

    protected Object createArrayOf(Connection connection, String typeName, Object[] elements) throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    private Object[] toArray(List list) {
        if (List.class.isInstance(list.get(0))) {
            List<Object> listOfArrays = new ArrayList<>(list.size());
            for (Object element : list) {
                listOfArrays.add(toArray((List) element));
            }
            return listOfArrays.toArray();
        } else {
            return list.toArray();
        }
    }

    private String findElementType(List list) {
        for (Object element : list) {
            if (element == null) {
            } else if (Short.class.isInstance(element)) {
                return "smallint";
            } else if (Integer.class.isInstance(element)) {
                return "integer";
            } else if (Long.class.isInstance(element)) {
                return "bigint";
            } else if (Float.class.isInstance(element)) {
                return "real";
            } else if (Double.class.isInstance(element)) {
                return "double precision";
            } else if (BigDecimal.class.isInstance(element)) {
                return "decimal";
            } else if (String.class.isInstance(element)) {
                return "varchar";
            } else if (Boolean.class.isInstance(element)) {
                return "boolean";
            } else if (Date.class.isInstance(element)) {
                return "date";
            } else if (Time.class.isInstance(element)) {
                return "time";
            } else if (Timestamp.class.isInstance(element)) {
                return "timestamp";
            } else if (List.class.isInstance(element)) {
                String elementType = findElementType((List) element);
                if (elementType != null) {
                    return elementType;
                }
            } else {
                throw new IllegalArgumentException("Unknown type " + element.getClass().getName());
            }
        }
        return null;
    }

    @Override
    public <X> List wrap(X value, WrapperOptions options) {
        // TODO Move ifnull here.
        if (Array.class.isInstance(value)) {
            Array sqlArray = (Array) value;
            try {
                Object[] array = (Object[]) sqlArray.getArray();
                return toList(array);
            } catch (SQLException e) {
                throw new IllegalArgumentException("Postgres array cannot be converted to List!", e);
            }
        }

        throw unknownWrap(value.getClass());
    }

    private List toList(Object[] array) {
        List<Object> list = Arrays.asList(array);
        if (!list.isEmpty() && Object[].class.isInstance(list.get(0))) {
            list = list.stream().map(e -> toList((Object[]) e)).collect(Collectors.toList());
        }
        return list;
    }

    public static class PgUnknownTypeArray {

        private final List list;

        public PgUnknownTypeArray(List list) {
            this.list = list;
        }

        @Override
        public String toString() {
            return toString(list);
        }

        private String toString(List list) {
            StringBuilder builder = new StringBuilder();
            builder.append('{');
            if (!list.isEmpty()) {
                // All elements of unknown type array has to be the same so there
                // is no need to check other elements than first one.
                Object firstElement = list.get(0);
                if (firstElement == null) {
                    builder.append("null");
                    for (int i = 1, len = list.size(); i < len; i++) {
                        builder.append(",null");
                    }
                } else if (List.class.isInstance(firstElement)) {
                    String subList = toString((List) firstElement);
                    if (!subList.equals("{}")) {
                        builder.append(subList);
                        for (int i = 1, len = list.size(); i < len; i++) {
                            builder.append(',');
                            builder.append(subList);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Unknown type " + firstElement.getClass().getName() + " in partially buit list " + builder.toString());
                }
            }
            builder.append('}');
            return builder.toString();
        }
    }
}
