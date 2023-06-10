package ${template.properties.project.groupId}.${template.properties.project.artifactId}.exception;


import com.vancone.web.common.model.Response;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author ExCode
 * @since ${date}
 */
@RestControllerAdvice
public class ValidExceptionHandler {

    @ExceptionHandler(BindException.class)
    public Response validExceptionHandler(BindException exception) {
        return Response.fail(400, exception.getAllErrors().get(0).getDefaultMessage());
    }
}