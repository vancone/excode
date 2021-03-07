package com.vancone.monster.code.service.web.impl;

import com.vancone.monster.code.repository.ModuleRepository;
import com.vancone.monster.code.service.web.ModuleService;
import com.vancone.monster.code.entity.DTO.Module;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Override
    public Module findById(String moduleId) {
        Optional<Module> module = moduleRepository.findById(moduleId);
        return module.orElse(null);
    }

    @Override
    public Page<Module> findAll(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return moduleRepository.findAll(pageable);
    }

    @Override
    public void save(Module module) {
        try {
            moduleRepository.save(module);
        } catch (Exception e) {
            log.error(e.toString());
        }

    }

    @Override
    public void delete(String moduleId) {
        moduleRepository.deleteById(moduleId);
    }
}
