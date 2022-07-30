package com.vancone.excode.repository;

import com.vancone.excode.entity.DataStoreRelational;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/05/09
 */
public interface DataStoreRelationalRepository extends JpaRepository<DataStoreRelational, String> {
    List<DataStoreRelational> findByProjectId(String projectId);
}
