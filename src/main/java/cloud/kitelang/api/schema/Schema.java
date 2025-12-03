package cloud.kitelang.api.schema;

import cloud.kitelang.api.annotations.TypeName;
import cloud.kitelang.api.resource.Property;
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

    public static String toString(Class<?> resource) {
        var fields = resource.getDeclaredFields();
        var properties = new StringBuilder(" {\n");
        for (Field field : fields) {
            var property = field.getAnnotation(cloud.kitelang.api.annotations.Property.class);
            var name = property.name().isBlank() ? field.getName() : property.name();
            properties.append("\t");
            // Property kind: input, output, or regular (no prefix)
            switch (property.kind()) {
                case INPUT -> properties.append("input ");
                case OUTPUT -> properties.append("output ");
                case REGULAR -> {} // no prefix
            }
            var typename = field.getType().getSimpleName().toLowerCase();
            properties.append(typename);
            properties.append(" ");
            properties.append(name);
            if (property.importable()) {
                properties.append(" // importable");
            }
            properties.append("\n");
        }
        properties.append("}\n");

        var annotation = resource.getAnnotation(cloud.kitelang.api.annotations.TypeName.class);
        return "schema %s%s".formatted(annotation.value(), properties);
    }

    @SneakyThrows
    public static Schema toSchema(Class<?> resource) {
        Objects.requireNonNull(resource);
        var builder = Schema.builder();
        var schemaDefinition = resource.getAnnotation(cloud.kitelang.api.annotations.TypeName.class);
        if (schemaDefinition == null) {
            throw new RuntimeException("@SchemaDefinition annotation not found on class: "+resource.getName());
        }

        builder.name(schemaDefinition.value());
        builder.properties(new LinkedHashSet<>());
        builder.resourceClass(resource);

        var fields = resource.getDeclaredFields();
        for (Field field : fields) {
            var propertySchema = field.getAnnotation(cloud.kitelang.api.annotations.Property.class);
            var property = Property.builder();

            property.type(field.getType().getSimpleName().toLowerCase());

            property.kind(propertySchema.kind());
            property.importable(propertySchema.importable());
            property.description(propertySchema.description());

            String name = propertySchema.name().isBlank() ? field.getName() : propertySchema.name();
            property.name(name);

            property.hidden(propertySchema.hidden());

            builder.properties.add(property.build());
        }

        return builder.build();
    }

    public static String schemaName(Class<?> resourceClass) {
        if (resourceClass.isAnnotationPresent(TypeName.class)) return resourceClass.getAnnotation(TypeName.class).value();
        else return resourceClass.getSimpleName();
    }

}
