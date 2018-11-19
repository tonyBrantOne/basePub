package com.dyc.publishFile.service;

import java.io.InputStream;

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 20:55
 * @Description:
 */
public interface FilePersisService {
    public void refreshJar(InputStream remoteInputStrem2, String path2, String pathBak);

    /**
     * 还原文件
     * @param path2
     * @param pathBak
     */
    public void fileBackOriginalVersion(String path2, String pathBak) throws Exception;

    void singleFileBackOriginalVersion(String pathLocal, String pathBak) throws Exception;
}
