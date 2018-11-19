package com.dyc.publishFile.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dyc.publishFile.constants.BaseConstants;
import com.dyc.publishFile.constants.FileConstants;
import com.dyc.publishFile.constants.RedisConstants;
import com.dyc.publishFile.constants.SwitchParamConstants;
import com.dyc.publishFile.model.AbstractFileUtil;
import com.dyc.publishFile.model.ProductServer;
import com.dyc.publishFile.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisCluster;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Set;


/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 21:50
 * @Description:
 */
@Service
public class FileRedisMessageServiceImpl implements FileRedisMessageService {
    private org.apache.logging.log4j.Logger LOG = LogManager.getLogger(FileRedisMessageServiceImpl.class);
    FilePersisService filePersisService = new FilePersisServiceImpl();

    @Override
    public void obtainJarFileMsg() throws Exception {
        LOG.info("进入obtainJarFileMsg方法里");
        JedisCluster jedis = null;
        String pathLocal = "";//文件本地路径
        String pathBak = "";//文件备份路径
        try {

                    /**
                     * 创建价包备份文件夹。
                     */
                    pathLocal = StringUtil.getLibUrlPrefix("pub/");
                    pathBak = StringUtil.getLibUrlPrefix("bak/");

                    FileHandleUtil.judgeFoldExist(pathLocal);
                    LOG.info("成功创建class路径同级的文件夹，路径为：" + pathLocal);

                    /**
                     * 创建价包备份文件夹。
                     */
                    FileHandleUtil.judgeFoldExist(pathBak);
                    LOG.info("备份文件夹创建成功，路径为：" + pathBak);


                    /**
                     * 删除上次发布备份留下来的文件夹里的jar文件
                     */
                    FileHandleUtil.deleteFoldAllFile(pathBak);
                    LOG.info("上次发布备份文件删除成功");


                    /**
                     * 获取JedisCluster对象
                     */
                    jedis = RedisUtil.getRedisClute( RedisConstants.HOST,RedisConstants.PORT);
                    LOG.info("JedisCluster集群对象创建成功,ip端口号为：" + RedisConstants.HOST + ":" + RedisConstants.PORT);


                    /**
                     * 获取该docker服务器名称的所有价包key值比如：publish/update/package:MicroWin10-1353:*
                     *
                     */
                    String keysUrl = getHostKeys();
                    LOG.info("该docker服务器下要发布价包总路径为：" + keysUrl);


                    /**
                     * 获得该服务器下要发布的jar的所有redis路径key值
                     */
                    Set<String> set = RedisUtil.getAllKeys(jedis,keysUrl);
                    LOG.info("要分布的价包redisde的Key路径分别为：" + set);

                    /**
                     * 遍历循环每个价包路径
                     */
                    InputStream remoteInputStrem = null;
                    InputStream remoteInputStrem2 = null;
                    String fileLocalUrl = null;
                    String fileBakUrl = null;
                    for(String key : set){
                        /**
                         * InputStream流方法定义到try外边，在finally里边可以关闭流。
                         */
                        try {
                                LOG.info("");
                                LOG.info("当前要替换的价包的Key路径分别为：" + key);

                                /**
                                 * 通过路径从redis中获取该包的版本对象信息
                                 */
                                String jsonPack = (String)jedis.get( key );
                                LOG.info("获取到的版本信息的json字符串为：" + jsonPack);

                                /**
                                 * 将json字符串转成Object
                                 */
                                ProductServer productServer = JSONObject.parseObject(jsonPack,ProductServer.class);
                                LOG.info("jsonPack对象转为productServer实体对象：" + jsonPack);

                                /**
                                 * 获得服务器文件的路径。
                                 */
                                String fileRemotePath = FileConstants.PATH_SERVER + productServer.getFilePath();
                                LOG.info("通过版本对象获取到文件对象fileRemotePath的路径为：" + fileRemotePath);

                                /**
                                 * 通过截取获得文件的名称
                                 */
                                String fileName = obtainFileNameByUrl( fileRemotePath );
                                LOG.info("获得文件名称fileName：" + fileName);

                                /**
                                 * 获取本地tomcat运行在lib目录下的路径 比如：D:\apache-tomcat-8.0.52\webapps\publishFile\WEB-INF\lib\soul.jar
                                 */
                                fileLocalUrl = pathLocal + fileName;
                                LOG.info("获得本地要更新的该价包的全路径fileLocalUrl：" + fileLocalUrl);

                                /**
                                 * 拼接备份文件的路径 比如：D:\apache-tomcat-8.0.52\webapps\publishFile\WEB-INF\bak\soul.jar
                                 */
                                fileBakUrl = pathBak + fileName;//备份url
                                LOG.info("获得本地要备份的该价包的全路径fileBakUrl：" + fileBakUrl);


                                /**
                                 * 从服务器中或者redis中获取输入流
                                 */
                                remoteInputStrem =  getInputStreamByRedisCfg(fileRemotePath,fileName,jedis);
                                LOG.info("成功获取输入流对象remoteInputStrem：" + remoteInputStrem);

                                /**
                                 * 如果是第一次发布版本，则直接创建，不做任何判断。
                                 */
                                Boolean isExistFile = FileHandleUtil.judgeFileisExist(fileLocalUrl);
                                if( !isExistFile ){
                                    LOG.info("文件未发布过");
                                    FileHandleUtil.copyRemoteToLocalFile(fileLocalUrl, remoteInputStrem);
                                    LOG.info("成功创建本地文件，创建的地址为fileLocalUrl：" +fileLocalUrl);
                                    continue;
                                }

                                /**
                                 * 通过MD5比对服务器跟本地的文件的内容是否相同。
                                 */
                                Boolean isSame = MD5Util.compareMD5ByFileUrl(remoteInputStrem, fileLocalUrl);
                                LOG.info("MD5比对结果isSame：" +isSame);
                                if ( isSame ) {//两边版本不一致

                                    /**
                                     * 从新获取服务器上输入流，因为如果不从新获取，会影响下次MD5的比如。
                                     */
                                    remoteInputStrem2 = getInputStreamByRedisCfg(fileRemotePath,fileName,jedis);
                                    LOG.info("成功再次获取服务器或者redis上的输入流remoteInputStrem2：" +remoteInputStrem2);

                                    /**
                                     * 替换掉本地的价包
                                     */
                                    replaceLocalJar(remoteInputStrem2, fileLocalUrl, fileBakUrl);
                                    LOG.info("价包替换成功" );


                                    /**
                                     * 调用丐哥那个牛逼的动态classLoader方法刷新jvm里边的类
                                     * 不过这个方法可能风险比较大，玩不好，就把虚拟机搞蹦了。
                                     * 大不了重启服务器。
                                     */


                                    /**
                                     * 更新redis中info表的状态为已安装。
                                     */

                                    modifyJarRefreshState(key, "3", jedis);
                                    LOG.info("更新redis中info表中版本对象状态为3,该版本key值为：" +key);

                                    /**
                                     * 删除redis中update表中记录。
                                     */
                         //           jedis.del(key);
                                    LOG.info("成功删除redis中info表的版本对象,该版本key值为：" +key);
                                    }
                        }catch ( Exception e){
                            LOG.error(e);
                            /**
                             * 还原备份文件，准备下次重新安装
                             */
                            LOG.info("我靠，安装过程出现错误啦！");
                            LOG.info("=====文件替换过程出现异常，还原当前备份价包");
                            filePersisService.singleFileBackOriginalVersion( fileLocalUrl,fileBakUrl );

                            /**
                             * 修改redis表中的info状态为4：安装失败。
                             */
                            modifyJarRefreshState(key, "4", jedis);
                            LOG.info("=====redis表info的状态成功更新为4，更新的key值为：" + key);

                        }finally {
                            if( remoteInputStrem!= null ){
                                remoteInputStrem.close();
                                LOG.info("关闭输入流remoteInputStrem：" +remoteInputStrem);
                            }
                            if( remoteInputStrem2!= null ){
                                remoteInputStrem2.close();
                                LOG.info("关闭输入流remoteInputStrem2：" +remoteInputStrem2);
                            }
                        }




                    }
        }catch (Exception e){
            LOG.error(e);
     //       filePersisService.fileBackOriginalVersion(getLibUrlPrefix("lib/"),getLibUrlPrefix("bak/"));
        }finally {
            jedis.close();
            LOG.info("成功关闭redis连接" );
        }
    };


    /**
     * 根据redis参数决定从服务器获取文件还是从redis中获取文件
     * @param fileRemotePath
     * @param fileName
     * @param jedis
     * @return
     * @throws Exception
     */
    public InputStream getInputStreamByRedisCfg( String fileRemotePath,String fileName,JedisCluster jedis) throws Exception {
        InputStream remoteInputStrem = null;
        if(SwitchParamConstants.isRedis){
            String redisFileUrl = new StringBuilder(RedisConstants.FILE_KEY).append(fileName).append(":").toString();
            LOG.info("获得redis中文件的路径redisFileUrl：" + redisFileUrl);
            remoteInputStrem = RedisUtil.getInputStreamFromRedis(redisFileUrl,jedis);
        }else{
            URLConnection connection  = FileConnectUtil.getURLConnectionByPath( fileRemotePath );
            LOG.info("通过该路径获取服务器中的connection对象：" + fileRemotePath);
            remoteInputStrem = connection.getInputStream();
        }
        return remoteInputStrem;
    }










    public void replaceLocalJar(InputStream remoteInputStrem2,String fileLocalUrl, String fileBakUrl) throws Exception {
        try {
            filePersisService.refreshJar( remoteInputStrem2,fileLocalUrl,fileBakUrl);
        }  finally {
            remoteInputStrem2.close();
        }
    }

    /**
     * 更新redis中版本对象的状态
     * @param state
     */
    public void modifyJarRefreshState( String keyUrl,String state, JedisCluster jedis ){
        String keyInfoUrl =  keyUrl.replace("update","info");
        LOG.info("更新redis的keyInfoUrl为：" + keyInfoUrl);
        String jsonPack = (String)jedis.get( keyInfoUrl );
        if( StringUtils.isBlank(jsonPack) ){
            throw new RuntimeException("获取keyInfoUrl的redis值字符串为空：" +jsonPack);
        }
        LOG.info("获取到版本的jsonPack：" + jsonPack);
        ProductServer productServer = JSONObject.parseObject(jsonPack,ProductServer.class);
        productServer.setStatus(state);
        String jsonStr = JSON.toJSONString(productServer);
        LOG.info("状态转换后的jsonStr值为：" + jsonStr);
        jedis.set(keyInfoUrl,jsonStr);
    }

    /**
     * 获得redis的key
     * @return
     * @throws UnknownHostException
     */
    public String getHostKeys() throws UnknownHostException {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String hostName = inetAddress.getHostName();
        if( BaseConstants.IS_IDEA_DEV ) hostName = hostName.equals("MicroWin10-1353")?"bbb":hostName;
        String keyUrl = new StringBuilder( RedisConstants.UPDATE_KEY).append(hostName).append(":*").toString();
        return keyUrl;
    }

    public static String  obtainFileNameByUrl( String path){
        String fileName = new AbstractFileUtil(){
            @Override
            public String getFileName(String path) {
                String[] strArr = path.split("/");
                return  strArr[strArr.length - 1] ;
            }
        }.getFileName(path);
        return fileName;
    }


}
