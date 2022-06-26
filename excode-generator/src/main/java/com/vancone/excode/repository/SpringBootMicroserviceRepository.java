package com.vancone.excode.repository;

import com.vancone.excode.entity.microservice.SpringBootMicroservice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
public interface SpringBootMicroserviceRepository extends JpaRepository<SpringBootMicroservice, String> {
}
