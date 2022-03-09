package com.vancone.excode.generator.service;

import com.vancone.cloud.common.exception.ResponseException;
import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.enums.ResponseEnum;
import com.vancone.excode.generator.entity.Project;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 */
@Service
public class ProjectService {

    @Autowired
    private DataStoreService dataStoreService;

    @Autowired
    private ProjectWriterService projectWriterService;

    @Autowired
    private ProgressLogService progressLogService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(Project project) {
        mongoTemplate.save(project);
    }

    public Project query(String projectId) {
        Project project = mongoTemplate.findById(projectId, Project.class);
        if (project != null && project.getDataAccess() != null) {
            project.getDataAccess().setDataStores(dataStoreService.queryList(projectId));
        }
        return project;
    }

    public ResponsePage<Project> queryPage(int pageNo, int pageSize, String search) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Query query = new Query();
        if (StringUtils.isNotBlank(search)) {
            query.addCriteria(Criteria.where("name").regex(search));
        }
        long count = mongoTemplate.count(query, Project.class);
        List<Project> projects = mongoTemplate.find(query.with(pageable), Project.class);
        return new ResponsePage<>(new PageImpl<>(projects, pageable, count));
    }

    public void delete(String projectId) {
        Project project = query(projectId);
        if (project != null) {
            project.setUpdatedTime(LocalDateTime.now());
            mongoTemplate.save(project, "deleted_project");
            mongoTemplate.remove(project);
        }
    }

    public void generate(String projectId) {
        // Check if there is a running task
        if (!progressLogService.isFinished(projectId)) {
            throw new ResponseException(ResponseEnum.PROJECT_GENERATE_TASK_ALREADY_EXIST);
        } else {
            progressLogService.clear(projectId);
        }

        Project project = query(projectId);
        projectWriterService.writeAndCompress(project);
    }

    public void saveSolution(String projectId, Project.DataAccess.Solution solution) {
        Project project = query(projectId);
        if (project != null) {
            if (project.getDataAccess() == null) {
                project.setDataAccess(new Project.DataAccess());
            }
            project.getDataAccess().setSolution(solution);
            save(project);
        }
    }

    public Map<String, Integer> overview(String projectId) {
        Map<String, Integer> overview = new HashMap<>(4);
        Project project = query(projectId);
        if (project != null) {
            overview.put("api", 0);
            overview.put("page", 0);

            // Count data stores
            int dataStoreCount = 0;
            if (project.getDataAccess() != null) {
                if (project.getDataAccess().getDataStores() != null) {
                    dataStoreCount = project.getDataAccess().getDataStores().size();
                }
            }
            overview.put("dataStore", dataStoreCount);
        }
        return overview;
    }
}
