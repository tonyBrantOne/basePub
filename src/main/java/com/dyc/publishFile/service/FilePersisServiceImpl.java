package com.dyc.publishFile.service;
/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 20:55
 * @Description:
 */

import com.dyc.publishFile.util.FileHandleUtil;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.InputStream;

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/17 20:55
 * @Description:
 */
public class FilePersisServiceImpl implements FilePersisService {
    private org.apache.logging.log4j.Logger LOG = LogManager.getLogger(FilePersisServiceImpl.class);

    @Override
    public void refreshJar(InputStream remoteInputStrem2, String path2, String pathBak){
        try {
            /**
             * 获取http连接对象
             */
       //     URLConnection connection = FileConnectUtil.getURLConnectionByPath(path1);


            /**
             * 备份本地文件
             */
            FileHandleUtil.copyLocalToLocalFile(path2 , pathBak );

            /**
             * 删除本地文件
             */
     //       FileHandleUtil.deleteFile(path2);

            /**
             * 生成本地文件
             */
            FileHandleUtil.copyRemoteToLocalFile(path2, remoteInputStrem2);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
        }

    }


    @Override
    public void fileBackOriginalVersion(String path2, String pathBak) throws Exception {
        File file = new File(pathBak);		//获取其file对象
        File[] fs = file.listFiles();	//遍历path下的文件和目录，放在File数组中
        for(File f : fs){
            if(!f.isDirectory()){ //若非目录(即文件)
                String pathBakUrl = pathBak + f.getName();
                String pathUrl = path2 + f.getName();
                FileHandleUtil.copyLocalToLocalFile(pathBakUrl,pathUrl);
            }
        }
    }


    @Override
    public void singleFileBackOriginalVersion(String pathLocal, String pathBak) throws Exception {
                LOG.info("被替换本地文件为pathLocal："+pathLocal);
                LOG.info("备份文件为pathBak："+pathBak);
                Boolean fileisExist = FileHandleUtil.judgeFileisExist(pathBak);
                LOG.info("当前备份文件夹是否存在标志fileisExist："+fileisExist);
                if( fileisExist ){
                    FileHandleUtil.copyLocalToLocalFile(pathBak,pathLocal);
                    LOG.info("存在备份文件，并成功还原回去");
                }else{
                    LOG.info("不存在备份文件");
                }
    }

}
