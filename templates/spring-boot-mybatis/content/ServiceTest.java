package ${template.properties.project.groupId}.${template.properties.project.artifactId}.service.impl;

import ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity.${ModelName};
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.service.${ModelName}Service;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.vancone.web.common.model.ResponsePage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author ExCode
 * @since ${date}
 */
@SpringBootTest
public class ${ModelName}ServiceTest {
    @Autowired
    private ${ModelName}Service ${modelName}Service;

    @Test
    @Transactional
    public void create() {
        ${ModelName} ${modelName} = new ${ModelName}();
        ${modelName}Service.create(${modelName});
    }

    @Test
    @Transactional
    public void query(String id) {
        ${modelName}Service.query(id);
    }

    @Test
    @Transactional
    public void queryPage() {
        int pageNo = 0, pageSize = 10;
        ${modelName}Service.queryPage(pageNo, pageSize);
    }

    @Test
    @Transactional
    public void update() {
        ${ModelName} ${modelName} = new ${ModelName}();
        ${modelName}Service.update(${modelName});
    }

    @Test
    @Transactional
    public void delete(String id) {
        ${modelName}Service.delete(id);
    }
}
