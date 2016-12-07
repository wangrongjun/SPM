package com.homework.model.api;

import com.homework.constant.StateCode;

/**
 * by wangrongjun on 2016/12/6.
 */
public class Response<T> {

    private StateCode stateCode;
    private String message;
    private T entity;
    private T entityList;
    private int currentPage; // 当前页数
    private int pageSize;    // 每页显示数量
    private int maxCount;    // 总条数
    private int maxPage;     // 总页数

    public Response(StateCode stateCode, String message) {
        this.stateCode = stateCode;
        this.message = message;
    }

    public Response(StateCode stateCode, T entity) {
        this.stateCode = stateCode;
        this.entity = entity;
    }

    public StateCode getStateCode() {
        return stateCode;
    }

    public String getMessage() {
        return message;
    }

    public T getEntity() {
        return entity;
    }

    public T getEntityList() {
        return entityList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getMaxPage() {
        return maxPage;
    }
}
