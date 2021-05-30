package com.vancone.devops.service.basic;

import com.vancone.devops.enums.ProjectEnum;
import com.vancone.devops.exception.ResponseException;
import com.vancone.devops.entity.DTO.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
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
        }
        project.setModifiedTime(time);
        mongoTemplate.save(project);
    }

    public Project findById(String projectId) {
        Project project = mongoTemplate.findById(projectId, Project.class);
        if (project == null || project.getDeleted()) {
            throw new ResponseException(ProjectEnum.PROJECT_NOT_EXIST);
        }
        return project;
    }

    public Page<Project> query(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "modifiedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Query query = Query.query(Criteria.where("deleted").is(false));
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