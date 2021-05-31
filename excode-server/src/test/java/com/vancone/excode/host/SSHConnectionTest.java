package com.vancone.excode.host;

import com.vancone.excode.entity.DTO.Host;
import com.vancone.excode.util.SessionManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class SSHConnectionTest {
    @Test
    void connect() {
        Host host = new Host();
        host.setIpAddress("10.10.10.1");
        host.setSshUsername("mekcone");
        host.setSshPassword("123456");
        SessionManager sessionManager = new SessionManager(host);
//        log.info("OS Type: {}", sessionManager.getOSType());
//        log.info("Process List: \n{}", sessionManager.getProcessList());
        log.info("Status: {}", host.testConnection());
    }
}
