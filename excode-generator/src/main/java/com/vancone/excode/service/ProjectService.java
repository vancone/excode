package com.vancone.excode.service;

import com.vancone.excode.entity.DataStoreRelational;
import com.vancone.excode.entity.Project;
import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import com.vancone.excode.repository.DataStoreRelationalRepository;
import com.vancone.excode.repository.ProjectRepository;
import com.vancone.excode.repository.SpringBootMicroserviceRepository;
import com.vancone.web.common.model.ResponsePage;
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
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private DataStoreRelationalRepository dataStoreRelationalRepository;

    @Autowired
    private SpringBootMicroserviceRepository springBootMicroserviceRepository;

    public Project create(Project project) {
        return projectRepository.save(project);
    }

    public void update(Project project) {
        projectRepository.save(project);
    }

    public Project query(String id) {
        return projectRepository.findById(id).get();
    }

    public ResponsePage<Project> queryPage(int pageNo, int pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Project example = new Project();
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
        Project project = query(projectId);
        if (project != null) {
            DataStoreRelational dataStoreRelational = new DataStoreRelational();
            dataStoreRelational.setProject(project);
            overview.put("dataStore", dataStoreRelationalRepository.count(Example.of(dataStoreRelational)));

            SpringBootMicroservice springBootMicroservice = new SpringBootMicroservice();
            springBootMicroservice.setProject(project);
            overview.put("microservice", springBootMicroserviceRepository.count(Example.of(springBootMicroservice)));
            overview.put("page", 0L);
            overview.put("document", 0L);
        }
        return overview;
    }
}
