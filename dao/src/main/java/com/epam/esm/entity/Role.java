package com.epam.esm.entity;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    USER(Set.of(Permission.ADD_ORDER, Permission.GET_ALL_USER_ORDERS, Permission.GET_ORDER_BY_ID, Permission.READ_TAGS,
            Permission.READ_TAG_BY_ID, Permission.MOST_POPULAR_TAG, Permission.READ_USERS, Permission.READ_USER_BY_ID)),
    ADMIN(Set.of(Permission.ADD_ORDER, Permission.GET_ALL_USER_ORDERS, Permission.GET_ORDER_BY_ID, Permission.READ_TAGS,
            Permission.READ_TAG_BY_ID, Permission.MOST_POPULAR_TAG, Permission.READ_USERS, Permission.READ_USER_BY_ID,
            Permission.ADD_CERTIFICATE, Permission.DELETE_CERTIFICATE, Permission.UPDATE_CERTIFICATE, Permission.ADD_TAG,
            Permission.DELETE_TAG));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}
