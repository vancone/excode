package com.vancone.excode.generator.service.basic;

import com.vancone.excode.generator.entity.Project;
import com.vancone.excode.generator.entity.ResponsePage;
import com.vancone.excode.generator.repository.ProjectRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Tenton Lien
 */
@Service
public class ProjectService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ProjectRepository projectRepository;

    public void save(Project project) {
        projectRepository.save(project);
    }

    public Project query(String projectId) {
        if (StringUtils.isNotBlank(projectId)) {
            Optional<Project> optional = projectRepository.findById(projectId);
            if (optional.isPresent()) {
                return optional.get();
            }
        }
//        Project project = mongoTemplate.findById(projectId, Project.class);
//        if (project == null || project.getDeleted()) {
//            throw new ResponseException(ProjectEnum.PROJECT_NOT_EXIST);
//        }

        // Write data source
//        for (RootDataNode rootDataNode: project.getDataTables()) {
//            String dataSourceId = rootDataNode.getDataSourceId();
//            DataSource dataSource = mongoTemplate.findById(dataSourceId, DataSource.class);
//            rootDataNode.setDataSource(dataSource);
//        }
        return null;
    }

    public ResponsePage<Project> queryPage(int pageNo, int pageSize, String search) {
        Sort sort = Sort.by(Sort.Direction.DESC, "updatedTime");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return new ResponsePage<>(projectRepository.findAll(pageable));
    }

    public void delete(String projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project != null) {
            project.setUpdatedTime(LocalDateTime.now());
            mongoTemplate.save(project, "deleted_project");
            projectRepository.delete(project);
        }
    }

    public void generate(Project project) {

    }
}
