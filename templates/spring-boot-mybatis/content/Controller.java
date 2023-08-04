package ${template.properties.project.groupId}.${template.properties.project.artifactId}.controller;

import ${template.properties.project.groupId}.${template.properties.project.artifactId}.entity.${ModelName};
import ${template.properties.project.groupId}.${template.properties.project.artifactId}.service.${ModelName}Service;

import com.vancone.web.common.model.Response;
import com.vancone.web.common.model.ResponsePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ExCode
 * @since ${date}
 */
@RestController
@RequestMapping("/api/${template.properties.project.artifactId}/${modelName}")
public class ${ModelName}Controller {
    @Autowired
    private ${ModelName}Service ${modelName}Service;

    @PostMapping
    public Response create(@RequestBody ${ModelName} ${modelName}) {
        ${modelName}Service.create(${modelName});
        return Response.success();
    }

    @GetMapping("/{id}")
    public Response<${ModelName}> query(@PathVariable String id) {
        ${ModelName} ${modelName} = ${modelName}Service.query(id);
        return Response.success(${modelName});
    }

    @GetMapping
    public Response<ResponsePage<${ModelName}>> queryPage(
            @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize) {
        ResponsePage<${ModelName}> ${modelName}Page = ${modelName}Service.queryPage(pageNo, pageSize);
        return Response.success(${modelName}Page);
    }

    @PutMapping
    public Response update(@RequestBody ${ModelName} ${ModelName}) {
        ${modelName}Service.update(${ModelName});
        return Response.success();
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable String id) {
        ${modelName}Service.delete(id);
        return Response.success();
    }

${otherMethods}
}
