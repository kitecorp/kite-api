package cloud.kitelang.api.schema;

import cloud.kitelang.api.annotations.Property;
import cloud.kitelang.api.annotations.TypeName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * {@code @Property(optional = false)} must reach the schema as {@code Property.required}: the doc
 * generator and the provider's gRPC schema read the schema, not the annotation
 * (kitecorp/kite-api#4, for kitecorp/kite-provider-sdk#12).
 */
class SchemaRequiredTest {

    @TypeName("Instance")
    static class Instance {
        @Property(optional = false)
        private String ami;

        @Property
        private String keyName;
    }

    @Test
    @DisplayName("toSchema marks a property required exactly when @Property says optional = false")
    void requiredFollowsTheOptionalFlag() {
        var schema = Schema.toSchema(Instance.class);

        var requiredByName = schema.getProperties().stream()
                .collect(Collectors.toMap(
                        cloud.kitelang.api.resource.Property::name,
                        cloud.kitelang.api.resource.Property::required));
        assertEquals(Map.of("ami", true, "keyName", false), requiredByName);
    }

    @Test
    @DisplayName("a property built without setting required is optional")
    void builderDefaultIsOptional() {
        // Schemas that are not built from annotations, such as the terraform bridge's, never set
        // the field; they must keep meaning optional, as they did before the field existed
        var property = cloud.kitelang.api.resource.Property.builder().name("length").build();

        assertEquals(false, property.required());
    }
}
