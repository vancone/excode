package com.mekcone.excrudserver.entity;

import com.mekcone.excrud.model.project.Project;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "project")
public class ProjectRecord extends Project {
    private String id;
    private String status;
    private String updatedTime;
}
