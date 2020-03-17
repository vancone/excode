package com.mekcone.excrud.service;

import com.mekcone.excrud.model.Settings;

public interface SettingService {
     Settings getSettings();
     boolean initialize();
     boolean load();
     boolean save();
}
