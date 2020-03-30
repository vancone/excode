package __groupId__.__artifactId__.VO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVO<T> {
    private int code;
    private String message;
    private T data;
}
