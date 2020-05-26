package com.mekcone.excrud.web.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Host implements Serializable {
    private String id;
    private String name;
    private String ipAddress;
    private String status;
}
