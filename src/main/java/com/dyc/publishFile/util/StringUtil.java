package com.dyc.publishFile.util;

import com.dyc.publishFile.constants.BaseConstants;
import com.dyc.publishFile.constants.FileConstants;
import com.dyc.publishFile.service.FileRedisMessageServiceImpl;
import org.apache.logging.log4j.LogManager;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Auther: tony_jaa
 * @Date: 2018/8/18 12:22
 * @Description:
 */
public class StringUtil {

    private static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(StringUtil.class);


    public static String getLibUrlPrefix( String pathSubfix ) throws UnknownHostException {
        String path = FileRedisMessageServiceImpl.class.getResource("").getPath();
        String[] arr = path.split("WEB-INF");
        String rootPath = arr[0];
        rootPath = rootPath.substring(5);

        InetAddress inetAddress = InetAddress.getLocalHost();
        String hostName = inetAddress.getHostName();
        if( BaseConstants.IS_IDEA_DEV ) {
            if (hostName.equals("MicroWin10-1353")) {
                if ("bak/".equals(pathSubfix)) {
                    return FileConstants.PATHB_BAK;
                }
                return FileConstants.PATH_REPLACEED;

            }
        }
        return rootPath + "WEB-INF/" + pathSubfix;
    }

}
