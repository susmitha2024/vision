package com.example.demo.model;

import com.example.demo.exception.ErrorMessageHandler;
import com.example.demo.exception.IdentityErrorMessageKey;
import com.example.demo.exception.IdentityException;
import com.example.demo.util.Constant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResourceType {

    INSTITUTE("institute"                                     ,Constant.INSTITUTE),
    STUDENT("student"                                     ,Constant.STUDENT),


    UNDEFINED(""                                    , "");

    private final String value;
    private final String persistValue;

    public String value() {
        return this.value;
    }
    public String persistValue() {
        return this.persistValue;
    }

    public static ResourceType fromValue(final String value) throws IdentityException {
        ResourceType type = byValue(value);
        if(type != UNDEFINED)
            return type;
        throw new IdentityException(String.format(ErrorMessageHandler.getMessage(IdentityErrorMessageKey.INVALID_RESOURCE_TYPE), value), HttpStatus.BAD_REQUEST);
    }

    //TODO byValue should be replaced with appropriate name
    public static ResourceType byValue(final String value) {
        if(StringUtils.isNotEmpty(value)) {
            for (ResourceType type : values()) {
                if (value.equalsIgnoreCase(type.name())
                        || value.equalsIgnoreCase(type.value())
                        || value.equalsIgnoreCase(type.persistValue())) {
                    return type;
                }
            }
        }
        return UNDEFINED;
    }
}
