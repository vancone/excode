package ## groupId ##.## artifactId ##.exception;

import ## groupId ##.## artifactId ##.VO.ResultVO;
import ## groupId ##.## artifactId ##.util.ResultVOUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class CustomExceptionHandler {
    @ExceptionHandler(value = CustomException.class)
    public ResultVO handlerLoginException(CustomException e){
        return ResultVOUtil.fail(e.getCode(), e.getMessage());
    }
}