package cloud.kitelang.api.resource;

import cloud.kitelang.api.annotations.PropertyKind;
import lombok.Builder;

@Builder
public record Property(
        String name,
        Object type,
        Object value,
        String description,
        String deprecationMessage,
        PropertyKind kind,
        boolean importable,
        boolean hidden) {

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
