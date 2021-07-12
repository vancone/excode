package com.vancone.excode.core.model.module.impl;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.vancone.excode.core.annotation.ExtensionClass;
import com.vancone.excode.core.annotation.Validator;
import com.vancone.excode.core.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class DeploymentModule extends Module {

    @JacksonXmlElementWrapper(localName = "operating-systems")
    @JacksonXmlProperty(localName = "operating-system")
    private List<OperatingSystem> operatingSystems = new ArrayList<>();

    @Validator({"docker", "supervisor", "systemd"})
    private String daemon;

    @Data
    public static class OperatingSystem {
        @Validator({"centos", "ubuntu"})
        private String type;

        private String version;
    }

    private Properties properties;

    @Data
    public static class Properties {

        private Nginx nginx;
        private Redis redis;

        @Data
        @ExtensionClass
        public static class Nginx {
            private String user = "www-data";

            @JacksonXmlProperty(localName = "worker-processes")
            private String workerProcesses = "auto";

            private String pid = "/run/nginx.pid";

            private String include = "/etc/nginx/modules-enabled/*.conf";

            // Events
            @JacksonXmlProperty(localName = "worker-connections")
            private int workerConnections = 768;

            private Http http;

            @Data
            public static class Http {
                @JacksonXmlProperty(localName = "send-file")
                private boolean sendfile = true;

                @JacksonXmlProperty(localName = "tcp-nopush")
                private boolean tcpNopush = true;

                @JacksonXmlProperty(localName = "tcp-nodelay")
                private boolean tcpNodelay = true;

                @JacksonXmlProperty(localName = "keepalive-timeout")
                private int keepaliveTimeout = 65;

                @JacksonXmlProperty(localName = "types-hash-max-size")
                private int typesHashMaxSize = 2048;

                @JacksonXmlProperty(localName = "http-include")
                private String httpInclude = "/etc/nginx/mime.types";

                @JacksonXmlProperty(localName = "default-type")
                private String defaultType = "application/octet-stream";

                @JacksonXmlProperty(localName = "ssl-protocols")
                private String sslProtocols = "TLSv1 TLSv1.1 TLSv1.2 TLSv1.3";

                @JacksonXmlProperty(localName = "ssl-prefer-server-ciphers")
                private boolean sslPreferServerCiphers = true;

                @JacksonXmlProperty(localName = "access-log")
                private String accessLog = "/var/log/nginx/access.log";

                @JacksonXmlProperty(localName = "error-log")
                private String errorLog = "/var/log/nginx/error.log";

                private boolean gzip = true;

                @JacksonXmlProperty(localName = "virtual-host-configs-include")
                private List<String> virtualHostConfigsInclude = new ArrayList<>(Arrays.asList(
                        "include /etc/nginx/conf.d/*.conf",
                        "include /etc/nginx/sites-enabled/*"
                ));

                @JacksonXmlElementWrapper(localName = "servers")
                @JacksonXmlProperty(localName = "server")
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
        public static class Redis {
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
