package com.github.vkuzel.hibernate.dialect;

import com.github.vkuzel.hibernate.type.*;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.metamodel.spi.TypeContributions;
import org.hibernate.service.ServiceRegistry;

import java.sql.Types;

public class PostgreSQL9ExtendedDialect extends PostgreSQL9Dialect {

    public PostgreSQL9ExtendedDialect() {
        super();
        registerHibernateType(Types.ARRAY, ArrayType.INSTANCE.getName());
        registerHibernateType(Types.DATE, LocalDateType.INSTANCE.getName());
        registerHibernateType(Types.TIME, LocalTimeType.INSTANCE.getName());
        registerHibernateType(Types.TIMESTAMP, LocalDateTimeType.INSTANCE.getName());
        registerHibernateType(Types.OTHER, JsonType.INSTANCE.getName());
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);
        typeContributions.contributeType(ArrayType.INSTANCE);
        typeContributions.contributeType(LocalDateType.INSTANCE);
        typeContributions.contributeType(LocalTimeType.INSTANCE);
        typeContributions.contributeType(LocalDateTimeType.INSTANCE);
        typeContributions.contributeType(JsonType.INSTANCE);
    }
}
