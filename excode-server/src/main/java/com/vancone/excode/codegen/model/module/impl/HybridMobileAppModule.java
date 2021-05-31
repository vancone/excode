package com.vancone.excode.codegen.model.module.impl;

import com.vancone.excode.codegen.model.module.Module;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class HybridMobileAppModule extends Module {
    private List<String> platforms = new ArrayList<>();
}
