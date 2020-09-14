package com.mekcone.studio.host.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

/*
 * Author: Tenton Lien
 */

@Slf4j
@Data
public class Host implements Serializable {
    private String id;
    private String name;
    private String ipAddress;
    private String status = "off";

    // SSH params
    private int sshPort = 22;
    private String sshUsername = "root";
    private String sshPassword;

    private String osVersion;

    public boolean testConnection() {
        int  timeOut = 3000;  // Timeout after 3 seconds
        try {
            log.info("ping " + ipAddress);
            return InetAddress.getByName(ipAddress).isReachable(timeOut);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
