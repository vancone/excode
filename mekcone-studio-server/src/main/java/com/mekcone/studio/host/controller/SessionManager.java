package com.mekcone.studio.host.controller;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.mekcone.studio.constant.ModuleConstant;
import com.mekcone.studio.host.entity.Host;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

/*
 * Author: Tenton Lien
 * Date: 6/27/2020
 */

@Slf4j
public class SessionManager {

    private com.jcraft.jsch.Session session;

    public SessionManager(Host host) {
        JSch jsch = new JSch();

        try {
            session = jsch.getSession(host.getSshUsername(), host.getIpAddress(), host.getSshPort());
            session.setPassword(host.getSshPassword());
            session.setConfig("StrictHostKeyChecking", "no");  // 第一次访问服务器时不用输入yes
            session.setServerAliveCountMax(0);
            session.connect();
        } catch (JSchException e) {
            log.error(e.getMessage());
        }
    }

    public String execute(String command) {
        try {
            ChannelExec channelExec = channelExec = (ChannelExec) session.openChannel("exec");
            InputStream inputStream = channelExec.getInputStream();
            channelExec.setCommand(command);
            channelExec.setErrStream(System.err);
            channelExec.connect();

            return IOUtils.toString(inputStream);
        } catch (JSchException | IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public String getOSType() {
        String result = execute("cat /etc/os-release");
        if (result != null && result.contains(ModuleConstant.DEPLOYMENT_OPERATING_SYSTEM_UBUNTU)) {
            return ModuleConstant.DEPLOYMENT_OPERATING_SYSTEM_UBUNTU;
        } else if (result != null && result.contains(ModuleConstant.DEPLOYMENT_OPERATING_SYSTEM_CENTOS)) {
            return ModuleConstant.DEPLOYMENT_OPERATING_SYSTEM_CENTOS;
        } else {
            return "other";
        }
    }

    public String getProcessList() {
        return execute("ps -ax");
    }
}
