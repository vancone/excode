package com.vancone.monster.code.repository;

import com.vancone.monster.code.entity.DTO.Module;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Author: Tenton Lien
 * Date: 9/15/2020
 */

public interface ModuleRepository extends JpaRepository<Module, String> {

}
