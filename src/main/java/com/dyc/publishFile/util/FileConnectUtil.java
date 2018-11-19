package com.dyc.publishFile.util;/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 20:53
 * @Description:
 */

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 20:53
 * @Description:
 */
public class FileConnectUtil {

    /**
     * 获取下载文件的InputStream对象
     * @param path
     * @return
     * @throws IOException
     */
    public static URLConnection getURLConnectionByPath(String path) throws IOException {
        URL url = new URL(path);
        URLConnection connection = url.openConnection();
        return connection;
    }


}
