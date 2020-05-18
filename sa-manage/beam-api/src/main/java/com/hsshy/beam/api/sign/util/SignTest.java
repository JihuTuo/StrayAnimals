package com.hsshy.beam.api.sign.util;
import com.hsshy.beam.api.sign.security.impl.Base64SecurityAction;
import com.hsshy.beam.common.utils.MD5Util;

public class SignTest {

    public static void main(String[] args) {
        Base64SecurityAction base64SecurityAction = new Base64SecurityAction();

        String a = "{\n" +
                "  \"password\": \"string\",\n" +
                "  \"username\": \"string\"\n" +
                "}";

        String o = base64SecurityAction.doAction(a);
        System.out.println(o);
        String encrypt =  MD5Util.encrypt(o + "miyao");
        System.out.println(encrypt);
    }


}
