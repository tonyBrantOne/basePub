package com.dyc.publishFile.util;/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 21:22
 * @Description:
 */

import com.dyc.publishFile.constants.FileConstants;
import com.dyc.publishFile.constants.RedisConstants;
import com.dyc.publishFile.service.FilePersisService;
import com.dyc.publishFile.service.FilePersisServiceImpl;
import com.dyc.publishFile.service.FileRedisMessageService;
import com.dyc.publishFile.service.FileRedisMessageServiceImpl;
import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLConnection;

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 21:22
 * @Description:
 */
public class MainStart {
    private static final String PATH1 = "http://192.168.0.11:8084/fserver/files/SG/0/headImage/1/1534504703696.png";
    private static final String PATH_A = "D:\\folder\\a\\";//文件存放路径
    private static final String PATH2 = "D:\\folder\\b\\";//文件存放路径
    private static final String PATHB_BAK = "D:\\folder\\bak\\";//文件存放路径
    private static final String NAME = "jasper.jar";//文件存放路径



    public static void test1() throws Exception {
       // MD5Util.compareMD5ByFileUrl(PATH1 ,PATH2 + NAME);
    }

    public static void test2() throws Exception {
        FileHandleUtil.judgeFoldExist(FileConstants.PATHB_BAK);
    }

    public static void test3() throws Exception {
        URLConnection connection = FileConnectUtil.getURLConnectionByPath(PATH1);
        InputStream inputStreamRemote = connection.getInputStream();
        inputStreamRemote.close();
    }

    public static void test4(){
        String keyUrl = "publish/update/package:aaa:com.soul.jar";
        String[] arr = keyUrl.split(":");
        arr[0] = RedisConstants.INFOR_KEY;
        String keyInfoUrl = StringUtils.join(arr,":");
        System.out.println(keyInfoUrl);
    }

    public static void test5(){
        String keyUrl = "publish/update/package:aaa:com.soul.jar";
        String keyInfoUrl = keyUrl.replace("update","info");
        System.out.println(keyInfoUrl);
    }

    public static void test6() throws Exception {
        FileRedisMessageService fileRedisMessageService = new FileRedisMessageServiceImpl();
        fileRedisMessageService.obtainJarFileMsg();
    }

    public static void test7() throws Exception {
        FileHandleUtil.copyLocalToLocalFile(PATH2 + NAME, PATHB_BAK +NAME );
        MD5Util.compareMD5(PATH2 + NAME, PATHB_BAK +NAME);
    }

    public static void test8() throws Exception {
        String path1 = "D:/apache-tomcat-8.0.52/webapps/publishFile/WEB-INF/lib/basePub-1.0-SNAPSHOT.jar";
        Boolean b = FileHandleUtil.judgeFileisExist(path1);
        System.out.println(b);
        FileHandleUtil.deleteFile(path1);
        FileHandleUtil.copyLocalToLocalFile(PATH_A+"basePub-1.0-SNAPSHOT.jar",path1);
    }

    public static void test9() throws Exception {
        String fileRemotePath = FileConstants.PATH_SERVER + "war/jasper.jar/20180819/jasper.jar";
        URLConnection connection  = FileConnectUtil.getURLConnectionByPath( fileRemotePath );
        if (true){
       //     Thread.sleep(2000);
      //       FileHandleUtil.copyRemoteToLocalFile(PATH2 + NAME, connection );
       //     MD5Util.compareMD5ByFileUrl(connection, PATH2 +NAME);
       //     FileHandleUtil.copyRemoteToLocalFile(PATH2 + NAME, connection );
        }
    }

    public static void test10() throws Exception {
        String fileRemotePath = FileConstants.PATH_SERVER + "war/jasper.jar/20180819/jasper.jar";
        URLConnection connection  = FileConnectUtil.getURLConnectionByPath( fileRemotePath );
        String str = FileHandleUtil.getFileMD5ByInputStrem(connection.getInputStream());
        System.out.println("str==" + str);
        InputStream inputStream = new FileInputStream(PATH2 + NAME);
        String str2 = FileHandleUtil.getFileMD5ByInputStrem(inputStream);
        System.out.println("str2==" + str2);
    }

    public static void test11() throws Exception{
        FilePersisService filePersisService = new FilePersisServiceImpl();
        filePersisService.fileBackOriginalVersion(PATH2,PATHB_BAK);
    }

    public static void main(String[] args) throws Exception {
        FilePersisService fileService = new FilePersisServiceImpl();
//        test10();
//        test9();
        test6();
    }

}
