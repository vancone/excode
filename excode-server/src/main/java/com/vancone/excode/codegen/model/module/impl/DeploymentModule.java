package com.vancone.excode.codegen.model.module.impl;

import com.vancone.excode.codegen.annotation.ExtensionClass;
import com.vancone.excode.codegen.annotation.Validator;
import com.vancone.excode.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class DeploymentModule extends Module {

    private List<OperatingSystem> operatingSystems = new ArrayList<>();

    @Validator({"docker", "supervisor", "systemd"})
    private String daemon;

    @Data
    public static class OperatingSystem {
        @Validator({"centos", "ubuntu"})
        private String type;

        private String version;
    }

    private DeploymentExtensions extensions;

    @Data
    public static class DeploymentExtensions {

        private Nginx nginx;
        private Redis redis;

        public List<Extension> asList() {
            List<Extension> extensionList = new ArrayList<>();
            extensionList.add(nginx);
            extensionList.add(redis);
            return extensionList;
        }

        @Data
        @ExtensionClass
        public static class Nginx extends Extension {
            private String user = "www-data";

            private String workerProcesses = "auto";

            private String pid = "/run/nginx.pid";

            private String include = "/etc/nginx/modules-enabled/*.conf";

            // Events
            private int workerConnections = 768;

            private Http http;

            @Data
            public static class Http {
                private boolean sendfile = true;

                private boolean tcpNopush = true;

                private boolean tcpNodelay = true;

                private int keepaliveTimeout = 65;

                private int typesHashMaxSize = 2048;

                private String httpInclude = "/etc/nginx/mime.types";

                private String defaultType = "application/octet-stream";

                private String sslProtocols = "TLSv1 TLSv1.1 TLSv1.2 TLSv1.3";

                private boolean sslPreferServerCiphers = true;

                private String accessLog = "/var/log/nginx/access.log";

                private String errorLog = "/var/log/nginx/error.log";

                private boolean gzip = true;

                private List<String> virtualHostConfigsInclude = new ArrayList<>(Arrays.asList(
                        "include /etc/nginx/conf.d/*.conf",
                        "include /etc/nginx/sites-enabled/*"
                ));

                private List<Server> servers = new ArrayList<>();

                @Data
                public static class Server {
                    private String type;
                    private int port;
                    private String home;
                }
            }

            @Override
            public String toString() {
                return "user " + user + ";\n" +
                        "worker_processes " + workerProcesses + ";\n" +
                        "pid " + pid + ";\n" +
                        "include " + include + ";\n\n" +
                        "events {\n" +
                        "    worker_connections " + workerConnections + ";\n" +
                        "}\n\n" +
                        "http {\n" +
                        "    sendfile " + (http.sendfile ? "on" : "off") + ";\n" +
                        "    tcp_nopush " + (http.tcpNopush ? "on" : "off") + ";\n" +
                        "    tcp_nodelay " + (http.tcpNodelay ? "on" : "off") + ";\n" +
                        "    keepalive_timeout " + http.keepaliveTimeout + ";\n" +
                        "    types_hash_max_size " + http.typesHashMaxSize + ";\n" +
                        "    include " + http.httpInclude + ";\n" +
                        "    default_type " + http.defaultType + ";\n" +
                        "    ssl_protocols " + http.sslProtocols + ";\n" +
                        "    ssl_prefer_server_ciphers " + (http.sslPreferServerCiphers ? "on" : "off") + ";\n" +
                        "    access_log " + http.accessLog + ";\n" +
                        "    error_log " + http.errorLog + ";\n" +
                        "    gzip " + (http.gzip ? "on" : "off") + ";\n" +
                        "}";
            }
        }

        @Data
        @ExtensionClass
        public static class Redis extends Extension {
            private int bind;
            private boolean protectedMode;
            private int port;
            private int tcpBacklog;
            private int timeout;
            private int tcpKeepalive;
            private boolean daemonize;
            private boolean supervised;
            private String pidffile;
            private String loglevel;
            private String logfile;
            private int databases;
            private boolean alwaysShowLogo = true;
            private boolean stopWritesOnBgsaveError = true;
            private boolean rdbCompression = true;
            private boolean rdbchecksum = true;
        }
    }
}
