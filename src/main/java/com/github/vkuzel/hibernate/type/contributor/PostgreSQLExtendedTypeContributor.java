package com.github.vkuzel.hibernate.type.contributor;

import com.github.vkuzel.hibernate.type.ArrayType;
import com.github.vkuzel.hibernate.type.JsonType;
import com.github.vkuzel.hibernate.type.descriptor.java.ArrayTypeDescriptor;
import com.github.vkuzel.hibernate.type.descriptor.java.JsonTypeDescriptor;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.java.JavaTypeDescriptorRegistry;

public class PostgreSQLExtendedTypeContributor implements TypeContributor {
    @Override
    public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(ArrayTypeDescriptor.INSTANCE);
        JavaTypeDescriptorRegistry.INSTANCE.addDescriptor(JsonTypeDescriptor.INSTANCE);

        typeContributions.contributeType(ArrayType.INSTANCE);
        typeContributions.contributeType(JsonType.INSTANCE);
    }
}
