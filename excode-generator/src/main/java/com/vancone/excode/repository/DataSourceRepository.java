package com.vancone.excode.repository;

import com.vancone.excode.entity.DataSource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Tenton Lien
 * @since 2022/05/17
 */
public interface DataSourceRepository extends JpaRepository<DataSource, String> {
}
