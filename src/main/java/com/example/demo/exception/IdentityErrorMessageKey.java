package com.example.demo.exception;

public enum IdentityErrorMessageKey {

    /**
     * All storage device error keys
     */

    INVALID_CREDENTIALS("invalid.credentials"),
    INVALID_TOKEN("invalid.token"),
    INVALID_USERNAME("invalid.username"),
    INVALID_OTP("invalid.otp"),
    OTP_EXPIRED("otp.expired"),
    USER_NOT_FOUND("user.not.found"),
    USED_PASSWORD("user.password"),
    UNABLE_TO_SEARCH("unable.search"),
    INVALID_RESOURCE_TYPE("invalid.resourceType"),
    UNABLE_SAVE_INSTITUTE("unable.save.institution");
    private final String key;

    IdentityErrorMessageKey(String keyVal) {
        key = keyVal;
    }

    public String getStatusCode() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }
}
