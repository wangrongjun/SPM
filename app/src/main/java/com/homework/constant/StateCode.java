package com.homework.constant;

/**
 * by wangrongjun on 2016/12/6.
 */
public enum StateCode {

    OK,

    /**
     * 参数为空
     */
    PARAM_NULL,
    /**
     * 网络或服务器访问失败
     */
    INTERNET_FAIL,
    /**
     * 程序出错
     */
    APP_ERROR,

    /**
     * 服务器：权限错误
     */
    ERROR_ILLEGAL,
    /**
     * 服务器：校验错误
     */
    ERROR_NORMAL,
    /**
     * 服务器：存储错误
     */
    ERROR_STORAGE,
    /**
     * 服务器：未知错误
     */
    ERROR_UNKNOWN
}
