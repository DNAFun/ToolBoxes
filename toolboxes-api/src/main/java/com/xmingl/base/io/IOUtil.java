package com.xmingl.base.io;

import org.apache.maven.surefire.shade.org.apache.commons.io.Charsets;

import java.io.*;
import java.net.*;
import java.nio.channels.Selector;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Objects;

public class IOUtil {

    private static final int    EOF                    = -1;
    public static final  char   DIR_SEPARATOR_UNIX     = '/';
    public static final  char   DIR_SEPARATOR_WINDOWS  = '\\';
    public static final  char   DIR_SEPARATOR          = File.separatorChar;
    public static final  String LINE_SEPARATOR_UNIX    = "\n";
    public static final  String LINE_SEPARATOR_WINDOWS = "\r\n";
    public static final String LINE_SEPARATOR;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;
    private static final int SKIP_BUFFER_SIZE = 2048;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    private static char[] SKIP_CHAR_BUFFER;
    private static byte[] SKIP_BYTE_BUFFER;


    static {
        StringBuilderWriter buf = new StringBuilderWriter(4);
        PrintWriter out = new PrintWriter(buf);
        out.println();
        LINE_SEPARATOR = buf.toString();
        out.close();
    }

    public IOUtil() {
        super();
    }

    /**
     * 比较流是否相同
     * @param input1 流
     * @param input2 流
     * @return 是否相同
     * @throws IOException 异常
     */
    public static boolean contentEquals(InputStream input1, InputStream input2)
            throws IOException {
        if (!(input1 instanceof BufferedInputStream)) {
            input1 = new BufferedInputStream(input1);
        }
        if (!(input2 instanceof BufferedInputStream)) {
            input2 = new BufferedInputStream(input2);
        }

        int ch = input1.read();
        while (EOF != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return ch2 == EOF;
    }

    /**
     * 比较流是否相同
     * @param input1 流
     * @param input2 流
     * @return 是否相同
     * @throws IOException 异常
     */
    public static boolean contentEquals(Reader input1, Reader input2)
            throws IOException {

        input1 = toBufferedReader(input1);
        input2 = toBufferedReader(input2);

        int ch = input1.read();
        while (EOF != ch) {
            int ch2 = input2.read();
            if (ch != ch2) {
                return false;
            }
            ch = input1.read();
        }

        int ch2 = input2.read();
        return ch2 == EOF;
    }

    /**
     * 比较流是否相同，忽略终止符
     * @param input1 流
     * @param input2 流
     * @return 是否相同
     * @throws IOException 结果
     */
    public static boolean contentEqualsIgnoreEOL(Reader input1, Reader input2)
            throws IOException {
        BufferedReader br1 = toBufferedReader(input1);
        BufferedReader br2 = toBufferedReader(input2);

        String line1 = br1.readLine();
        String line2 = br2.readLine();
        while (line1 != null && line1.equals(line2)) {
            line1 = br1.readLine();
            line2 = br2.readLine();
        }
        return Objects.equals(line1, line2);
    }
    public static void closeQuietly(Reader input) {
        closeQuietly((Closeable) input);
    }

    /**
     * 快速关闭
     * @param output 参数
     */
    public static void closeQuietly(Writer output) {
        closeQuietly((Closeable) output);
    }

    /**
     * 快速关闭
     * @param input 参数
     */
    public static void closeQuietly(InputStream input) {
        closeQuietly((Closeable) input);
    }

    /**
     * 快速关闭
     * @param output 参数
     */
    public static void closeQuietly(OutputStream output) {
        closeQuietly((Closeable) output);
    }

    /**
     * 快速关闭
     * @param closeable 参数
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }

    /**
     * 快速关闭
     * @param sock 参数
     */
    public static void closeQuietly(Socket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException ioe) {
                // ignored
            }
        }
    }

    /**
     * 快速关闭
     * @param selector 参数
     */
    public static void closeQuietly(Selector selector) {
        if (selector != null) {
            try {
                selector.close();
            } catch (IOException ioe) {
                // ignored
            }
        }
    }

    /**
     * 快速关闭
     * @param sock 参数
     */
    public static void closeQuietly(ServerSocket sock) {
        if (sock != null) {
            try {
                sock.close();
            } catch (IOException ioe) {
                // ignored
            }
        }
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(InputStream input, OutputStream output)
            throws IOException {
        return copyLarge(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param buffer 字节数组
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param inputOffset 位移
     * @param length 长度
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(InputStream input, OutputStream output, long inputOffset, long length)
            throws IOException {
        return copyLarge(input, output, inputOffset, length, new byte[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param inputOffset 位移
     * @param length 长度
     * @param buffer 字节数组
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(InputStream input, OutputStream output,
                                 final long inputOffset, final long length, byte[] buffer) throws IOException {
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        final int bufferLength = buffer.length;
        int bytesToRead = bufferLength;
        if (length > 0 && length < bufferLength) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, bufferLength);
            }
        }
        return totalRead;
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @return 长度
     * @throws IOException 异常
     */
    public static int copy(InputStream input, Writer output)
            throws IOException {
        return copy(input, output, Charset.defaultCharset());
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @return 长度
     * @throws IOException 异常
     */
    public static int copy(InputStream input, OutputStream output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @return 长度
     * @throws IOException 异常
     */
    public static int copy(InputStream input, Writer output, Charset encoding) throws IOException {
        InputStreamReader in = new InputStreamReader(input, Charsets.toCharset(encoding));
        return copy(in, output);
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @return 长度
     * @throws IOException 异常
     */
    public static int copy(InputStream input, Writer output, String encoding) throws IOException {
        return copy(input, output, Charsets.toCharset(encoding));
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @return 长度
     * @throws IOException 异常
     */
    public static int copy(Reader input, Writer output) throws IOException {
        long count = copyLarge(input, output);
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(Reader input, Writer output) throws IOException {
        return copyLarge(input, output, new char[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param buffer 字节数组
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(Reader input, Writer output, char[] buffer) throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param inputOffset 位移
     * @param length 长度
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(Reader input, Writer output, final long inputOffset, final long length)
            throws IOException {
        return copyLarge(input, output, inputOffset, length, new char[DEFAULT_BUFFER_SIZE]);
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param inputOffset 位移
     * @param length 长度
     * @param buffer 字节数组
     * @return 长度
     * @throws IOException 异常
     */
    public static long copyLarge(Reader input, Writer output, final long inputOffset, final long length, char[] buffer)
            throws IOException {
        if (inputOffset > 0) {
            skipFully(input, inputOffset);
        }
        if (length == 0) {
            return 0;
        }
        int bytesToRead = buffer.length;
        if (length > 0 && length < buffer.length) {
            bytesToRead = (int) length;
        }
        int read;
        long totalRead = 0;
        while (bytesToRead > 0 && EOF != (read = input.read(buffer, 0, bytesToRead))) {
            output.write(buffer, 0, read);
            totalRead += read;
            if (length > 0) { // only adjust length if not reading to the end
                // Note the cast must work because buffer.length is an integer
                bytesToRead = (int) Math.min(length - totalRead, buffer.length);
            }
        }
        return totalRead;
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @throws IOException 异常
     */
    public static void copy(Reader input, OutputStream output)
            throws IOException {
        copy(input, output, Charset.defaultCharset());
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param encoding 编码
     * @throws IOException 异常
     */
    public static void copy(Reader input, OutputStream output, Charset encoding) throws IOException {
        OutputStreamWriter out = new OutputStreamWriter(output, Charsets.toCharset(encoding));
        copy(input, out);
        // TODO Unless anyone is planning on rewriting OutputStreamWriter, we have to flush here.
        out.flush();
    }

    /**
     * 拷贝文件
     * @param input 输入
     * @param output 输出
     * @param encoding 编码
     * @throws IOException 异常
     */
    public static void copy(Reader input, OutputStream output, String encoding) throws IOException {
        copy(input, output, Charsets.toCharset(encoding));
    }

    /**
     * 转字符串
     * @param input 输入
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(InputStream input) throws IOException {
        return toString(input, UTF_8);
    }

    /**
     * 转字符串
     * @param input 输入
     * @param encoding 编码
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(InputStream input, Charset encoding) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy(input, sw, encoding);
        return sw.toString();
    }

    /**
     * 转字符串
     * @param input 输入
     * @param encoding 编码
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(InputStream input, String encoding)
            throws IOException {
        return toString(input, Charsets.toCharset(encoding));
    }

    /**
     * 转字符串
     * @param input 输入
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(Reader input) throws IOException {
        StringBuilderWriter sw = new StringBuilderWriter();
        copy(input, sw);
        return sw.toString();
    }

    /**
     * 转字符串
     * @param uri 输入
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(URI uri) throws IOException {
        return toString(uri, Charset.defaultCharset());
    }

    /**
     * 转字符串
     * @param uri 输入
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(URI uri, Charset encoding) throws IOException {
        return toString(uri.toURL(), Charsets.toCharset(encoding));
    }

    /**
     * 转字符串
     * @param uri 输入
     * @param encoding 字符集
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(URI uri, String encoding) throws IOException {
        return toString(uri, Charsets.toCharset(encoding));
    }

    /**
     * 转字符串
     * @param url 输入
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(URL url) throws IOException {
        return toString(url, Charset.defaultCharset());
    }

    /**
     * 转字符串
     * @param url 输入
     * @param encoding 字符集
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(URL url, Charset encoding) throws IOException {
        try (InputStream inputStream = url.openStream()) {
            return toString(inputStream, encoding);
        }
    }

    /**
     * 转字符串
     * @param url 输入
     * @param encoding 字符集
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(URL url, String encoding) throws IOException {
        return toString(url, Charsets.toCharset(encoding));
    }

    /**
     * 转字符串
     * @param input 输入
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(byte[] input) throws IOException {
        return new String(input);
    }

    /**
     * 转字符串
     * @param input 输入
     * @param encoding 字符集
     * @return 字符串
     * @throws IOException 异常
     */
    public static String toString(byte[] input, String encoding) throws IOException {
        return new String(input, encoding);
    }

    public static long skip(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        if (SKIP_BYTE_BUFFER == null) {
            SKIP_BYTE_BUFFER = new byte[SKIP_BUFFER_SIZE];
        }
        long remain = toSkip;
        while (remain > 0) {
            long n = input.read(SKIP_BYTE_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    public static long skip(Reader input, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Skip count must be non-negative, actual: " + toSkip);
        }
        if (SKIP_CHAR_BUFFER == null) {
            SKIP_CHAR_BUFFER = new char[SKIP_BUFFER_SIZE];
        }
        long remain = toSkip;
        while (remain > 0) {
            long n = input.read(SKIP_CHAR_BUFFER, 0, (int) Math.min(remain, SKIP_BUFFER_SIZE));
            if (n < 0) { // EOF
                break;
            }
            remain -= n;
        }
        return toSkip - remain;
    }

    public static void skipFully(InputStream input, long toSkip) throws IOException {
        if (toSkip < 0) {
            throw new IllegalArgumentException("Bytes to skip must not be negative: " + toSkip);
        }
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Bytes to skip: " + toSkip + " actual: " + skipped);
        }
    }

    public static void skipFully(Reader input, long toSkip) throws IOException {
        long skipped = skip(input, toSkip);
        if (skipped != toSkip) {
            throw new EOFException("Chars to skip: " + toSkip + " actual: " + skipped);
        }
    }

    /**
     * reader转bufferReader
     * @param reader re
     * @return br
     */
    public static BufferedReader toBufferedReader(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output);
        return output.toByteArray();
    }

    public static byte[] toByteArray(InputStream input, long size) throws IOException {

        if (size > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be greater than Integer max value: " + size);
        }

        return toByteArray(input, (int) size);
    }

    public static byte[] toByteArray(InputStream input, int size) throws IOException {

        if (size < 0) {
            throw new IllegalArgumentException("Size must be equal or greater than zero: " + size);
        }

        if (size == 0) {
            return new byte[0];
        }

        byte[] data = new byte[size];
        int offset = 0;
        int readed;

        while (offset < size && (readed = input.read(data, offset, size - offset)) != EOF) {
            offset += readed;
        }

        if (offset != size) {
            throw new IOException("Unexpected readed size. current: " + offset + ", excepted: " + size);
        }

        return data;
    }

    public static byte[] toByteArray(Reader input) throws IOException {
        return toByteArray(input, Charset.defaultCharset());
    }

    public static byte[] toByteArray(Reader input, Charset encoding) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        copy(input, output, encoding);
        return output.toByteArray();
    }

    public static byte[] toByteArray(Reader input, String encoding) throws IOException {
        return toByteArray(input, Charsets.toCharset(encoding));
    }

    public static byte[] toByteArray(String input) throws IOException {
        return input.getBytes();
    }

    public static byte[] toByteArray(URI uri) throws IOException {
        return IOUtil.toByteArray(uri.toURL());
    }

    public static byte[] toByteArray(URL url) throws IOException {
        URLConnection conn = url.openConnection();
        try {
            return IOUtil.toByteArray(conn);
        } finally {
            close(conn);
        }
    }

    public static byte[] toByteArray(URLConnection urlConn) throws IOException {
        try (InputStream inputStream = urlConn.getInputStream()) {
            return IOUtil.toByteArray(inputStream);
        }
    }

    public static void close(URLConnection conn) {
        if (conn instanceof HttpURLConnection) {
            ((HttpURLConnection) conn).disconnect();
        }
    }

    public static void write(byte[] data, OutputStream output)
            throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(byte[] data, Writer output) throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(byte[] data, Writer output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(new String(data, Charsets.toCharset(encoding)));
        }
    }

    public static void write(byte[] data, Writer output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(char[] data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(char[] data, OutputStream output)
            throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(char[] data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(getBytes(new String(data), Charsets.toCharset(encoding)));
        }
    }

    public static void write(char[] data, OutputStream output, String encoding)
            throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(CharSequence data, Writer output) throws IOException {
        if (data != null) {
            write(data.toString(), output);
        }
    }

    public static void write(CharSequence data, OutputStream output)
            throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(CharSequence data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            write(data.toString(), output, encoding);
        }
    }

    public static void write(CharSequence data, OutputStream output, String encoding) throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static void write(String data, Writer output) throws IOException {
        if (data != null) {
            output.write(data);
        }
    }

    public static void write(String data, OutputStream output)
            throws IOException {
        write(data, output, Charset.defaultCharset());
    }

    public static void write(String data, OutputStream output, Charset encoding) throws IOException {
        if (data != null) {
            output.write(getBytes(data, Charsets.toCharset(encoding)));
        }
    }
    public static void write(String data, OutputStream output, String encoding)
            throws IOException {
        write(data, output, Charsets.toCharset(encoding));
    }

    public static byte[] getBytes(String src, Charset charSet) {
        return src.getBytes(charSet);
    }

    public static void writeLines(Collection<?> lines, String lineEnding,
                                  OutputStream output) throws IOException {
        writeLines(lines, lineEnding, output, Charset.defaultCharset());
    }

    public static void writeLines(Collection<?> lines, String lineEnding, OutputStream output, Charset encoding)
            throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        Charset cs = Charsets.toCharset(encoding);
        for (Object line : lines) {
            if (line != null) {
                output.write(getBytes(line.toString(), cs));
            }
            output.write(getBytes(lineEnding,cs));
        }
    }

    public static void writeLines(Collection<?> lines, String lineEnding,
                                  OutputStream output, String encoding) throws IOException {
        writeLines(lines, lineEnding, output, Charsets.toCharset(encoding));
    }

    public static void writeLines(Collection<?> lines, String lineEnding,
                                  Writer writer) throws IOException {
        if (lines == null) {
            return;
        }
        if (lineEnding == null) {
            lineEnding = LINE_SEPARATOR;
        }
        for (Object line : lines) {
            if (line != null) {
                writer.write(line.toString());
            }
            writer.write(lineEnding);
        }
    }

}