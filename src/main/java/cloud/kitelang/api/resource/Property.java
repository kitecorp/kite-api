package cloud.kitelang.api.resource;

import cloud.kitelang.api.annotations.PropertyKind;
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
        PropertyKind kind,
        boolean importable,
        boolean hidden) {

    public Object getValue() {
        return value == null ? defaultVal : value;
    }

    public boolean isInput() {
        return kind == PropertyKind.INPUT;
    }

    public boolean isOutput() {
        return kind == PropertyKind.OUTPUT;
    }

    public boolean isRegular() {
        return kind == PropertyKind.REGULAR;
    }
}
