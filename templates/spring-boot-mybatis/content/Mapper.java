package ${template.properties.project.groupId}.${template.properties.project.artifactId}.mapper;

import ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity.${ModelName};
import com.vancone.web.common.model.ResponsePage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ExCode
 * @since ${date}
 */
@Service
public interface ${ModelName}Mapper {
    List<${ModelName}> queryPage();
    ${ModelName} query(String id);
    boolean create(${ModelName} ${modelName});
    boolean update(${ModelName} ${modelName});
    boolean delete(String id);
}
