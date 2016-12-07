package com.homework.model;

import com.homework.constant.StateCode;

/**
 * by wangrongjun on 2016/12/6.
 */
public interface CallBack<T> {

    void onSucceed(T data);

    void onFailure(StateCode stateCode, String errorMsg);

}
