package com.hsshy.beam.admin.common.listener;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.Map;

/**
 * ServletContext监听器
 *
 * @author stylefeng
 * @Date 2018/2/22 21:07
 */
public class ConfigListener implements ServletContextListener {

    private static Map<String, String> conf = new HashMap<>();

    public static Map<String, String> getConf() {
        return conf;
    }

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        conf.clear();
    }

    @Override
    public void contextInitialized(ServletContextEvent evt) {
        ServletContext sc = evt.getServletContext();

        //项目发布,当前运行环境的绝对路径 例如：C:\Users\xxx\AppData\Local\Temp\tomcat-docbase.251591378651784368.8080\
        conf.put("realPath", sc.getRealPath("/").replaceFirst("/", ""));

        //servletContextPath,默认"" 例如：/beam_ht
        conf.put("contextPath", sc.getContextPath());

    }

}
