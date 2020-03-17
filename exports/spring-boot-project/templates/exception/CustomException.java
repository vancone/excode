package ## groupId ##.## artifactId ##.exception;

import ## groupId ##.## artifactId ##.enums.BaseEnums;
import lombok.Data;

@Data
public class CustomException extends RuntimeException {
    private int code;

    public CustomException(BaseEnums baseEnums) {
        super(baseEnums.getMessage());
        this.code = baseEnums.getCode();
    }
}
