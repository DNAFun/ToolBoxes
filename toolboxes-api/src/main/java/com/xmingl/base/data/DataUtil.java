package com.xmingl.base.data;

import com.alibaba.fastjson.JSON;
import com.xmingl.SystemBaseInfo;
import com.xmingl.base.io.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO 初版制定计划为JSON数据格式类型，打算考虑后期整改为多种数据类型全部支持
 * @author XMINGL
 * @version 0.0.1 基础的数据读写工具
 */
public class DataUtil {

    public static String BASE_FILE_PATH = FileUtil.getUserDirectoryPath()+FileUtil.SYSTEM_SEPARATOR+ SystemBaseInfo.PROJECT_NAME;

    /**
     * 获取数据
     *
     * @param filePath 相对路径
     * @return 字符串
     * @throws IOException 异常
     */
    public static String loadDataString(File filePath) throws IOException {
        return FileUtil.readFileToString(filePath);
    }

    /**
     * 获取数据
     *
     * @param filePath 相对路径
     * @return 字符串
     * @throws IOException 异常
     */
    public static String loadDataString(String filePath) throws IOException {
        return loadDataString(new File(BASE_FILE_PATH,filePath));
    }

    /**
     * 获取数据集合
     * @param filePath 相对路径
     * @param clazz 类型
     * @return 数据集合
     * @throws IOException 异常
     */
    public static <T> List<T> loadDataList(String filePath, Class<T> clazz) throws IOException {
        List<T> data = new ArrayList<>();
        List<File> files = new ArrayList<>();
        FileUtil.listFiles(new File(BASE_FILE_PATH,filePath),files);
        for (File f : files) {
            String dataString = loadDataString(new File(BASE_FILE_PATH+filePath+FileUtil.SYSTEM_SEPARATOR+f.getName()));
            data.add(JSON.parseObject(dataString, clazz));
        }
        return data;
    }

    /**
     * 获取数据量
     * @param filePath
     * @return
     * @throws IOException
     */
    public static int listDataNum(String filePath) throws IOException{
        List<File> files = new ArrayList<>();
        FileUtil.listFiles(new File(BASE_FILE_PATH,filePath),files);
        return files.size();
    }

    /**
     * 获取数据集合
     * @param filePath 相对路径
     * @return 数据集合
     * @throws IOException 异常
     */
    public static List<?> loadDataList(String filePath) throws IOException {
        return loadDataList(filePath, Object.class);
    }

    /**
     * 获取数据对象
     * @param clazz 类型
     * @param filePath 相对路径
     * @return 对象
     * @throws IOException 异常
     */
    public static Class<?> loadDataObject(Class<?> clazz, String filePath) throws IOException{
        String dataString = loadDataString(filePath);
        return (Class<?>) JSON.parseObject(dataString, clazz);
    }

}
