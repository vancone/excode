package com.vancone.devops.codegen.model.module.impl;

import com.vancone.devops.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HybridMobileAppModule extends Module {
    private List<String> platforms = new ArrayList<>();
}
