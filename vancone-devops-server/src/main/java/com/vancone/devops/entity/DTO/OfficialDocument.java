package com.vancone.devops.entity.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Tenton Lien
 * @date 3/13/2021
 */
@Data
@Entity
@Table(name = "document")
public class OfficialDocument {

    @Id
    private String id;

    private String title;

    private String author;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(updatable = false)
    @CreationTimestamp
    private Date createdTime;

    @Column(columnDefinition = "TEXT")
    private String content;
}
