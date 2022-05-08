package com.vancone.excode.generator.service;

import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.entity.Microservice;
import com.vancone.excode.generator.entity.MicroserviceSpringBoot;
import com.vancone.excode.generator.entity.Project;
import com.vancone.excode.generator.entity.ProjectNew;
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

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@Service
public class ProjectNewService {

    @Autowired
    private ProjectRepository projectRepository;

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
}
