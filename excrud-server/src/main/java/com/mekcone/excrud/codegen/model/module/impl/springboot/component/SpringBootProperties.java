package com.mekcone.excrud.codegen.model.module.impl.springboot.component;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpringBootProperties {
    // Basic properties
    @JacksonXmlProperty(localName = "application-name")
    private String applicationName;

    @JacksonXmlProperty(localName = "page-size")
    private int pageSize;

    @JacksonXmlProperty(localName = "server-port")
    private int serverPort;

    // Extended properties
    @JacksonXmlProperty(localName = "cross-origin")
    private CrossOrigin crossOrigin;

    @JacksonXmlProperty(localName = "mwp-account")
    private MekConeWebPlatformAccount mekConeWebPlatformAccount;

    @Data
    public class CrossOrigin {

        @JacksonXmlElementWrapper(localName = "allowed-headers")
        @JacksonXmlProperty(localName = "allowed-header")
        private List<String> allowedHeaders = new ArrayList<>();

        @JacksonXmlElementWrapper(localName = "allowed-methods")
        @JacksonXmlProperty(localName = "allowed-method")
        private List<String> allowedMethods = new ArrayList<>();

        @JacksonXmlElementWrapper(localName = "allowed-origins")
        @JacksonXmlProperty(localName = "allowed-origin")
        private List<String> allowedOrigins = new ArrayList<>();
    }

    @Data
    public class MekConeWebPlatformAccount {

        @JacksonXmlProperty(localName = "test-account")
        private TestAccount testAccount = new TestAccount();

        @Data
        public class TestAccount {
            @JacksonXmlProperty(isAttribute = true)
            private String username;

            @JacksonXmlProperty(isAttribute = true)
            private String password;

            @JacksonXmlProperty(isAttribute = true)
            private String role;

            @JacksonXmlProperty(isAttribute = true)
            private String profile;
        }
    }
}
