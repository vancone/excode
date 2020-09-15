package com.mekcone.studio.service;


import com.mekcone.studio.entity.Extension;
import org.springframework.data.domain.Page;

/*
 * Author: Tenton Lien
 * Date: 9/15/2020
 */

public interface ExtensionService {
    void create(Extension extension);
    Extension findById(String extensionId);
    Page<Extension> findAll(int pageNo, int pageSize);
    void save(Extension extension);
    void delete(String extensionId);
}
