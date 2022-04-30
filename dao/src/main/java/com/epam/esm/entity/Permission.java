package com.epam.esm.entity;

public enum Permission {
    ADD_CERTIFICATE("add_certificate"),
    DELETE_CERTIFICATE("delete_certificate"),
    UPDATE_CERTIFICATE("update_certificate"),
    ADD_TAG("add_tag"),
    DELETE_TAG("delete_tag"),
    READ_TAGS("read_tags"),
    READ_TAG_BY_ID("read_tag_by_id"),
    MOST_POPULAR_TAG("most_popular_tag"),
    READ_USERS("read_users"),
    READ_USER_BY_ID("read_user_by_id"),
    ADD_ORDER("add_order"),
    GET_ALL_USER_ORDERS("get_all_user_orders"),
    GET_ORDER_BY_ID("get_order_by_id");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
