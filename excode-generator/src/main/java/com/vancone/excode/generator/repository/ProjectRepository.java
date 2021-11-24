package com.vancone.excode.generator.repository;

import com.vancone.excode.generator.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tenton Lien
 * @since 2021/11/14
 */
public interface ProjectRepository extends JpaRepository<Project, String> {
}
