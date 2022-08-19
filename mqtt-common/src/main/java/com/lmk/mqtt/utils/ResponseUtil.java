package com.lmk.mqtt.utils;

import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author lmk
 * @version 1.0.0
 * @ClassName ResponseUtil.java
 * @Description TODO
 * @createTime 2022-07-29 21:34:36
 */
public class ResponseUtil {

    public static void response(HttpServletResponse response,Object message){
        try {
            response.setContentType("application/json;charset=utf-8");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(JSON.toJSONString(message));
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
