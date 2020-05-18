package com.hsshy.beam.common.utils;
import com.hsshy.beam.common.utils.support.HttpKit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DouyinVideoUtil {

    private static String cookies="odin_tt=c46ce311c5ecdb6ef699c4efbfb7172587b71d8747ec010d5834ca4a2f6d6fecc77dfb3dca537a88729450779176ccda; install_id=74080768309; ttreq=1$19245b3c928a74710b3f89c4b73fc3b19e1b77d3; qh[360]=1";


    public static String parseHtml(String url){

            String html = HttpKit.pretendDoGet(url,null);
            String rgex = "playAddr: \"(.*?)\"";
            String playAddr = RegexUtil.getSubUtilSimple(html,rgex);
            System.out.println("playAddr:"+playAddr);
            rgex = "cover: \"(.*?)\"";
            String cover = RegexUtil.getSubUtilSimple(html,rgex);
            System.out.println("cover:"+cover);
            return playAddr;
    }


    public static String getRealUrl(String url){
        //此地址为抖音获取剪切板时的路径地址


        String str[] = {"iid=74655440239&device_id=57318346369","iid=43619087057&device_id=57318346369","iid=74080768309&device_id=41895256493"};

        url = "https://aweme.snssdk.com/aweme/v1/aweme/detail/?aweme_id="+getVideoId(url)+
                "&origin_type=link&retry_type=no_retry&" +
                 str[0] +
                "&ac=wifi&channel=update&aid=1128&app_name=aweme&version_code=650" +
                "&version_name=6.5.0&device_platform=android&ssmix=a&device_type=ONEPLUS+A5000&device_brand=OnePlus" +
                "&language=zh&os_api=27&os_version=8.1.0&uuid=866265037641158&openudid=3ad8508e9189d190&manifest_version_code=650&" +
                "resolution=1080*1920&dpi=420&update_version_code=6502&_rticket=1567061048691&mcc_mnc=46000&js_sdk_version=1.16.3.5" +
                "&ts=1567061049";



        String result=sendGet(url);
        System.out.println("抖音识别剪切板分享链接返回的结果:"+result);
        result=getSubString(result,"play_addr_lowbr","width");//获取最后一个play_addr_lowbr
        result=getSubString(result,"url_list\":[\"","\",\"");//获取 url_list
        return result;

    }


    /**
     *
     * 这里获取作品ID
     * */
    public static String getVideoId (String url){
        String result=sendGet(url);
        result=getSubString(result,"/share/video/","/?");
        System.out.println("vedioId:"+result);
        return result;
    }


    /**
     * 取出中间文本
     *
     * */
    private static String getSubString(String str,String left,String right){
        String strRight="";

        int indexLeft = str.indexOf(left);
        if(indexLeft==-1){
            return "";//没有找到直接返回空以免出现异常
        }else{
            strRight=str.substring(indexLeft);
        }
        int length=str.length()-strRight.length();
        int indexRight = strRight.indexOf(right);
        if (indexRight==-1){
            return "";
        }
        String result=str.substring(length+left.length(),length+indexRight);
        return result;
    }



    //模拟手机请求
    private static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            URL realUrl = new URL(url);
            //打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            //设置通用的请求属性
            connection.setRequestProperty("Host", "aweme.snssdk.com");
            connection.setRequestProperty("Connection", "keep-alive");
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("Cookie",cookies);
//            connection.setRequestProperty("X-SS-REQ-TICKET","1567065174396");
//            connection.setRequestProperty("X-Tt-Token","003c4e578218a9169c7ecf08d8300c68e58f977ee12be1cab370ba2d30cd11a0ee721b742c0f009f1d5068bba681cbc8152d");
//            connection.setRequestProperty("sdk-version","1");
            connection.setRequestProperty("User-agent","com.ss.android.ugc.aweme/650 (Linux; U; Android 8.1.0; zh_CN; ONEPLUS A5000; Build/OPM1.171019.011; Cronet/58.0.2991.0)");
//            connection.setRequestProperty("X-Khronos","X-Khronos");
//            connection.setRequestProperty("X-Gorgon","03006cc00000aa97f2f8a76ab9b6042f07657b81af15d6423218");
//            connection.setRequestProperty("X-Pods","");

//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("Accept-Encoding", "utf-8");
//            connection.setRequestProperty("Host", "api-hl.amemv.com");
//            connection.setRequestProperty("user-agent","okhttp/3.10.0.1");
//            connection.setRequestProperty("cookie",cookies);




            //建立实际的连接
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }catch(Exception e) {
            //发送异常
            return "发送失败,请检查URL地址是否正确";
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch(Exception e2) {
                //关闭异常
                System.out.println("关闭异常");
            }
        }
        return result;
    }


}
