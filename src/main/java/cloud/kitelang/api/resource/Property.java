package cloud.kitelang.api.resource;

import lombok.Builder;

/**
 * Represents a schema property definition.
 */
@Builder
public record Property(
        String name,
        Object type,
        Object value,
        String description,
        String deprecationMessage,
        boolean cloud,
        boolean importable,
        boolean hidden) {

    /**
     * @return true if this is a cloud-managed property (read-only, set by cloud provider)
     */
    public boolean isCloud() {
        return cloud;
    }
}
