package com.github.vkuzel.hibernate.type;

import com.github.vkuzel.hibernate.type.descriptor.java.PostgresArrayTypeDescriptor;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostgresArrayTypeTest {

    @Test
    public void testUnwrapArray() {
        PostgresArrayTypeDescriptorMock descriptor = new PostgresArrayTypeDescriptorMock();

        Object array1 = descriptor.unwrap(null, Collections.emptyList());
        Assert.assertTrue(PostgresArrayTypeDescriptor.PgUnknownTypeArray.class.isInstance(array1));
        Assert.assertEquals("{}", array1.toString());

        Object array2 = descriptor.unwrap(null, listOf(Collections.emptyList(), Collections.emptyList()));
        Assert.assertTrue(PostgresArrayTypeDescriptor.PgUnknownTypeArray.class.isInstance(array2));
        Assert.assertEquals("{}", array2.toString());

        Object array3 = descriptor.unwrap(null, listOf(null, null));
        Assert.assertTrue(PostgresArrayTypeDescriptor.PgUnknownTypeArray.class.isInstance(array3));
        Assert.assertEquals("{null,null}", array3.toString());

        ArrayDescription array4 = (ArrayDescription) descriptor.unwrap(null, listOf(1, 2, null));
        Assert.assertEquals("integer", array4.getTypeName());
        Assert.assertTrue(Object[].class.isInstance(array4.getArray()));
        Assert.assertEquals(1, array4.getArray()[0]);
        Assert.assertEquals(2, array4.getArray()[1]);
        Assert.assertEquals(null, array4.getArray()[2]);

        ArrayDescription array5 = (ArrayDescription) descriptor.unwrap(null, listOf(listOf(1, 2), listOf(3, 4)));
        Assert.assertEquals("integer", array5.getTypeName());
        Assert.assertTrue(Object[].class.isInstance(array5.getArray()));
        Assert.assertTrue(Object[].class.isInstance(array5.getArray()[0]));
        Assert.assertTrue(Object[].class.isInstance(array5.getArray()[1]));
        Assert.assertEquals(1, ((Object[]) array5.getArray()[0])[0]);
        Assert.assertEquals(2, ((Object[]) array5.getArray()[0])[1]);
        Assert.assertEquals(3, ((Object[]) array5.getArray()[1])[0]);
        Assert.assertEquals(4, ((Object[]) array5.getArray()[1])[1]);

        ArrayDescription array6 = (ArrayDescription) descriptor.unwrap(null, listOf("Lorem", "Ipsum"));
        Assert.assertEquals("varchar", array6.getTypeName());
    }

    @Test
    public void testWrapArray() throws SQLException {
        PostgresArrayTypeDescriptorMock descriptor = new PostgresArrayTypeDescriptorMock();

        Array array1 = mock(Array.class);
        when(array1.getArray()).thenReturn(new Object[]{});
        List list1 = descriptor.wrap(array1, null);
        Assert.assertEquals(0, list1.size());

        Array array2 = mock(Array.class);
        when(array2.getArray()).thenReturn(new Object[]{null, null});
        List list2 = descriptor.wrap(array2, null);
        Assert.assertEquals(2, list2.size());
        Assert.assertNull(list2.get(0));
        Assert.assertNull(list2.get(1));

        Array array3 = mock(Array.class);
        when(array3.getArray()).thenReturn(new Integer[]{1, 2, null});
        List list3 = descriptor.wrap(array3, null);
        Assert.assertEquals(3, list3.size());
        Assert.assertTrue(Integer.class.isInstance(list3.get(0)));
        Assert.assertEquals(1, list3.get(0));
        Assert.assertEquals(2, list3.get(1));
        Assert.assertEquals(null, list3.get(2));

        Array array4 = mock(Array.class);
        when(array4.getArray()).thenReturn(new Integer[][]{{1, 2}, {3, 4}});
        List list4 = descriptor.wrap(array4, null);
        Assert.assertEquals(2, list4.size());
        Assert.assertEquals(2, ((List) list4.get(0)).size());
        Assert.assertTrue(Integer.class.isInstance(((List) list4.get(0)).get(0)));
        Assert.assertEquals(1, ((List) list4.get(0)).get(0));
        Assert.assertEquals(2, ((List) list4.get(0)).get(1));
        Assert.assertEquals(2, ((List) list4.get(1)).size());
        Assert.assertEquals(3, ((List) list4.get(1)).get(0));
        Assert.assertEquals(4, ((List) list4.get(1)).get(1));

        Array array5 = mock(Array.class);
        when(array5.getArray()).thenReturn(new String[]{"Lorem", "Ipsum"});
        List list5 = descriptor.wrap(array5, null);
        Assert.assertTrue(String.class.isInstance(list5.get(0)));
    }

    @SafeVarargs
    private static <E> List<E> listOf(E... elements) {
        List<E> list = new ArrayList<>(elements.length);
        Collections.addAll(list, elements);
        return list;
    }

    private static class PostgresArrayTypeDescriptorMock extends PostgresArrayTypeDescriptor {
        @Override
        protected Object createArrayOf(Connection connection, String typeName, Object[] elements) throws SQLException {
            return new ArrayDescription(typeName, elements);
        }
    }

    private static class ArrayDescription {
        private final String typeName;
        private final Object[] array;

        public ArrayDescription(String typeName, Object[] array) {
            this.typeName = typeName;
            this.array = array;
        }

        public String getTypeName() {
            return typeName;
        }

        public Object[] getArray() {
            return array;
        }
    }
}
