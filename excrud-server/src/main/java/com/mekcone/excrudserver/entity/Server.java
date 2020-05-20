package com.mekcone.excrudserver.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Server implements Serializable {
    private String id;
    private String name;
    private String ipAddress;
    private String status;
}
