package com.mekcone.excrud;

import com.mekcone.excrud.config.DefaultConfig;
import com.mekcone.excrud.util.CmdUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {
    public static void main(String[] args) {
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext(DefaultConfig.class);
        CmdUtil.registerController(applicationContext);

        if (args.length > 0) {
            CmdUtil.execute(args[0]);
        }
    }
}