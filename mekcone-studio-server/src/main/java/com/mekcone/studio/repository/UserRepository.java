package com.mekcone.studio.repository;

import com.mekcone.studio.entity.DTO.User;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Author: Tenton Lien
 * Date: 9/28/2020
 */

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
}
