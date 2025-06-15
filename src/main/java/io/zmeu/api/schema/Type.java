package io.zmeu.api.schema;

import lombok.Getter;

@Getter
public enum Type {
    String("string"), Number("number"), Boolean("boolean");
    private final String value;

    Type(String value) {
        this.value = value;
    }

}
