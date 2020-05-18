package com.hsshy.beam.web.modular.base.controller;
import com.hsshy.beam.common.utils.support.HttpKit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {




    protected HttpServletRequest getHttpServletRequest() {
        return HttpKit.getRequest();
    }

    protected HttpServletResponse getHttpServletResponse() {
        return HttpKit.getResponse();
    }

    protected HttpSession getSession() {
        return HttpKit.getRequest().getSession();
    }

    protected HttpSession getSession(Boolean flag) {
        return HttpKit.getRequest().getSession(flag);
    }

    protected String getPara(String name) {
        return HttpKit.getRequest().getParameter(name);
    }

    protected void setAttr(String name, Object value) {
        HttpKit.getRequest().setAttribute(name, value);
    }

    protected Integer getSystemInvokCount() {
        return (Integer) this.getHttpServletRequest().getServletContext().getAttribute("systemCount");
    }


    public HttpServletResponse getExportExcelResponse(String title){
        HttpServletResponse response = HttpKit.getResponse();
        //web浏览通过MIME类型判断文件是excel类型
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        // Content-disposition属性设置成以附件方式进行下载
        try {
            response.setHeader("Content-disposition", "attachment;filename=" +new String((title+ ".xlsx").getBytes(), "iso-8859-1") );
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return response;

    }



}
