package com.hsshy.beam.common.utils.xss;
import com.hsshy.beam.common.constant.RetEnum;
import com.hsshy.beam.common.utils.R;
import com.hsshy.beam.common.utils.RenderUtil;
import com.hsshy.beam.common.utils.ToolUtil;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;


public class XssFilter implements Filter {

    FilterConfig filterConfig = null;

    private List<String> urlExclusion = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    @Override
    public void destroy() {
        this.filterConfig = null;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String servletPath = httpServletRequest.getServletPath();
        XssAndSqlHttpServletRequestWrapper xssRequest = null;
        if (urlExclusion != null && urlExclusion.contains(servletPath)) {
            chain.doFilter(request, response);
        } else {
            String param = "";
            String method = "GET";
            if (request instanceof HttpServletRequest) {
                method = ((HttpServletRequest) request).getMethod();
                xssRequest = new XssAndSqlHttpServletRequestWrapper((HttpServletRequest) request);
            }
            if ("POST".equalsIgnoreCase(method)) {
                param = XssFilter.getBodyString(xssRequest.getReader());
                if(ToolUtil.isNotEmpty(param)){
//                    防止xss跨脚本攻击
//                    param = xssRequest.stripXSSAndSql(param);
                    if(XssAndSqlHttpServletRequestWrapper.checkXSSAndSql(param)){
                        RenderUtil.renderJson(response, R.fail(RetEnum.DANGER_ERROR.getRet(),RetEnum.DANGER_ERROR.getMsg()));
                        return;
                    }
                }
            }
            chain.doFilter(xssRequest,response);
        }
    }

    public List<String> getUrlExclusion() {
        return urlExclusion;
    }

    public void setUrlExclusion(List<String> urlExclusion) {
        this.urlExclusion = urlExclusion;
    }

    // 获取request请求body中参数
    public static String getBodyString(BufferedReader br) {
        String inputLine;
        String str = "";
        try {
            while ((inputLine = br.readLine()) != null) {
                str += inputLine;
            }
            br.close();
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }
        return str;

    }
}