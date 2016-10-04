package com.spm.constant;

/**
 * by 王荣俊 on 2016/10/5.
 */
public class C {

    public static final String hostUrl = "http://123.207.87.197/homework";
    public static final String verifyUrl = hostUrl + "/verifyCode";
    public static final String loginUrl = hostUrl + "/pc/login";

    public static String getLoginOutput(String number, String password, String verifyCode) {
        return "userName=" + number + "&password=" + password + "&role=1&verifyCode=" + verifyCode;
    }

}
