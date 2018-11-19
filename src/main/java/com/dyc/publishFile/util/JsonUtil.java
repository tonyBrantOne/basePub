package com.dyc.publishFile.util;/**
 * @Auther: tony_jaa
 * @Date: 2018/7/27 10:42
 * @Description:
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @Auther: tony_jaa
 * @Date: 2018/7/27 10:42
 * @Description:
 */
public class JsonUtil {



    public static <T>T castJsonObjectToObject( Object jsonObject , Class<T> clazz){
            T t = null;
            if( jsonObject != null ){
                String sysParamStr = JSON.toJSONString(jsonObject);
                t = (T) JSONObject.parseObject(sysParamStr,clazz);
            }
            return t;

    }


}
