package com.mekcone.excrud.codegen.model.file.nginx;

import lombok.Data;

import java.util.List;

@Data
public class NginxConfig {
    private String user = "nginx";
    private String workerProcesses = "auto";
    private String errorLog;
    private String pid;
    private String include;

    private Events events;

    @Data
    public class Events {
        private int workerConnections = 1024;
    }

    private Http http;

    @Data
    public class Http {
        private boolean sendfile = true;
        private boolean tcpNopush = true;
        private boolean tcpNodelay = true;
        private int keepaliveTimeout = 65;
        private int typesHashMaxSize = 2048;
        private String include;
        private String defaultType;


        @Data
        public class Server {
            private List<String> listens;
            private String serverName;
            private String root;
        }
    }
}
