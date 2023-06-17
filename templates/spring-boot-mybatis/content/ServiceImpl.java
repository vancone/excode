package ${template.properties.project.groupId}.${template.properties.project.artifactId}.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity.${ModelName};
${otherEntityImports}
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.enums.ResponseEnum;
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.mapper.${ModelName}Mapper;
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.service.${ModelName}Service;
import com.vancone.web.common.exception.ResponseException;
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
        PageHelper.startPage(pageNo, pageSize);
        return new ResponsePage<>(new PageInfo<>(${modelName}Mapper.queryPage()));
    }

    @Override
    public void update(${ModelName} ${modelName}) {
        ${modelName}Mapper.update(${modelName});
    }

    @Override
    public void delete(String id) {
        ${ModelName} ${modelName} = query(id);
        if (${modelName} == null) {
            throw new ResponseException(ResponseEnum.NOT_FOUND);
        }
        ${modelName}Mapper.delete(id);
    }

${otherMethods}
}
