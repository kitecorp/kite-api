/*
 * Copyright (C) 2012-present the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cloud.kitelang.api;

import cloud.kitelang.api.resource.Property;
import cloud.kitelang.api.schema.Schema;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class ResourceHandler<T> implements IResourceHandler<T> {
    private final Map<String, Class<?>> schemaMap = new HashMap<>();
    @Getter
    private final Class<T> resource;

    /**
     * we don't need to store this list in the database for each resource because it's a waste of space.
     * We know from the schema which properties are immutable and here we just store a reverse index on them
     */
    private final Set<String> immutableProperties = new HashSet<>();

    /**
     * Create a ResourceHandler with auto-inferred generic type.
     */
    public ResourceHandler() {
        this.resource = (Class<T>) resolveGenericParameter(getClass());
        schemasMap();
    }

    /**
     * Create a ResourceHandler with an explicit resource class.
     * Use this when the generic type cannot be inferred (e.g., for adapters).
     *
     * @param resourceClass The resource class
     */
    protected ResourceHandler(Class<T> resourceClass) {
        this.resource = resourceClass;
        schemasMap();
    }

    /**
     * Create a ResourceHandler with an explicit resource class, optionally skipping schema initialization.
     * Use this for adapters (like gRPC) where schema comes from external sources.
     *
     * @param resourceClass The resource class
     * @param skipSchemaInit If true, skip schema initialization (for adapters)
     */
    protected ResourceHandler(Class<T> resourceClass, boolean skipSchemaInit) {
        this.resource = resourceClass;
        if (!skipSchemaInit) {
            schemasMap();
        }
    }

    public Schema schema() {
        return Schema.toSchema(resource);
    }

    private Map<String, Class<?>> schemasMap() {
        var definition = schema();
        schemaMap.put(definition.getName(), definition.getResourceClass());
        initImmutables(definition);
        return schemaMap;
    }

    private void initImmutables(Schema definition) {
        for (Property property : definition.getProperties()) {
            if (property.isCloud()) {
                immutableProperties.add(property.name());
            }
        }
    }

    public boolean isImmutable(String name) {
        return immutableProperties.contains(name);
    }

    public Class<?> getSchema(String name) {
        return schemaMap.get(name);
    }

    public String schemasString() {
        return Schema.toString(resource);
    }

    public String schemaName() {
        return Schema.schemaName(resource);
    }


    private Class<?> resolveGenericParameter(Class<?> clazz) {
        while (clazz != null) {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType pType) {
                if (pType.getRawType().equals(ResourceHandler.class)) {
                    Type actualType = pType.getActualTypeArguments()[0];
                    if (actualType instanceof Class<?> c) return c;
                    if (actualType instanceof ParameterizedType pt) return (Class<?>) pt.getRawType();
                }
            }
            clazz = clazz.getSuperclass();
        }
        throw new IllegalStateException("Could not resolve generic parameter for Provider<T>");
    }
}
