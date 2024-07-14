package com.leowork.crm.commons.entity;

/**
 * 专门用户返回Json格式字符串的类
 *
 * @author Leo
 * @version 1.0
 * @className ReturnJsonObject
 * @since 1.0
 **/
public class ReturnJsonObject {
    /**
     * 处理成功或失败的标记
     * 1 --- 成功
     * 0 --- 失败
     */
    private String code;
    /**
     * 返回状态信息
     */
    private String message;
    /**
     * 预留的返回的其他数据
     */
    private Object returnData;

    public ReturnJsonObject() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getReturnData() {
        return returnData;
    }

    public void setReturnData(Object returnData) {
        this.returnData = returnData;
    }

    @Override
    public String toString() {
        return "ReturnJsonObject{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", returnData=" + returnData +
                '}';
    }
}
