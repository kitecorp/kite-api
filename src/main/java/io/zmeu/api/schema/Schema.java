package io.zmeu.api.schema;

import io.zmeu.api.annotations.TypeName;
import io.zmeu.api.resource.Property;
import lombok.Builder;
import lombok.Data;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Class represeting a schema {...} definition
 */
@Data
@Builder
public class Schema {
    private String name;

    private String description;

    private String uri;

    private String version;

    private Class resourceClass;

    private Set<Property> properties;

    public static String toString(Object resource) {
        var fields = resource.getClass().getDeclaredFields();
        var properties = new StringBuilder(" { \n");
        for (Field field : fields) {
            var property = field.getAnnotation(io.zmeu.api.annotations.Property.class);
            var name = property.name().isBlank() ? field.getName() : property.name();
            if (property.immutable()) {
                properties.append("\tval ");
            } else {
                properties.append("\tvar ");
            }
            var typename = field.getType().getSimpleName().toLowerCase();
            properties.append(typename);
            properties.append(" ");
            properties.append(name);
            properties.append("\n");
        }
        properties.append("} \n");

        var annotation = resource.getClass().getAnnotation(io.zmeu.api.annotations.TypeName.class);
        return "schema %s%s".formatted(annotation.value(), properties);
    }

    @SneakyThrows
    public static Schema toSchema(Object resource) {
        Objects.requireNonNull(resource);
        var builder = Schema.builder();
        var schemaDefinition = resource.getClass().getAnnotation(io.zmeu.api.annotations.TypeName.class);
        if (schemaDefinition == null) {
            throw new RuntimeException("@SchemaDefinition annotation not found on class: "+resource.getClass().getName());
        }

        builder.name(schemaDefinition.value());
        builder.properties(new LinkedHashSet<>());
        builder.resourceClass(resource.getClass());

        var fields = resource.getClass().getDeclaredFields();
        for (Field field : fields) {
            var propertySchema = field.getAnnotation(io.zmeu.api.annotations.Property.class);
            var property = Property.builder();

            property.required(propertySchema.optional());
            property.type(field.getType().getSimpleName().toLowerCase());


            property.immutable(propertySchema.immutable());
            property.description(propertySchema.description());

            String name = propertySchema.name().isBlank() ? field.getName() : propertySchema.name();
            property.name(name);

            property.hidden(propertySchema.hidden());

            builder.properties.add(property.build());
        }

        return builder.build();
    }

    public static String schemaName(Object resource) {
        Class<?> aClass = resource.getClass();
        if (aClass.isAnnotationPresent(TypeName.class)) return aClass.getAnnotation(TypeName.class).value();
        else return aClass.getSimpleName();
    }

}
