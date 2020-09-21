package com.mekcone.studio.repository;

import com.mekcone.studio.entity.DTO.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Author: Tenton Lien
 * Date: 9/14/2020
 */

public interface ProjectRepository extends JpaRepository<Project, String> {
}
