package com.mekcone.excrudserver.entity;

import com.mekcone.excrud.model.project.Project;
import lombok.Data;

@Data
public class ProjectRecord extends Project {
    private String id;
    private String status;
    private String updatedTime;
}
