package com.github.vkuzel.hibernate.dialect;

import com.github.vkuzel.hibernate.type.LocalDateType;
import com.github.vkuzel.hibernate.type.PostgresArrayType;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.dialect.PostgreSQL9Dialect;
import org.hibernate.metamodel.spi.TypeContributions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

import java.sql.Types;

public class PostgreSQL9ExtendedDialect extends PostgreSQL9Dialect {


    public PostgreSQL9ExtendedDialect() {
        super();
        registerHibernateType(Types.ARRAY, PostgresArrayType.INSTANCE.getName());
        registerHibernateType(Types.DATE, LocalDateType.INSTANCE.getName());
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);
        typeContributions.contributeType(PostgresArrayType.INSTANCE);
        typeContributions.contributeType(LocalDateType.INSTANCE);
    }
}
