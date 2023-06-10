package ${template.properties.project.groupId}.${template.properties.project.artifactId}.enums;

import com.vancone.web.common.exception.BaseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ExCode
 * @since {date}
 */
public enum ResponseEnum implements BaseEnum {

    LOGIN_REQUIRED(401, "Login required"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),

    ALREADY_EXIST(10001, "Resource already exist");

    @Getter
    @Setter
    private int code;

    @Getter @Setter
    private String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
