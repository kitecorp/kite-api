package cloud.kitelang.api.resource;

import lombok.Builder;

import java.util.List;

/**
 * Represents a schema property definition.
 */
@Builder
public record Property(
        String name,
        Object type,
        Class<?> typeClass,  // The actual Java class for this property's type
        Object value,
        String description,
        String deprecationMessage,
        boolean cloud,
        boolean importable,
        boolean hidden,
        boolean sensitive,            // Value must be masked in rendered output (plans, logs)
        String defaultValue,          // Default value as string (from field initialization)
        List<String> validValues) {   // Valid values for string enum properties

    /**
     * @return true if this is a cloud-managed property (read-only, set by cloud provider)
     */
    public boolean isCloud() {
        return cloud;
    }

    /**
     * @return true if the property's value must never be rendered in plans,
     * diffs, or logs (it still flows to providers and state unmasked)
     */
    public boolean isSensitive() {
        return sensitive;
    }
}
