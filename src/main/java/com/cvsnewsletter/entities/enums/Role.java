package com.cvsnewsletter.entities.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}
