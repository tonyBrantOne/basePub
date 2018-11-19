package com.dyc.publishFile.util;

import org.apache.logging.log4j.LogManager;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/18 12:22
 * @Description:
 */
public class MD5Util {

    private static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(MD5Util.class);
    public static boolean compareMD5ByFileUrl( InputStream inputStreamRemote, String path2 ) throws Exception {
        InputStream  inputStreamLocal = null;
        try {
            /**
             * 获取服务器文件的MD5值
             */
        //    InputStream inputStreamRemote = connection.getInputStream();
            String remoteMD5 = FileHandleUtil.getFileMD5ByInputStrem( inputStreamRemote);
            LOG.info("服务器MD5:"+remoteMD5);

            /**
             * 获取本地文件的MD5值
             */
            inputStreamLocal = new FileInputStream(path2);
            String localMD5 = FileHandleUtil.getFileMD5ByInputStrem( inputStreamLocal);
            LOG.info("本地MD5:"+localMD5);


            /**
             * 判断两个文件内容是不是相同的。
             */
            Boolean isSame = remoteMD5.equals(localMD5);
            LOG.info("文件MD5的值是否一致"+isSame);
            return isSame;
        }finally {
            inputStreamLocal.close();
        }
    }

    public static boolean compareMD5( String path1, String path2 ) throws Exception {
        InputStream  inputStreamLocal = null;
        try {
            /**
             * 获取本地文件的MD5值
             */
            inputStreamLocal = new FileInputStream(path1);
            String remoteMD5 = FileHandleUtil.getFileMD5ByInputStrem( inputStreamLocal);

            /**
             * 获取本地文件的MD5值
             */
            inputStreamLocal = new FileInputStream(path2);
            String localMD5 = FileHandleUtil.getFileMD5ByInputStrem( inputStreamLocal);

            /**
             * 判断两个文件内容是不是相同的。
             */
            Boolean isSame = remoteMD5.equals(localMD5);
            System.out.println("文件MD5的值是否一致"+isSame);
            return isSame;
        }finally {
            inputStreamLocal.close();
        }
    }
}
