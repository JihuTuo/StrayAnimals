package com.hsshy.beam.common.utils;
import com.alibaba.fastjson.JSON;
import com.hsshy.beam.common.constant.RetEnum;
import com.hsshy.beam.common.exception.BeamException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
/**
 * 渲染工具类
 *
 * @author stylefeng
 * @date 2017-08-25 14:13
 */
public class RenderUtil {

    /**
     * 渲染json对象
     */
    public static void renderJson(HttpServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            throw new BeamException(RetEnum.WRITE_ERROR);
        }
    }

    /**
     * 渲染json对象
     */
    public static void renderJson(ServletResponse response, Object jsonObject) {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSON.toJSONString(jsonObject));
        } catch (IOException e) {
            throw new BeamException(RetEnum.WRITE_ERROR);
        }
    }
}
