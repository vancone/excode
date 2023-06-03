package ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author ExCode
 * @since ${date}
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ${modelName} {
${fields}
}
