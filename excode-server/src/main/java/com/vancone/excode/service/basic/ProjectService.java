package com.vancone.excode.service.basic;

import com.vancone.excode.entity.DTO.Project;
import com.vancone.excode.entity.DTO.data.RootDataNode;
import com.vancone.excode.entity.DTO.data.source.DataSource;
import com.vancone.excode.entity.DTO.module.Module;
import com.vancone.excode.entity.DTO.module.SpringBootModule;
import com.vancone.excode.enums.ProjectEnum;
import com.vancone.excode.exception.ResponseException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Service
public class ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Project project) {
        LocalDateTime time = LocalDateTime.now();
        if (project.getId() == null) {
            project.setCreatedTime(time);

            // Initial modules and extensions
            List<Module> modules = new ArrayList<>();
            SpringBootModule springBootModule = new SpringBootModule();
            modules.add(springBootModule);
            project.setModules(modules);

            // Spring Boot Module
            springBootModule.setGroupId("com.vancone");
            springBootModule.setArtifactId(project.getName());
            springBootModule.setVersion("0.1.0");
        }
        project.setModifiedTime(time);
        mongoTemplate.save(project);
    }

    public Project query(String projectId) {
        Project project = mongoTemplate.findById(projectId, Project.class);
        if (project == null || project.getDeleted()) {
            throw new ResponseException(ProjectEnum.PROJECT_NOT_EXIST);
        }

        // Write data source
        for (RootDataNode rootDataNode: project.getDataTables()) {
            String dataSourceId = rootDataNode.getDataSourceId();
            DataSource dataSource = mongoTemplate.findById(dataSourceId, DataSource.class);
            rootDataNode.setDataSource(dataSource);
        }
        return project;
    }

    public Page<Project> queryPage(int pageNo, int pageSize, String search) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Query query = Query.query(Criteria.where("deleted").is(false));
        if (StringUtils.isNotBlank(search)) {
            query.addCriteria(Criteria.where("name").regex(search));
        }
        long count = mongoTemplate.count(query, Project.class);
        List<Project> projects = mongoTemplate.find(query.with(pageable), Project.class);
        return new PageImpl<>(projects, pageable, count);
    }

    public void delete(String projectId) {
        Project project = mongoTemplate.findById(projectId, Project.class);
        if (project != null) {
            project.setDeleted(true);
            mongoTemplate.save(project);
        }
    }

    public void generate(Project project) {

    }

    public void export(HttpServletResponse response, String fileType, String projectId) {
        response.setContentType("application/force-download");
        response.addHeader("Content-Disposition", "attachment;fileName=" + projectId + ".xml");
//        try {
//            OutputStream outputStream = response.getOutputStream();
//            Optional<Project> project = projectRepository.findById(projectId);
//            if (project.isPresent()) {
//                outputStream.write(project.toString().getBytes(StandardCharsets.UTF_8));
//            }
//            outputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
