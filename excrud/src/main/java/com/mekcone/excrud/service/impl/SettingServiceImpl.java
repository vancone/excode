package com.mekcone.excrud.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.excrud.model.Settings;
import com.mekcone.excrud.service.SettingService;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.KeyPair;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private Settings settings;

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    private String configJsonPath = PathUtil.getProgramPath() + "config.json";

    @Override
    public boolean initialize() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        String rsaPublicKey = Base64.encode((pair.getPublic().getEncoded()));
        String rsaPrivateKey = Base64.encode(pair.getPrivate().getEncoded());
        settings.addRsaKeyPair(rsaPublicKey, rsaPrivateKey);
        save();
        return false;
    }

    @Override
    public boolean load() {
        String data = FileUtil.read(configJsonPath);
        if (data != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                settings = objectMapper.readValue(data, Settings.class);
            } catch (Exception ex) {
                LogUtil.warn(ex.getMessage());
                return false;
            }
        } else {
            initialize();
        }
        return true;
    }

    @Override
    public boolean save() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String settingModelText = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(settings);
            return FileUtil.write(configJsonPath, settingModelText);
        } catch (JsonProcessingException e) {
            LogUtil.warn(e.getMessage());
            return false;
        }
    }
}
