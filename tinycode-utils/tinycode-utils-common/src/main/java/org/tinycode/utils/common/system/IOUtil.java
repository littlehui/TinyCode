package org.tinycode.utils.common.system;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * IO流工具.
 *
 * @author littlehui
 * @date 2013-1-31 下午3:07:15
 */
public class IOUtil {
    /**
     * 读取classpath下的文件流.
     *
     * @param path 相对于classPath
     * @return
     * @author littlehui
     * @date 2012-7-28 下午04:50:52
     */
    public static InputStream getClassPathInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    public static String in2Str(InputStream in, String encoding) {
        if (in == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        try {
            InputStreamReader reader = new InputStreamReader(in, encoding);
            int tmp = -1;
            while ((tmp = reader.read()) != -1) {
                char temp = (char) tmp;
                sb.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * 关闭输入输出流.
     *
     * @param stream
     * @author littlehui
     * @date 2013-2-19 下午6:51:18
     */
    public static void close(Object stream) {
        try {
            if (stream == null) {
                return;
            } else if (stream instanceof OutputStream) {
                OutputStream out = (OutputStream) stream;
                out.close();
            } else if (stream instanceof InputStream) {
                InputStream in = (InputStream) stream;
                in.close();
            } else {
                stream.getClass().getMethod("close").invoke(stream);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
