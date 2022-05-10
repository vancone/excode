package com.vancone.excode.generator.repository;

import com.vancone.excode.generator.entity.DataStoreRelational;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tenton Lien
 * @since 2022/05/09
 */
public interface DataStoreRelationalRepository extends JpaRepository<DataStoreRelational, String> {
}
