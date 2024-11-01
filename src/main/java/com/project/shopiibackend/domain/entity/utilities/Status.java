package com.project.shopiibackend.domain.entity.utilities;


public enum Status {

    ACTIVE,

    INACTIVE;

    public static Status fromString(String status) {
        try {
            return Status.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }
    }
}
