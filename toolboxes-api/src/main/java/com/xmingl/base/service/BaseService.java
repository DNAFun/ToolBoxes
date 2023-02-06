package com.xmingl.base.service;

import com.xmingl.base.io.FileUtil;

import java.util.Locale;

public abstract class BaseService {

    public static String BASE_FILE_PATH;

    private static final String SERVICE_SUFFIX = "ServiceImpl";

    public BaseService() {
        BASE_FILE_PATH = FileUtil.SYSTEM_SEPARATOR
                            + getClass()
                                .getName()
                                .replace(SERVICE_SUFFIX, "")
                                .replace(FileUtil.EXTENSION_SEPARATOR, FileUtil.SYSTEM_SEPARATOR)
                            + FileUtil.SYSTEM_SEPARATOR;
        BASE_FILE_PATH = BASE_FILE_PATH.toLowerCase(Locale.ROOT);
    }

    /**
     * 获取文件存储地址，首尾无文件分隔符
     *
     * @return 文件存储地址
     */
    public static String getBaseFilePath() {
        return BASE_FILE_PATH;
    }
}
