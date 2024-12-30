package com.example.demo.util;

import com.example.demo.model.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Service
@Slf4j
public class ResourceIdUtils {

    //todo-to make this static method
    public static String generateInstituteResourceId(String instituteName) {
        return generateGlobalResourceId(ResourceType.INSTITUTE, instituteName);
    }

    public static String generateStudentResourceId(String firstName, long l) {
        return generateGlobalResourceId(ResourceType.STUDENT, firstName, l);
    }
    /**
     * Generate a global resource ID based on the resource type
     *
     * @param type The type of resource
     * @param args the values of attributes that uniquely identify the resource. The generator
     *             is sensitive to the order of the specified values
     */
    public static String generateGlobalResourceId(ResourceType type, Object... args) {
        boolean isCaseSensitive = false;
        final String prefix = ((type != null && type != ResourceType.UNDEFINED)
                        ? type.persistValue()
                        : Constant.DEFAULT)
                    + "-";

        StringBuilder md5Input = new StringBuilder();
        for (Object arg : args) {
            if (arg != null) {
                if (md5Input.length() == 0) {
                    md5Input.append(arg.toString());
                } else {
                    md5Input.append(":").append(arg.toString());
                }
            }
        }
        String md5Hash;
        if (isCaseSensitive) {
            md5Hash = org.springframework.util.DigestUtils.md5DigestAsHex(md5Input.toString().getBytes()).toLowerCase();

        } else {
            md5Hash = org.springframework.util.DigestUtils.md5DigestAsHex(md5Input.toString().toLowerCase().getBytes()).toLowerCase();

        }
        return prefix + md5Hash;
    }

}