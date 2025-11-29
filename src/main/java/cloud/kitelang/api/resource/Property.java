package cloud.kitelang.api.resource;

import lombok.Builder;

@Builder
public record Property(
        String name,
        Object type,
        Object value,
        Object defaultVal,
        String description,
        String deprecationMessage,
        boolean required,
        boolean output,
        boolean importable,
        boolean hidden) {

    public Object getValue() {
        return value == null ? defaultVal : value;
    }
}
