package com.roslabsystem.todo.domain.user;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_USER;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
