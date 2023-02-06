package com.xmingl.base.io;

import org.apache.maven.surefire.shade.org.apache.commons.io.Charsets;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 基础文件操作工具类<br/>
 * TODO 现在所有异常全部使用的原生，等待自定义异常完成后再进行处理
 *
 * @author XMINGL
 * @version 0.0.1 文件操作工具的基础版本
 */
public class FileUtil {

    public FileUtil(){
        super();
    }

    /**
     * 基础1KB的定义
     */
    public static final long ONE_KB = 1024;

    /**
     * 基础1KB的定义
     */
    public static final BigInteger ONE_KB_BI = BigInteger.valueOf(ONE_KB);

    /**
     * 基础1MB的定义
     */
    public static final long ONE_MB = ONE_KB * ONE_KB;

    /**
     * 基础1MB的定义
     */
    public static final BigInteger ONE_MB_BI = BigInteger.valueOf(ONE_MB);

    /**
     * 基础1GB的定义
     */
    public static final long ONE_GB = ONE_KB * ONE_MB;

    /**
     * 基础1GB的定义
     */
    public static final BigInteger ONE_GB_BI = BigInteger.valueOf(ONE_GB);

    /**
     * 基础1TB的定义
     */
    public static final long ONE_TB = ONE_KB * ONE_GB;

    /**
     * 基础1TB的定义
     */
    public static final BigInteger ONE_TB_BI = BigInteger.valueOf(ONE_TB);


    /**
     * 一个单纯的空数据
     */
    public static final File[] EMPTY_FILE_ARRAY = new File[0];

    /**
     * 基础文件编码格式：UTF-8
     */
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    /**
     * 临时文件基础路径参数
     */
    private static final String SYSTEM_TEMP_DIR_PARAM = "java.io.tmpdir";

    /**
     * 文件拷贝基础大小：30MB
     */
    private static final long FILE_COPY_BUFFER_SIZE = ONE_MB * 30;

    /**
     * 当前系统文件路径分隔符
     */
    public static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * 文件后缀
     */
    public static final char EXTENSION_SEPARATOR = '.';

    /**
     * UNIX分隔符
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * WINDOWS分隔符
     */
    private static final char WINDOWS_SEPARATOR = '\\';




    /**
     * 获取指定目录下的文件<br/>
     * dir：new File("C:\")<br/>
     * names: a, b, c<br/>
     * return new File("C:\a\b\c")<br/>
     * @param dir 目录
     * @param names 文件名
     * @return 文件
     */
    public static File getFile(File dir, String... names){
        if(dir == null){
            throw new NullPointerException("参数dir不能为空！");
        }
        if(names == null){
            throw new NullPointerException("参数names不能为空");
        }
        File base = dir;
        for (String name :
                names) {
            base = new File(base, name);
        }
        return base;
    }

    /**
     * 获取指定目录下的文件<br/>
     * names: a, b, c<br/>
     * return new File("a\b\c")<br/>
     * @param names 文件名
     * @return 文件
     */
    public static File getFile(String... names){
        if(names == null){
            throw new NullPointerException("参数names不能为空");
        }
        File base = null;
        for (String name :
                names) {
            if(base == null){
                base = new File(name);
            }else{
                base = new File(base, name);
            }
        }
        return base;
    }

    /**
     * 获取临时文件夹路径
     * @return 临时文件夹路径
     */
    public static String getTempDirectoryPath(){
        return System.getProperty(SYSTEM_TEMP_DIR_PARAM);
    }

    /**
     * 获取临时文件夹
     * @return 临时文件夹
     */
    public static File getTempDirectory(){
        return new File(getTempDirectoryPath());
    }

    /**
     * 获取用户文件夹路径
     * @return 用户文件夹路径
     */
    public static String getUserDirectoryPath() {
        return System.getProperty("user.home");
    }

    /**
     * 获取用户文件夹
     * @return 用户文件夹
     */
    public static File getUserDirectory() {
        return new File(getUserDirectoryPath());
    }

    /**
     * 打开文件输入流
     * @param file 文件
     * @return 文件输入流
     * @throws IOException 异常
     */
    public static FileInputStream openInputStream(File file) throws IOException{
        if(file.exists()){
            if(file.isDirectory()){
                throw new IOException("文件‘"+file+"’存在，但是为文件夹！");
            }
            if(!file.canRead()){
                throw new IOException("文件‘"+file+"’存在，但是不可读！");
            }
        }else{
            throw new IOException("文件‘"+file+"’不存在");
        }
        return new FileInputStream(file);
    }

    /**
     * 打开文件输出流
     * @param file 文件
     * @return 文件输出流
     * @throws IOException 异常
     */
    public static FileOutputStream openOutputStream(File file) throws IOException {
        return openOutputStream(file, false);
    }

    /**
     * 打开文件输出流
     * @param file 文件
     * @param append 是否追加
     * @return 文件输出流
     * @throws IOException 异常
     */
    public static FileOutputStream openOutputStream(File file, boolean append) throws IOException {
        if (file.exists()) {
            if(file.isDirectory()){
                throw new IOException("文件‘"+file+"’存在，但是为文件夹！");
            }
            if(!file.canWrite()){
                throw new IOException("文件‘"+file+"’存在，但是不可写！");
            }
        } else {
            File parent = file.getParentFile();
            if (parent != null) {
                if (!parent.mkdirs() && !parent.isDirectory()) {
                    throw new IOException("父文件夹’" + parent + "‘无法创建！");
                }
            }
        }
        return new FileOutputStream(file, append);
    }

    /**
     * 格式化字节数
     * @param size 字节
     * @return 格式化后的显示方式
     */
    public static String formatByteSize(BigInteger size){
        String formattedSize;
        if(size.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) > 0){
            formattedSize = String.valueOf(size.divide(ONE_TB_BI)) + "TB";
        }else if(size.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) > 0){
            formattedSize = String.valueOf(size.divide(ONE_GB_BI)) + "GB";
        }else if(size.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) > 0){
            formattedSize = String.valueOf(size.divide(ONE_MB_BI)) + "MB";
        }else if(size.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) > 0){
            formattedSize = String.valueOf(size.divide(ONE_KB_BI)) + "KB";
        }else{
            formattedSize = String.valueOf(size) + "B";
        }
        return formattedSize;
    }

    /**
     * 格式化字节数
     * @param size 字节
     * @return 格式化后的显示方式
     */
    public static String formatByteSize(long size){
        return formatByteSize(BigInteger.valueOf(size));
    }

    /**
     * 将集合转化为数组
     * @param files 文件集合
     * @return 文件数组
     */
    public static File[] convertCollectionToArray(Collection<File> files) {
        return files.toArray(new File[0]);
    }

    /**
     * 将文件类型转化为文件后缀
     * @param extensions 文件格式数组
     * @return 文件类型数组
     */
    public static String[] toSuffixes(String[] extensions) {
        String[] suffixes = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            suffixes[i] = "." + extensions[i];
        }
        return suffixes;
    }

    /**
     * 比较两个文件是否完全相同
     * @param file1 文件1
     * @param file2 文件2
     * @return 是否相同 true：相同，false：不同
     * @throws IOException 异常
     */
    public static boolean contentEquals(File file1, File file2) throws IOException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }
        if (!file1Exists) {
            return true;
        }
        if (file1.isDirectory() || file2.isDirectory()) {
            throw new IOException("非具体文件，无法比较");
        }
        if (file1.length() != file2.length()) {
            return false;
        }
        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            return true;
        }
        InputStream input1 = null;
        InputStream input2 = null;
        try {
            input1 = new FileInputStream(file1);
            input2 = new FileInputStream(file2);
            return IOUtil.contentEquals(input1, input2);
        } finally {
            IOUtil.closeQuietly(input1);
            IOUtil.closeQuietly(input2);
        }
    }

    /**
     * 比较两个文件是否相同，忽略终止符
     * @param file1 文件1
     * @param file2 文件2
     * @param charsetName 字符集
     * @return 是否相同
     * @throws IOException 异常
     */
    public static boolean contentEqualsIgnoreEOL(File file1, File file2, String charsetName) throws IOException {
        boolean file1Exists = file1.exists();
        if (file1Exists != file2.exists()) {
            return false;
        }
        if (!file1Exists) {
            return true;
        }
        if (file1.isDirectory() || file2.isDirectory()) {
            throw new IOException("非具体文件，无法比较");
        }
        if (file1.getCanonicalFile().equals(file2.getCanonicalFile())) {
            return true;
        }

        Reader input1 = null;
        Reader input2 = null;
        try {
            if (charsetName == null) {
                input1 = new InputStreamReader(new FileInputStream(file1));
                input2 = new InputStreamReader(new FileInputStream(file2));
            } else {
                input1 = new InputStreamReader(new FileInputStream(file1), charsetName);
                input2 = new InputStreamReader(new FileInputStream(file2), charsetName);
            }
            return IOUtil.contentEqualsIgnoreEOL(input1, input2);

        } finally {
            IOUtil.closeQuietly(input1);
            IOUtil.closeQuietly(input2);
        }
    }

    /**
     * 转文件
     * @param url 地址
     * @return 文件
     */
    public static File toFile(URL url) {
        if (url == null || !"file".equalsIgnoreCase(url.getProtocol())) {
            return null;
        } else {
            String filename = url.getFile().replace('/', File.separatorChar);
            filename = decodeUrl(filename);
            return new File(filename);
        }
    }

    /**
     * 批量转文件
     * @param urls 地址
     * @return 文件数组
     */
    public static File[] toFiles(URL[] urls) {
        if (urls == null || urls.length == 0) {
            return EMPTY_FILE_ARRAY;
        }
        File[] files = new File[urls.length];
        for (int i = 0; i < urls.length; i++) {
            URL url = urls[i];
            if (url != null) {
                if (!url.getProtocol().equals("file")) {
                    throw new IllegalArgumentException(
                            "URL不能获取文件: " + url);
                }
                files[i] = toFile(url);
            }
        }
        return files;
    }

    /**
     * 文件转URL
     * @param files 文件数组
     * @return URL数组
     * @throws IOException 异常
     */
    public static URL[] toURLs(File[] files) throws IOException {
        URL[] urls = new URL[files.length];
        for (int i = 0; i < urls.length; i++) {
            urls[i] = files[i].toURI().toURL();
        }
        return urls;
    }

    /**
     * 解析文件
     * @param url 路径
     * @return 路径
     */
    static String decodeUrl(String url) {
        String decoded = url;
        if (url != null && url.indexOf('%') >= 0) {
            int n = url.length();
            StringBuilder buffer = new StringBuilder();
            ByteBuffer bytes = ByteBuffer.allocate(n);
            for (int i = 0; i < n; ) {
                if (url.charAt(i) == '%') {
                    try {
                        do {
                            byte octet = (byte) Integer.parseInt(url.substring(i + 1, i + 3), 16);
                            bytes.put(octet);
                            i += 3;
                        } while (i < n && url.charAt(i) == '%');
                        continue;
                    } catch (RuntimeException e) {
                        // malformed percent-encoded octet, fall through and
                        // append characters literally
                    } finally {
                        if (bytes.position() > 0) {
                            bytes.flip();
                            buffer.append(UTF_8.decode(bytes).toString());
                            bytes.clear();
                        }
                    }
                }
                buffer.append(url.charAt(i++));
            }
            decoded = buffer.toString();
        }
        return decoded;
    }

    /**
     * 拷贝文件到指定路径
     * @param srcFile 文件
     * @param destDir 路径
     * @throws IOException 异常
     */
    public static void copyFileToDirectory(File srcFile, File destDir) throws IOException {
        copyFileToDirectory(srcFile, destDir, true);
    }

    /**
     * 拷贝文件至指定路径
     * @param srcFile 文件
     * @param destDir 路径
     * @param preserveFileDate 拷贝文件时间
     * @throws IOException 异常
     */
    public static void copyFileToDirectory(File srcFile, File destDir, boolean preserveFileDate) throws IOException {
        if (destDir == null) {
            throw new NullPointerException("目标地址不可以为空！");
        }
        if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("目标地址'" + destDir + "' 不是一个文件夹！");
        }
        File destFile = new File(destDir, srcFile.getName());
        copyFile(srcFile, destFile, preserveFileDate);
    }

    /**
     * 拷贝文件至指定路径
     * @param srcFile 文件
     * @param destFile 路径
     * @throws IOException 异常
     */
    public static void copyFile(File srcFile, File destFile) throws IOException {
        copyFile(srcFile, destFile, true);
    }

    /**
     * 拷贝文件至指定路径
     * @param srcFile 文件
     * @param destFile 路径
     * @param preserveFileDate 拷贝文件时间
     * @throws IOException 异常
     */
    public static void copyFile(File srcFile, File destFile,
                                boolean preserveFileDate) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("源文件不可以为空！");
        }
        if (destFile == null) {
            throw new NullPointerException("目标地址不可以为空！");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("源文件'" + srcFile + "' 不存在");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("源文件'" + srcFile + "' exists but is a directory");
        }
        if (srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            throw new IOException("源文件'" + srcFile + "' and 目标地址'" + destFile + "' are the same");
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                throw new IOException("目标地址'" + parentFile + "' directory cannot be created");
            }
        }
        if (destFile.exists() && !destFile.canWrite()) {
            throw new IOException("目标地址'" + destFile + "' exists but is read-only");
        }
        doCopyFile(srcFile, destFile, preserveFileDate);
    }

    /**
     * 拷贝文件至指定路径
     * @param srcFile 文件
     * @param destFile 路径
     * @param preserveFileDate 拷贝文件
     * @throws IOException 异常
     */
    private static void doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        if (destFile.exists() && destFile.isDirectory()) {
            throw new IOException("目标路径 '" + destFile + "' 存在，但是是一个文件夹！");
        }

        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count = 0;
            while (pos < size) {
                count = Math.min(size - pos, FILE_COPY_BUFFER_SIZE);
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            IOUtil.closeQuietly(output);
            IOUtil.closeQuietly(fos);
            IOUtil.closeQuietly(input);
            IOUtil.closeQuietly(fis);
        }

        if (srcFile.length() != destFile.length()) {
            throw new IOException("无法完整的拷贝文件从 '" +
                    srcFile + "' 到 '" + destFile + "'");
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
    }

    /**
     * 拷贝文件
     * @param input 文件
     * @param output 输出
     * @return 字节
     * @throws IOException 异常
     */
    public static long copyFile(File input, OutputStream output) throws IOException {
        try (FileInputStream fis = new FileInputStream(input)) {
            return IOUtil.copyLarge(fis, output);
        }
    }

    /**
     * 拷贝文件夹至指定路径
     * @param srcDir 文件夹
     * @param destDir 路径
     * @throws IOException 异常
     */
    public static void copyDirectoryToDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("源文件不可以为空！");
        }
        if (srcDir.exists() && !srcDir.isDirectory()) {
            throw new IllegalArgumentException("源文件'" + destDir + "' 不是一个文件夹！");
        }
        if (destDir == null) {
            throw new NullPointerException("目标地址不可以为空！");
        }
        if (destDir.exists() && !destDir.isDirectory()) {
            throw new IllegalArgumentException("目标地址'" + destDir + "' 不是一个文件夹！");
        }
        copyDirectory(srcDir, new File(destDir, srcDir.getName()), true);
    }

    /**
     * 拷贝文件夹至指定路径
     * @param srcDir 文件夹
     * @param destDir 路径
     * @throws IOException 异常
     */
    public static void copyDirectory(File srcDir, File destDir) throws IOException {
        copyDirectory(srcDir, destDir, true);
    }

    /**
     * 拷贝文件夹至指定路径
     * @param srcDir 文件夹
     * @param destDir 路径
     * @param preserveFileDate 拷贝文件修改时间
     * @throws IOException 异常
     */
    public static void copyDirectory(File srcDir, File destDir,
                                     boolean preserveFileDate) throws IOException {
        copyDirectory(srcDir, destDir, null, preserveFileDate);
    }

    /**
     * 拷贝文件夹至指定路径
     * @param srcDir 文件夹
     * @param destDir 路径
     * @param filter 过滤器
     * @throws IOException 异常
     */
    public static void copyDirectory(File srcDir, File destDir,
                                     FileFilter filter) throws IOException {
        copyDirectory(srcDir, destDir, filter, true);
    }

    /**
     * 拷贝文件夹至指定路径
     * @param srcDir 文件夹
     * @param destDir 路径
     * @param filter 过滤器
     * @param preserveFileDate 拷贝文件修改时间
     * @throws IOException 异常
     */
    public static void copyDirectory(File srcDir, File destDir,
                                     FileFilter filter, boolean preserveFileDate) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("源路径不可以为空！");
        }
        if (destDir == null) {
            throw new NullPointerException("目标路径不可以为空！");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("源路径 '" + srcDir + "' 不存在");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("源路径 '" + srcDir + "' 存在，但是非文件夹");
        }
        if (srcDir.getCanonicalPath().equals(destDir.getCanonicalPath())) {
            throw new IOException("源路径 '" + srcDir + "' 与目标路径 '" + destDir + "' 相同！");
        }

        List<String> exclusionList = null;
        if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
            File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
            if (srcFiles != null && srcFiles.length > 0) {
                exclusionList = new ArrayList<>(srcFiles.length);
                for (File srcFile : srcFiles) {
                    File copiedFile = new File(destDir, srcFile.getName());
                    exclusionList.add(copiedFile.getCanonicalPath());
                }
            }
        }
        doCopyDirectory(srcDir, destDir, filter, preserveFileDate, exclusionList);
    }

    /**
     * 拷贝文件夹至指定路径
     * @param srcDir 文件夹
     * @param destDir 路径
     * @param filter 过滤器
     * @param preserveFileDate 拷贝文件修改时间
     * @param exclusionList 忽略文件
     * @throws IOException 异常
     */
    private static void doCopyDirectory(File srcDir, File destDir, FileFilter filter,
                                        boolean preserveFileDate, List<String> exclusionList) throws IOException {
        // recurse
        File[] srcFiles = filter == null ? srcDir.listFiles() : srcDir.listFiles(filter);
        if (srcFiles == null) {
            throw new IOException("源路径为空：" + srcDir);
        }
        if (destDir.exists()) {
            if (!destDir.isDirectory()) {
                throw new IOException("目标路径 '" + destDir + "' 存在，但是非文件夹！");
            }
        } else {
            if (!destDir.mkdirs() && !destDir.isDirectory()) {
                throw new IOException("目标路径 '" + destDir + "' 无法被创建");
            }
        }
        if (!destDir.canWrite()) {
            throw new IOException("目标路径 '" + destDir + "' 无法写入");
        }
        for (File srcFile : srcFiles) {
            File dstFile = new File(destDir, srcFile.getName());
            if (exclusionList == null || !exclusionList.contains(srcFile.getCanonicalPath())) {
                if (srcFile.isDirectory()) {
                    doCopyDirectory(srcFile, dstFile, filter, preserveFileDate, exclusionList);
                } else {
                    doCopyFile(srcFile, dstFile, preserveFileDate);
                }
            }
        }
        if (preserveFileDate) {
            destDir.setLastModified(srcDir.lastModified());
        }
    }

    /**
     * 拷贝文件至指定路径
     * @param source 源文件
     * @param destination 指定路径
     * @throws IOException 异常
     */
    public static void copyURLToFile(URL source, File destination) throws IOException {
        InputStream input = source.openStream();
        copyInputStreamToFile(input, destination);
    }

    /**
     * 拷贝文件至指定路径
     * @param source 源文件
     * @param destination 指定路径
     * @param connectionTimeout 连接超时时间
     * @param readTimeout 读取超时时间
     * @throws IOException 异常
     */
    public static void copyURLToFile(URL source, File destination,
                                     int connectionTimeout, int readTimeout) throws IOException {
        URLConnection connection = source.openConnection();
        connection.setConnectTimeout(connectionTimeout);
        connection.setReadTimeout(readTimeout);
        InputStream input = connection.getInputStream();
        copyInputStreamToFile(input, destination);
    }

    /**
     * 拷贝流至指定路径
     * @param source 源文件
     * @param destination 指定路径
     * @throws IOException 异常
     */
    public static void copyInputStreamToFile(InputStream source, File destination) throws IOException {
        try {
            FileOutputStream output = openOutputStream(destination);
            try {
                IOUtil.copy(source, output);
                output.close();
            } finally {
                IOUtil.closeQuietly(output);
            }
        } finally {
            IOUtil.closeQuietly(source);
        }
    }

    /**
     * 删除文件夹下所有文件，保留文件夹本身
     * @param directory 文件夹
     * @throws IOException 异常
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    /**
     * 快速删除文件，如果是文件夹则会删除文件夹及文件夹下的所有文件
     * @param file 路径
     * @return 结果
     */
    public static boolean deleteQuietly(File file) {
        if (file == null) {
            return false;
        }
        try {
            if (file.isDirectory()) {
                cleanDirectory(file);
            }
        } catch (Exception ignored) {
        }

        try {
            return file.delete();
        } catch (Exception ignored) {
            return false;
        }
    }

    /**
     * 清空文件夹下的文件，保留当前文件夹
     * @param directory 指定路径
     * @throws IOException 异常
     */
    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " 不存在";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " 不是一个文件夹！";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * 文件是否为符号连接
     * @param file 文件
     * @return 结果
     * @throws IOException 异常
     */
    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (SYSTEM_SEPARATOR == WINDOWS_SEPARATOR) {
            return false;
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        return !fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile());
    }

    /**
     * 读取文件转为字符串
     * @param file 文件
     * @param encoding 编码
     * @return 字符串
     * @throws IOException 异常
     */
    public static String readFileToString(File file, Charset encoding) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtil.toString(in, Charsets.toCharset(encoding));
        } finally {
            IOUtil.closeQuietly(in);
        }
    }

    /**
     * 读取文件转为字符串
     * @param file 文件
     * @param encoding 编码
     * @return 字符串
     * @throws IOException 异常
     */
    public static String readFileToString(File file, String encoding) throws IOException {
        return readFileToString(file, Charsets.toCharset(encoding));
    }

    /**
     * 读取文件转为字符串
     * @param file 文件
     * @return 字符串
     * @throws IOException 异常
     */
    public static String readFileToString(File file) throws IOException {
        return readFileToString(file, UTF_8);
    }

    /**
     * Convert the specified CharSequence to an input stream, encoded as bytes
     * using the default character encoding of the platform.
     *
     * @param input the CharSequence to convert
     * @return an input stream
     * @since 2.0
     */
    public static InputStream toInputStream(CharSequence input) {
        return toInputStream(input, Charset.defaultCharset());
    }

    /**
     * Convert the specified CharSequence to an input stream, encoded as bytes
     * using the specified character encoding.
     *
     * @param input    the CharSequence to convert
     * @param encoding the encoding to use, null means platform default
     * @return an input stream
     * @since 2.3
     */
    public static InputStream toInputStream(CharSequence input, Charset encoding) {
        return toInputStream(input.toString(), encoding);
    }

    /**
     * 转输入流
     * @param input 输入
     * @param encoding 字符集
     * @return 输入流
     * @throws IOException 异常
     */
    public static InputStream toInputStream(CharSequence input, String encoding) throws IOException {
        return toInputStream(input, Charsets.toCharset(encoding));
    }

    /**
     * 转输入流
     * @param input 字符串
     * @return 输入流
     */
    public static InputStream toInputStream(String input) {
        return toInputStream(input, UTF_8);
    }

    /**
     * 转输入流
     * @param input 字符串
     * @param encoding 字符集
     * @return 输入流
     */
    public static InputStream toInputStream(String input, Charset encoding) {
        return new ByteArrayInputStream(getBytes(input, Charsets.toCharset(encoding)));
    }

    /**
     * 转输入流
     * @param input 字符串
     * @param encoding 字符集
     * @return 输入流
     */
    public static InputStream toInputStream(String input, String encoding) throws IOException {
        byte[] bytes = getBytes(input,Charsets.toCharset(encoding));
        return new ByteArrayInputStream(bytes);
    }

    /**
     * 字符串转字节数组
     * @param src 字符串
     * @param charSet 字符集
     * @return 字节数组
     */
    public static byte[] getBytes(String src, Charset charSet) {
        return src.getBytes(charSet);
    }

    /**
     * 字符串转字节数组
     * @param src 字符串
     * @return 字节数组
     */
    public static byte[] getBytes(String src) {
        return getBytes(src, UTF_8);
    }

    /**
     * 文件转字节数组
     * @param file 文件
     * @return 字节数组
     * @throws IOException 异常
     */
    public static byte[] readFileToByteArray(File file) throws IOException {
        InputStream in = null;
        try {
            in = openInputStream(file);
            return IOUtil.toByteArray(in, file.length());
        } finally {
            IOUtil.closeQuietly(in);
        }
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @throws IOException 异常
     */
    public static void writeStringToFile(File file, String data, Charset encoding) throws IOException {
        writeStringToFile(file, data, encoding, false);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @throws IOException 异常
     */
    public static void writeStringToFile(File file, String data, String encoding) throws IOException {
        writeStringToFile(file, data, encoding, false);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @param append 追加
     * @throws IOException 异常
     */
    public static void writeStringToFile(File file, String data, Charset encoding, boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            IOUtil.write(data, out, encoding);
            out.close(); // don't swallow close Exception if copy completes normally
        } finally {
            IOUtil.closeQuietly(out);
        }
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @param append 追加
     * @throws IOException 异常
     */
    public static void writeStringToFile(File file, String data, String encoding, boolean append) throws IOException {
        writeStringToFile(file, data, Charsets.toCharset(encoding), append);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @throws IOException 异常
     */
    public static void writeStringToFile(File file, String data) throws IOException {
        writeStringToFile(file, data, Charset.defaultCharset(), false);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param append 追加
     * @throws IOException 异常
     */
    public static void writeStringToFile(File file, String data, boolean append) throws IOException {
        writeStringToFile(file, data, Charset.defaultCharset(), append);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @throws IOException 异常
     */
    public static void write(File file, CharSequence data) throws IOException {
        write(file, data, Charset.defaultCharset(), false);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param append 追加
     * @throws IOException 异常
     */
    public static void write(File file, CharSequence data, boolean append) throws IOException {
        write(file, data, Charset.defaultCharset(), append);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @throws IOException 异常
     */
    public static void write(File file, CharSequence data, Charset encoding) throws IOException {
        write(file, data, encoding, false);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @throws IOException 异常
     */
    public static void write(File file, CharSequence data, String encoding) throws IOException {
        write(file, data, encoding, false);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @param append 追加
     * @throws IOException 异常
     */
    public static void write(File file, CharSequence data, Charset encoding, boolean append) throws IOException {
        String str = data == null ? null : data.toString();
        writeStringToFile(file, str, encoding, append);
    }

    /**
     * 写入文件
     * @param file 文件
     * @param data 数据
     * @param encoding 编目
     * @param append 追加
     * @throws IOException 异常
     */
    public static void write(File file, CharSequence data, String encoding, boolean append) throws IOException {
        write(file, data, Charsets.toCharset(encoding), append);
    }

    /**
     * 写入行
     * @param file 文件
     * @param data 字节
     * @throws IOException 异常
     */
    public static void writeByteArrayToFile(File file, byte[] data) throws IOException {
        writeByteArrayToFile(file, data, false);
    }

    /**
     * 写入行
     * @param file 文件
     * @param data 字节
     * @param append 是否追加
     * @throws IOException 异常
     */
    public static void writeByteArrayToFile(File file, byte[] data, boolean append) throws IOException {
        OutputStream out = null;
        try {
            out = openOutputStream(file, append);
            out.write(data);
            out.close(); 
        } finally {
            IOUtil.closeQuietly(out);
        }
    }

    /**
     * 写入行
     * @param file 文件
     * @param encoding 字符集
     * @param lines 行
     * @throws IOException 异常
     */
    public static void writeLines(File file, String encoding, Collection<?> lines) throws IOException {
        writeLines(file, encoding, lines, null, false);
    }

    /**
     * 写入行
     * @param file 文件
     * @param encoding 字符集
     * @param lines 行
     * @param append 是否追加
     * @throws IOException 异常
     */
    public static void writeLines(File file, String encoding, Collection<?> lines, boolean append) throws IOException {
        writeLines(file, encoding, lines, null, append);
    }

    /**
     * 写入行
     * @param file 文件
     * @param lines 行
     * @throws IOException 异常
     */
    public static void writeLines(File file, Collection<?> lines) throws IOException {
        writeLines(file, null, lines, null, false);
    }

    /**
     * 写入行
     * @param file 文件
     * @param lines 行
     * @param append 是否追加
     * @throws IOException 异常
     */
    public static void writeLines(File file, Collection<?> lines, boolean append) throws IOException {
        writeLines(file, null, lines, null, append);
    }

    /**
     * 写入行
     * @param file 文件
     * @param lines 行
     * @param lineEnding 字符集
     * @throws IOException 异常
     */
    public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding)
            throws IOException {
        writeLines(file, encoding, lines, lineEnding, false);
    }

    /**
     * 写入行
     * @param file 文件
     * @param encoding 字符集
     * @param lines 行
     * @param lineEnding 行字符集
     * @param append 是否追加
     * @throws IOException 异常
     */
    public static void writeLines(File file, String encoding, Collection<?> lines, String lineEnding, boolean append)
            throws IOException {
        FileOutputStream out = null;
        try {
            out = openOutputStream(file, append);
            final BufferedOutputStream buffer = new BufferedOutputStream(out);
            IOUtil.writeLines(lines, lineEnding, buffer, encoding);
            buffer.flush();
            out.close();
        } finally {
            IOUtil.closeQuietly(out);
        }
    }

    /**
     * 写入行
     * @param file 文件
     * @param lines 行
     * @param lineEnding 字符集
     * @throws IOException 异常
     */
    public static void writeLines(File file, Collection<?> lines, String lineEnding) throws IOException {
        writeLines(file, null, lines, lineEnding, false);
    }

    /**
     * 写入行
     * @param file 文件
     * @param lines 行
     * @param lineEnding 字符集
     * @param append 是否追加
     * @throws IOException 异常
     */
    public static void writeLines(File file, Collection<?> lines, String lineEnding, boolean append)
            throws IOException {
        writeLines(file, null, lines, lineEnding, append);
    }

    /**
     * 强制删除文件或文件夹
     * @param file 文件或文件夹
     * @throws IOException 异常
     */
    public static void forceDelete(File file) throws IOException {
        if(file == null){
            throw new NullPointerException("文件或文件夹不可以为空！");
        }
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("文件不存在: " + file);
                }
                String message =
                        "无法删除文件: " + file;
                throw new IOException(message);
            }
        }
    }

    /**
     * 结束使用时强制删除文件或文件夹
     * @param file 文件或文件夹
     * @throws IOException 异常
     */
    public static void forceDeleteOnExit(File file) throws IOException {
        if(file == null){
            throw new NullPointerException("文件或文件夹不可以为空！");
        }
        if (file.isDirectory()) {
            deleteDirectoryOnExit(file);
        } else {
            file.deleteOnExit();
        }
    }

    /**
     * 结束使用时，删除文件夹
     * @param directory 文件夹
     * @throws IOException 异常
     */
    private static void deleteDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        directory.deleteOnExit();
        if (!isSymlink(directory)) {
            cleanDirectoryOnExit(directory);
        }
    }

    /**
     * 结束使用时，清空文件夹
     * @param directory 文件夹
     * @throws IOException 异常
     */
    private static void cleanDirectoryOnExit(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " 不存在";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " 不是一个文件夹！";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDeleteOnExit(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    /**
     * 强制创建一个文件夹
     * @param directory 文件夹
     * @throws IOException 异常
     */
    public static void forceMkdir(File directory) throws IOException {
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                String message =
                        "文件 "
                                + directory
                                + " 存在，但是不是一个文件夹，无法创建文件夹！";
                throw new IOException(message);
            }
        } else {
            if (!directory.mkdirs()) {
                // Double-check that some other thread or process hasn't made
                // the directory in the background
                if (!directory.isDirectory()) {
                    String message =
                            "无法创建文件夹： " + directory;
                    throw new IOException(message);
                }
            }
        }
    }

    public static long sizeOf(File file) {

        if (!file.exists()) {
            String message = file + " 不存在";
            throw new IllegalArgumentException(message);
        }

        if (file.isDirectory()) {
            return sizeOfDirectory(file);
        } else {
            return file.length();
        }

    }
    
    public static BigInteger sizeOfAsBigInteger(File file) {

        if (!file.exists()) {
            String message = file + " 不存在";
            throw new IllegalArgumentException(message);
        }

        if (file.isDirectory()) {
            return sizeOfDirectoryAsBigInteger(file);
        } else {
            return BigInteger.valueOf(file.length());
        }

    }

    public static long sizeOfDirectory(File directory) {
        checkDirectory(directory);

        final File[] files = directory.listFiles();
        if (files == null) {
            return 0L;
        }
        long size = 0;

        for (final File file : files) {
            try {
                if (!isSymlink(file)) {
                    size += sizeOf(file);
                    if (size < 0) {
                        break;
                    }
                }
            } catch (IOException ignored) {
            }
        }

        return size;
    }

    public static BigInteger sizeOfDirectoryAsBigInteger(File directory) {
        checkDirectory(directory);

        final File[] files = directory.listFiles();
        if (files == null) {  
            return BigInteger.ZERO;
        }
        BigInteger size = BigInteger.ZERO;

        for (final File file : files) {
            try {
                if (!isSymlink(file)) {
                    size = size.add(BigInteger.valueOf(sizeOf(file)));
                }
            } catch (IOException ignored) {
            }
        }

        return size;
    }

    private static void checkDirectory(File directory) {
        if(directory == null){
            throw new NullPointerException("参数不可以为空");
        }
        if (!directory.exists()) {
            throw new IllegalArgumentException(directory + " 不存在");
        }
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory + " 不是文件夹");
        }
    }

    public static void moveDirectory(File srcDir, File destDir) throws IOException {
        if (srcDir == null) {
            throw new NullPointerException("源文件不可以为空！");
        }
        if (destDir == null) {
            throw new NullPointerException("目标地址不可以为空！");
        }
        if (!srcDir.exists()) {
            throw new FileNotFoundException("源文件'" + srcDir + "' 不存在！");
        }
        if (!srcDir.isDirectory()) {
            throw new IOException("源文件'" + srcDir + "' 不是一个文件夹！");
        }
        if (destDir.exists()) {
            throw new IOException("目标地址'" + destDir + "' 已存在！");
        }
        boolean rename = srcDir.renameTo(destDir);
        if (!rename) {
            if (destDir.getCanonicalPath().startsWith(srcDir.getCanonicalPath())) {
                throw new IOException("无法移动文件夹: " + srcDir + " 移动到它的子文件夹: " + destDir);
            }
            copyDirectory(srcDir, destDir);
            deleteDirectory(srcDir);
            if (srcDir.exists()) {
                throw new IOException("无法删除原文件 '" + srcDir);
            }
        }
    }

    public static void moveDirectoryToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        checkSrcAndDirFile(src, destDir, createDestDir);
        moveDirectory(src, new File(destDir, src.getName()));

    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("源文件不可以为空！");
        }
        if (destFile == null) {
            throw new NullPointerException("目标地址不可以为空！");
        }
        if (!srcFile.exists()) {
            throw new FileNotFoundException("源文件'" + srcFile + "' 不存在！");
        }
        if (srcFile.isDirectory()) {
            throw new IOException("源文件'" + srcFile + "' is a directory");
        }
        if (destFile.exists()) {
            throw new IOException("目标地址'" + destFile + "' 已存在！");
        }
        if (destFile.isDirectory()) {
            throw new IOException("目标地址'" + destFile + "' is a directory");
        }
        boolean rename = srcFile.renameTo(destFile);
        if (!rename) {
            copyFile(srcFile, destFile);
            if (!srcFile.delete()) {
                deleteQuietly(destFile);
                throw new IOException("Failed to delete original file '" + srcFile +
                        "' after copy to '" + destFile + "'");
            }
        }
    }

    public static void moveFileToDirectory(File srcFile, File destDir, boolean createDestDir) throws IOException {
        checkSrcAndDirFile(srcFile, destDir, createDestDir);
        moveFile(srcFile, new File(destDir, srcFile.getName()));
    }

    private static void checkSrcAndDirFile(File srcFile, File destDir, boolean createDestDir) throws IOException {
        if (srcFile == null) {
            throw new NullPointerException("源文件不可以为空！");
        }
        if (destDir == null) {
            throw new NullPointerException("目标地址不可以为空！");
        }
        if (!destDir.exists() && createDestDir) {
            destDir.mkdirs();
        }
        if (!destDir.exists()) {
            throw new FileNotFoundException("目标地址directory '" + destDir +
                    "' 不存在！ [createDestDir=" + createDestDir + "]");
        }
        if (!destDir.isDirectory()) {
            throw new IOException("目标地址'" + destDir + "' 不是一个文件夹！");
        }
    }

    public static void moveToDirectory(File src, File destDir, boolean createDestDir) throws IOException {
        if (src == null) {
            throw new NullPointerException("源文件不可以为空！");
        }
        if (destDir == null) {
            throw new NullPointerException("目标地址不可以为空！");
        }
        if (!src.exists()) {
            throw new FileNotFoundException("源文件'" + src + "' 不存在！");
        }
        if (src.isDirectory()) {
            moveDirectoryToDirectory(src, destDir, createDestDir);
        } else {
            moveFileToDirectory(src, destDir, createDestDir);
        }
    }

    public static void listFiles(File file, List<File> files) {
        if(!file.exists()){
            return;
        }
        if(file.isDirectory()){
            for (File f :
                    file.listFiles()) {
                listFiles(f, files);
            }
        }else{
            files.add(file);
        }
    }
}
