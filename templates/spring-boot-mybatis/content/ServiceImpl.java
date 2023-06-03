package ${template.properties.project.groupId}.${template.properties.project.artifactId}.service.impl;

import ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity.${ModelName};
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.mapper.${ModelName}Mapper;
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.service.${ModelName}Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.vancone.web.common.model.ResponsePage;
import org.springframework.stereotype.Service;

/**
 * @author ExCode
 * @since ${date}
 */
@Service
public class ${ModelName}ServiceImpl implements ${ModelName}Service {
    @Autowired
    private ${ModelName}Mapper ${modelName}Mapper;

    @Override
    public void create(${ModelName} ${modelName}) {
        ${modelName}Mapper.create(${modelName});
    }

    @Override
    public ${ModelName} query(String id) {
        return ${modelName}Mapper.query(id);
    }

    @Override
    public ResponsePage<${ModelName}> queryPage(int pageNo, int pageSize) {
        return ${modelName}Mapper.queryPage(pageNo, pageSize);
    }

    @Override
    public void update(${ModelName} ${modelName}) {
        ${modelName}Mapper.update(${modelName});
    }

    @Override
    public void delete(String id) {
        ${modelName}Mapper.query(id);
    }
}
