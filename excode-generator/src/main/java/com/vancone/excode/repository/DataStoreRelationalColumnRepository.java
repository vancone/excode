package com.vancone.excode.repository;

import com.vancone.excode.entity.DataStoreRelationalColumn;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tenton Lien
 * @since 2022/05/09
 */
public interface DataStoreRelationalColumnRepository extends JpaRepository<DataStoreRelationalColumn, String> {
}
