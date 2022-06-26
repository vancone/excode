package com.vancone.excode.repository;

import com.vancone.excode.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
public interface ProjectRepository extends JpaRepository<Project, String> {
}
