package com.xmingl.base.exception;

public class BaseException {

    // TODO 对异常的设计不是很熟悉，暂不考虑自定义异常

    public static final String TYPE_APP_NULL_POINT = "空指针";

    public static final String TYPE_APP_NULL_PARAM = "缺少参数";

    private String type;

    public BaseException(String type) {
        this.type = type;
    }
}
