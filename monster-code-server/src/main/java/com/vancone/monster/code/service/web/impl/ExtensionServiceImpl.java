package com.vancone.monster.code.service.web.impl;

import com.vancone.monster.code.repository.ExtensionRepository;
import com.vancone.monster.code.service.web.ExtensionService;
import com.vancone.monster.code.entity.DTO.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/*
 * Author: Tenton Lien
 * Date: 9/15/2020
 */

@Service
public class ExtensionServiceImpl implements ExtensionService {

    @Autowired
    private ExtensionRepository extensionRepository;

    @Override
    public void create(Extension extension) {
        extensionRepository.save(extension);
    }

    @Override
    public Extension findById(String extensionId) {
        Optional<Extension> extension = extensionRepository.findById(extensionId);
        return extension.orElse(null);
    }

    @Override
    public Page<Extension> findAll(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return extensionRepository.findAll(pageable);
    }

    @Override
    public void save(Extension extension) {
        extensionRepository.save(extension);
    }

    @Override
    public void delete(String extensionId) {
        extensionRepository.deleteById(extensionId);
    }

}
