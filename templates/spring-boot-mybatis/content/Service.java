package ${template.properties.project.groupId}.${template.properties.project.artifactId}.service;

import ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity.${ModelName};
import com.vancone.web.common.model.ResponsePage;

/**
 * @author ExCode
 * @since ${date}
 */
public interface ${ModelName}Service {
    void create(${ModelName} ${modelName});

    ${ModelName} query(String id);

    ResponsePage<${ModelName}> queryPage(int pageNo, int pageSize);

    void update(${ModelName} ${modelName});

    void delete(String id);
}
