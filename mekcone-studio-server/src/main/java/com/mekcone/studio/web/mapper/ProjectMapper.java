package com.mekcone.studio.web.mapper;

import com.mekcone.studio.web.entity.Project;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface ProjectMapper {
    List<Project> retrieveList();
    Project retrieveTest();
}
