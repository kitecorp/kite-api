package cloud.kitelang.api.schema;

import cloud.kitelang.api.annotations.Cloud;
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

    /**
     * Generates Kite schema definition string from a Java resource class.
     * Uses @cloud decorator for cloud-managed properties (immutable/read-only from cloud).
     */
    public static String toString(Class<?> resource) {
        var fields = resource.getDeclaredFields();
        var properties = new StringBuilder(" {\n");
        for (Field field : fields) {
            var property = field.getAnnotation(cloud.kitelang.api.annotations.Property.class);
            var name = property.name().isBlank() ? field.getName() : property.name();

            properties.append("\t");
            // Use @cloud decorator for cloud-managed properties
            var cloudAnnotation = field.getAnnotation(Cloud.class);
            if (cloudAnnotation != null) {
                if (cloudAnnotation.importable()) {
                    properties.append("@cloud(importable) ");
                } else {
                    properties.append("@cloud ");
                }
            }

            var typename = field.getType().getSimpleName().toLowerCase();
            properties.append(typename);
            properties.append(" ");
            properties.append(name);
            properties.append("\n");
        }
        properties.append("}\n");

        var annotation = resource.getAnnotation(TypeName.class);
        return "schema %s%s".formatted(annotation.value(), properties);
    }

    @SneakyThrows
    public static Schema toSchema(Class<?> resource) {
        Objects.requireNonNull(resource);
        var builder = Schema.builder();
        var schemaDefinition = resource.getAnnotation(TypeName.class);
        if (schemaDefinition == null) {
            throw new RuntimeException("@SchemaDefinition annotation not found on class: "+resource.getName());
        }

        builder.name(schemaDefinition.value());
        builder.properties(new LinkedHashSet<>());
        builder.resourceClass(resource);

        // Create instance to read default field values
        Object defaultInstance = null;
        try {
            var noArgConstructor = resource.getDeclaredConstructor();
            noArgConstructor.setAccessible(true);
            defaultInstance = noArgConstructor.newInstance();
        } catch (Exception ignored) {
            // No default instance available, defaults will be null
        }

        var fields = resource.getDeclaredFields();
        for (Field field : fields) {
            var propertySchema = field.getAnnotation(cloud.kitelang.api.annotations.Property.class);
            var property = Property.builder();

            property.type(field.getType().getSimpleName().toLowerCase());
            property.typeClass(field.getType());  // Store actual class for struct detection

            var cloudAnnotation = field.getAnnotation(Cloud.class);
            property.cloud(cloudAnnotation != null);
            // importable comes from @Cloud annotation (for cloud-managed importable properties)
            property.importable(cloudAnnotation != null && cloudAnnotation.importable());
            property.description(propertySchema.description());

            String name = propertySchema.name().isBlank() ? field.getName() : propertySchema.name();
            property.name(name);

            property.hidden(propertySchema.hidden());

            // Extract validValues from annotation
            var validValues = propertySchema.validValues();
            if (validValues.length > 0) {
                property.validValues(List.of(validValues));
            }

            // Extract default value from field initialization
            if (defaultInstance != null) {
                try {
                    field.setAccessible(true);
                    var defaultValue = field.get(defaultInstance);
                    if (defaultValue != null) {
                        property.defaultValue(String.valueOf(defaultValue));
                    }
                } catch (Exception ignored) {
                    // Could not read default value
                }
            }

            builder.properties.add(property.build());
        }

        return builder.build();
    }

    public static String schemaName(Class<?> resourceClass) {
        if (resourceClass.isAnnotationPresent(TypeName.class)) return resourceClass.getAnnotation(TypeName.class).value();
        else return resourceClass.getSimpleName();
    }

}
