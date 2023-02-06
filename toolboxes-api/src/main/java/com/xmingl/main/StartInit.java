package com.xmingl.main;

import com.xmingl.SystemBaseInfo;
import com.xmingl.base.anno.AutoConfModule;
import com.xmingl.base.io.FileUtil;
import com.xmingl.base.service.BaseService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

@Component
public class StartInit {
    public static Logger logger = LoggerFactory.getLogger(StartInit.class);

    private static final String BASE_FILE_PATH = "BASE_FILE_PATH";

    @PostConstruct
    public void init() {
        logger.info("=============================== StartInit  start");
        // 获取系统需要自动管理的模块service
        Set<Class<?>> autoConfModules = getAutoConfModules();
        logger.info("StartInit autoConfModules：{}", autoConfModules.size());
        logger.info("StartInit create system dir start");
        // 创建系统所需文件夹
        createDir(autoConfModules);
        logger.info("StartInit create system dir finish");
        logger.info("=============================== StartInit  end");
    }


    private Set<Class<?>> getAutoConfModules() {
        Set<Class<?>> classes = new HashSet<>();
        String name = this.getClass().getName();
        String[] packages = name.split("\\" + FileUtil.EXTENSION_SEPARATOR);
        if (packages.length < 2) {
            return classes;
        }
        name = packages[0] + FileUtil.EXTENSION_SEPARATOR + packages[1];
        Reflections ref = new Reflections(name);
        classes = ref.getTypesAnnotatedWith(AutoConfModule.class);
        return classes;
    }

    /**
     * 创建系统所需文件夹
     *
     * @param autoConfModules 使用注解的类
     */
    private void createDir(Set<Class<?>> autoConfModules) {
        boolean successCreateFileDir = true;
        File projectDirectory = new File(FileUtil.getUserDirectory(), SystemBaseInfo.PROJECT_NAME);
        logger.info("project datafile save directory: {}", projectDirectory.getAbsolutePath());
        for (Class<?> clazz : autoConfModules) {
            if (clazz.getSuperclass() != BaseService.class) {
                logger.error("classname: {} , reason: {}", clazz.getName(), "非继承BaseService");
                successCreateFileDir = false;
                continue;
            }
            try {
                Constructor<?> constructor = clazz.getConstructor();
                Object newInstance = constructor.newInstance();
                String path = clazz.getField(BASE_FILE_PATH).get(newInstance).toString();
                File clazzFile = new File(projectDirectory, path);
                if (clazzFile.exists()) {
                    continue;
                }
                FileUtil.forceMkdir(clazzFile);
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchFieldException | IOException e) {
                logger.error("classname: {} , reason: {}", clazz.getName(), e.getMessage());
                successCreateFileDir = false;
            }
        }
        if (!successCreateFileDir) {
            logger.error("System start error!");
            System.exit(-1);
        }

    }


}

