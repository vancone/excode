package com.vancone.excode.generator.service;

import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.entity.*;
import com.vancone.excode.generator.repository.DataStoreRelationalRepository;
import com.vancone.excode.generator.repository.MicroserviceSpringBootRepository;
import com.vancone.excode.generator.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@Service
public class ProjectNewService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DataStoreRelationalRepository dataStoreRelationalRepository;

    @Autowired
    private MicroserviceSpringBootRepository microserviceSpringBootRepository;

    public ProjectNew create(ProjectNew project) {
        return projectRepository.save(project);
    }

    public void update(ProjectNew project) {
        projectRepository.save(project);
    }

    public ProjectNew query(String id) {
        return projectRepository.findById(id).get();
    }

    public ResponsePage<ProjectNew> queryPage(int pageNo, int pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        ProjectNew example = new ProjectNew();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        if (StringUtils.isNotBlank(search)) {
            example.setName(search);
        }
        return new ResponsePage<>(projectRepository.findAll(Example.of(example, matcher), pageable));
    }

    public void delete(String id) {
        projectRepository.deleteById(id);
    }

    public Map<String, Long> overview(String projectId) {
        Map<String, Long> overview = new HashMap<>(4);
        ProjectNew project = query(projectId);
        if (project != null) {
            DataStoreRelational dataStoreRelational = new DataStoreRelational();
            dataStoreRelational.setProject(project);
            overview.put("dataStore", dataStoreRelationalRepository.count(Example.of(dataStoreRelational)));

            MicroserviceSpringBoot microserviceSpringBoot = new MicroserviceSpringBoot();
            microserviceSpringBoot.setProject(project);
            overview.put("microservice", microserviceSpringBootRepository.count(Example.of(microserviceSpringBoot)));
            overview.put("page", 0L);
            overview.put("document", 0L);
        }
        return overview;
    }
}
