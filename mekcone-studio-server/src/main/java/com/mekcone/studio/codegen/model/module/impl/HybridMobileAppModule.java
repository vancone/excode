package com.mekcone.studio.codegen.model.module.impl;

import com.mekcone.studio.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HybridMobileAppModule extends Module {
    private List<String> platforms = new ArrayList<>();
}
