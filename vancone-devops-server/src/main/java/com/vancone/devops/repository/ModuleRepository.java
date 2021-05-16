package com.vancone.devops.repository;

import com.vancone.devops.entity.DTO.Module;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Author: Tenton Lien
 * Date: 9/15/2020
 */

public interface ModuleRepository extends JpaRepository<Module, String> {

}
