package com.vancone.excode.generator.service;

import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.entity.Microservice;
import com.vancone.excode.generator.entity.MicroserviceSpringBoot;
import com.vancone.excode.generator.repository.MicroserviceSpringBootRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author Tenton Lien
 * @since 2022/05/08
 */
@Slf4j
@Service
public class MicroserviceSpringBootService {

    @Autowired
    private MicroserviceSpringBootRepository microserviceSpringBootRepository;

    public Microservice create(MicroserviceSpringBoot microserviceSpringBoot) {
        return microserviceSpringBootRepository.save(microserviceSpringBoot);
    }

    public void update(MicroserviceSpringBoot microserviceSpringBoot) {
        microserviceSpringBootRepository.save(microserviceSpringBoot);
    }

    public MicroserviceSpringBoot query(String id) {
        return microserviceSpringBootRepository.findById(id).get();
    }

    public ResponsePage<MicroserviceSpringBoot> queryPage(int pageNo, int pageSize, String search) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        MicroserviceSpringBoot example = new MicroserviceSpringBoot();
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        if (StringUtils.isNotBlank(search)) {
            example.setName(search);
        }
        return new ResponsePage<>(microserviceSpringBootRepository.findAll(Example.of(example, matcher), pageable));
    }

    public void delete(String id) {
        microserviceSpringBootRepository.deleteById(id);
    }
}
