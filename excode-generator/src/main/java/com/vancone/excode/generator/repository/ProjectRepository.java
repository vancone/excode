package com.vancone.excode.generator.repository;

import com.vancone.excode.generator.entity.ProjectNew;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
public interface ProjectRepository extends JpaRepository<ProjectNew, String> {
}
