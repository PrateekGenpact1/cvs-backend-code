package com.cvsnewsletter.entities.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN"),
    MANAGER("MANAGER");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getPrefixedValue() {
        return "ROLE_" + value;
    }
}
