package com.vancone.excode.generator.service;

import com.vancone.cloud.common.model.ResponsePage;
import com.vancone.excode.generator.entity.Project;
import com.vancone.excode.generator.entity.Template;
import com.vancone.excode.generator.enums.TemplateType;
import com.vancone.excode.generator.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/07/24
 */
@Service
public class TemplateFactoryService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public ResponsePage<Template> queryPage(int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "name");
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Query query = new Query();
        long count = mongoTemplate.count(query, Template.class);
        List<Template> templates = mongoTemplate.find(query.with(pageable), Template.class);
        return new ResponsePage<>(new PageImpl<>(templates, pageable, count));
    }

    public MongoTemplate getMongoTemplate() {
        return mongoTemplate;
    }

    public Template getTemplate(TemplateType type) {
        return mongoTemplate.findOne(Query.query(Criteria.where("type").is(type)), Template.class);
    }

    public List<Template> getTemplatesByModuleName(String moduleName) {
        return mongoTemplate.find(Query.query(Criteria.where("module").regex(moduleName)), Template.class);
    }

    public void preProcess(Project.DataAccess.Solution.JavaSpringBoot module, Template template) {
        template.replace("groupId", module.getGroupId());
        template.replace("artifactId", module.getArtifactId());
        template.replace("artifact.id", module.getArtifactId().replace('-', '.'));
        template.replace("ArtifactId", StrUtil.upperCamelCase(module.getArtifactId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        template.replace("date", formatter.format(LocalDateTime.now()));
    }
}
