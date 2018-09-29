package com.hsshy.beam.modular.auth.util;

import com.hsshy.beam.modular.auth.security.impl.Base64SecurityAction;
import com.hsshy.common.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;

public class SignTest {

    public static void main(String[] args) {
        Base64SecurityAction base64SecurityAction = new Base64SecurityAction();

        String a = "{\n" +
                "  \"password\": \"string\",\n" +
                "  \"userName\": \"string\"\n" +
                "}";

        String o = base64SecurityAction.doAction(a);
        System.out.println(o);
        String encrypt =  MD5Util.encrypt(o + "evsae7");
        System.out.println(encrypt);
    }


}
