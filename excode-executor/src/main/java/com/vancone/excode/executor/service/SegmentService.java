package com.vancone.excode.executor.service;

import com.vancone.excode.executor.entity.Segment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author Tenton Lien
 * @since 8/1/2021
 */
@Slf4j
@Service
public class SegmentService {

    @Autowired
    private MongoTemplate mongoTemplate;

    /*public Page<Program> query() {
        PageRequest page = PageRequest.of(0, 10);
        List<Program> programs = mongoTemplate.findAll(Program.class);
        return new PageImpl<Program>();
    }*/

    public void create(Segment segment) {
        mongoTemplate.save(segment);
        log.info("Create segment (id={})", segment.getId());
    }

    public void delete(String id) {
        Segment segment = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Segment.class);
        if (segment != null) {
            mongoTemplate.remove(segment);
            log.info("Delete segment (id={})", id);
        }
    }

    public void execute(String id) {
        Segment segment = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Segment.class);
//        if (segment != null) {
            try {
                Process process = Runtime.getRuntime().exec("ipconfig");
                InputStream ins= process.getInputStream();
                InputStream ers= process.getErrorStream();
                new Thread(new inputStreamThread(ins)).start();
                process.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Execute segment (id={})", id);
//        }

    }

    class inputStreamThread implements Runnable{
        private InputStream ins = null;
        private BufferedReader bfr = null;
        public inputStreamThread(InputStream ins){
            this.ins = ins;
            this.bfr = new BufferedReader(new InputStreamReader(ins));
        }
        @Override
        public void run() {
            String line = null;
            byte[] b = new byte[100];
            int num = 0;
            try {
                while((num=ins.read(b))!=-1){
                    System.out.println(new String(b,"gb2312"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
