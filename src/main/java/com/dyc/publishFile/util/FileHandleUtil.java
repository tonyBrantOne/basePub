package com.dyc.publishFile.util;
import org.apache.logging.log4j.LogManager;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 19:28
 * @Description:
 */
public class FileHandleUtil {
    private static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(FileHandleUtil.class);



    /**
     * 远程替换本地文件
     * @param
     * @param path2
     * @throws Exception
     */
    public static void copyRemoteToLocalFile( String path2,   InputStream inputStream ) throws Exception {
    //    InputStream inputStream =null;
        OutputStream outputStream = null;
        try {
        //    inputStream = connection.getInputStream();
            outputStream = new FileOutputStream( path2 );
            replaceLocalFile(inputStream,outputStream);
        }
        finally {
            outputStream.close();
        }
    }


    /**
     * 本地替换本地文件
     * @param path1
     * @param path2
     * @throws Exception
     */
    public static void copyLocalToLocalFile(String path1, String path2) throws Exception {
        InputStream inputStream =null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(path1);
            outputStream = new FileOutputStream(path2);
            replaceLocalFile(inputStream,outputStream);
        }
        finally {
            inputStream.close();
            outputStream.close();
        }
    }


    /**
     * 输入流数据传输到输出流
     * @param inputStream
     * @param outputStream
     * @throws Exception
     */
    private static void replaceLocalFile(InputStream inputStream, OutputStream outputStream) throws Exception{
        byte[] buf = new byte[1024];
        int bytesRead;
        while ( ( bytesRead = inputStream.read(buf)) != -1){
            outputStream.write(buf,0,bytesRead);
        }
        LOG.info("流数据转换完成");
    }


    /**
     * 获取文件的MD5的值
     * @param
     * @return
     * @throws Exception
     */
    public static String getFileMD5ByInputStrem( InputStream inputStream ) throws Exception {
        MessageDigest digest = null;
     //   InputStream inputStream =null;
        try {
            digest = MessageDigest.getInstance("MD5");
      //      inputStream = new FileInputStream(path1);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ( ( bytesRead = inputStream.read(buf)) != -1){
                digest.update(buf, 0, bytesRead);
            }
            BigInteger bigInt = new BigInteger(1, digest.digest());
            String bigStr = bigInt.toString();
       //     System.out.println("文件MD5的值为" + bigStr);
            return bigStr;
        }
        finally {
        //    inputStream.close();
        }
    }


    /**
     * 判断文件夹是否存在并创建文件
     * @param path
     */
    public static void judgeFoldExist( String path ) {
        File file = new File(path);
        LOG.info("当前文件夹是否存在：" + file.isDirectory());
        if( !file.isDirectory() ){
            file.mkdir();
            LOG.info("成功创建文件夹");
        }
    }


    /**
     * 判断文件夹是否存在并创建文件
     * @param path
     */
    public static Boolean judgeFileisExist( String path ) {
        File file = new File(path);
        if( !file.exists() ){
            return false;
        }
        return true;
    }

    public static void deleteFile( String path ) {
        File file = new File(path);
        if( file.exists() ){
            file.delete();
        }
    }

    public static void deleteFoldAllFile( String path ){
        File file = new File(path);		//获取其file对象
        LOG.info("上次发布备份文件对象：" + file);
        if( !file.isDirectory() ){
            throw new RuntimeException("当前文件目录不存在，目录路径为：" + path);
        }
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File f : fs){
            f.delete();
        }
    }


}
