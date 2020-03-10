package com.mekcone.excrud.service;

import com.mekcone.excrud.model.SettingModel;

public interface SettingService {
     SettingModel getSettingModel();
     boolean initialize();
     boolean load();
     boolean save();
}
