package com.arms.common.vo;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonVO {

    /**
     * 数据体
     */
    private Object data;

    /**
     * 是否需要重定向
     */
    private int    isRedirect;

    /**
     * 返回状态，是否正常
     */
    private int    isSuccess;

    /**
     * 是否登录
     */
    private int    login;

    /**
     * 返回信息 (显示给用户)
     */
    private String msg;

    /**
     * 错误信息（显示给客户端调试）
     */
    private String msgForDebug;

    /**
     * 重定向地址(UI)
     */
    private String redirectURL;

    /**
     * 异常，须有默认构造方法，改造后，用异常码和异常信息代替
     */
    @Deprecated
    private Object exception;

    /**
     * 错误异常码
     */
    private String errorCode;

    public JsonVO(boolean isSuccess) {
        if (isSuccess) {
            this.isSuccess = 1;
        } else {
            this.isSuccess = 0;
        }
    }

    public JsonVO(boolean isSuccess, Object obj) {
        if (isSuccess) {
            this.isSuccess = 1;
        } else {
            this.isSuccess = 0;
        }
        this.data = obj;
    }

    public JsonVO(String jsonDataStr) {
        JsonVO jsonVO = null;
        jsonVO = JSON.parseObject(jsonDataStr, JsonVO.class);
        if (jsonVO != null) {
            this.setData(jsonVO.getData());
            this.setIsRedirect(jsonVO.getIsRedirect());
            this.setIsSuccess(jsonVO.getIsSuccess());
            this.setLogin(jsonVO.getLogin());
            this.setMsg(jsonVO.getMsg());
            this.setRedirectURL(jsonVO.getRedirectURL());
            this.setException(jsonVO.getException());
            this.setErrorCode(jsonVO.getErrorCode());
            this.setMsgForDebug(jsonVO.getMsgForDebug());
        }
    }

    public JsonVO() {
    }

    public Object getData() {
        return data;
    }

    public int getIsSuccess() {
        return isSuccess;
    }

    public int getLogin() {
        return login;
    }

    public String getMsg() {
        return msg;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public JsonVO setData(Object data) {
        this.data = data;
        return this;
    }

    public JsonVO setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }

    public JsonVO setLogin(int login) {
        this.login = login;
        return this;
    }

    public JsonVO setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public JsonVO setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
        return this;
    }

    public String getMsgForDebug() {
        return msgForDebug;
    }

    public void setMsgForDebug(String msgForDebug) {
        this.msgForDebug = msgForDebug;
    }

    public int getIsRedirect() {
        return isRedirect;
    }

    public JsonVO setIsRedirect(int isRedirect) {
        this.isRedirect = isRedirect;
        return this;
    }

    public <T> T getObjectData(Class<T> clazz) {
        if (this.getIsSuccess() == 1 && this.getData() != null) {
            if (this.getData() instanceof JSONObject) {
                return JSON.toJavaObject((JSONObject) this.getData(), clazz);
            } else {
                return clazz.cast(this.getData());
            }
        }
        return null;
    }

    public <T> T getExpcetionData(Class<T> clazz) {
        if (this.getIsSuccess() == 0 && this.getException() != null) {
            if (this.getException() instanceof JSONObject) {
                return JSON.toJavaObject((JSONObject) this.getException(), clazz);
            } else {
                return clazz.cast(this.getException());
            }
        }
        return null;
    }

    public <T> List<T> getArrayData(Class<T> clazz) {
        if (this.getIsSuccess() == 1 && this.getData() != null) {
            return JSON.parseArray(JSON.toJSONString(this.getData()), clazz);
        }
        return null;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public String toString(String callback) {
        return callback + '(' + this.toString() + ')';
    }

    public Object getException() {
        return exception;
    }

    public JsonVO setException(Object exception) {
        this.exception = exception;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
