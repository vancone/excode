package ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ExCode
 * @since ${date}
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${modelName} {
${fields}

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;
}
