package com.vancone.excode.service;

import cn.hutool.core.util.ZipUtil;
import com.vancone.excode.entity.DataStoreOld;
import com.vancone.excode.entity.ProjectOld;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.entity.Output;
import com.vancone.excode.enums.DataCarrier;
import com.vancone.excode.util.FileUtil;
import com.vancone.excode.util.OutputUtil;
import com.vancone.excode.util.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/07/24
 */
@Slf4j
@Service
public class ProjectWriterService {

    @Autowired
    private ProgressLogService progressLogService;

//    @Autowired
//    private SpringBootGenerator springBootGenerator;

    private final String GEN_LOCATION = "/opt/excode/gen" + File.separator;

    public String write(ProjectOld project) {
        List<Output> outputs = new ArrayList<>();
        String rootDirectory = GEN_LOCATION + project.getId() + File.separator;

        // Data source
        DataStoreOld store = project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).get(0);
        if (store != null) {
            String sql = SqlUtil.createDatabase(store) + "\n\n";

            for (DataStoreOld item: project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL)) {
                sql += SqlUtil.createTable(item) + "\n\n";
            }

            OutputUtil.addOutput(outputs, TemplateType.SQL, "create.sql", sql);
        }

//        outputs.addAll(springBootGenerator.generate(project));

        // Write to disk
        for (Output output: outputs) {
            String content = output.getTemplate() == null ? output.getContent() : output.getTemplate().getContent();
            FileUtil.write(rootDirectory + output.getPath(), content);
        }

        // Build and Package
//        SpringBootGenerator.build(this);
        progressLogService.output(project.getId(), "Complete");
        progressLogService.output(project.getId(), "<EOF>");
        return rootDirectory;
    }

    public void writeAndCompress(ProjectOld project) {
        ZipUtil.zip(write(project));
    }
}
