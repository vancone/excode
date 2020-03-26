package com.mekcone.excrud.util;

import com.mekcone.excrud.annotation.CommandMapping;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CmdUtil {

    private static List<CommandMappingItem> commandMappingItems = new ArrayList<>();

    @Data
    static class CommandMappingItem {
        private String cmd;
        private Object bean;
        private Method method;

        public CommandMappingItem(String cmd, Object bean, Method method) {
            this.cmd = cmd;
            this.bean = bean;
            this.method = method;
        }
    }

    public static void execute(String cmd) {
        for (CommandMappingItem commandMappingItem: commandMappingItems) {
            if (commandMappingItem.getCmd().equals(cmd)) {
                Method method = commandMappingItem.getMethod();
                try {
                    method.invoke(commandMappingItem.getBean(), new Object[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                    //LogUtil.error(ex.getMessage());
                }
            }
        }
    }

    public static void printCommandMappings() {
        for (CommandMappingItem commandMappingItem: commandMappingItems) {
            System.out.println(commandMappingItem.getCmd() + " -> " + commandMappingItem.getMethod().getName() + "\n");
        }
    }

    public static void registerController(ApplicationContext applicationContext) {
        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName: beanNames) {
            Object bean = applicationContext.getBean(beanName);
            Controller controller = bean.getClass().getDeclaredAnnotation(Controller.class);
            if (controller != null) {
                Method[] methods = bean.getClass().getMethods();
                for (Method method: methods) {
                    CommandMapping commandMapping = method.getDeclaredAnnotation(CommandMapping.class);
                    if (commandMapping != null) {
                        commandMappingItems.add(new CommandMappingItem(commandMapping.value(), bean, method));
                    }
                }
            }
        }
    }

}
