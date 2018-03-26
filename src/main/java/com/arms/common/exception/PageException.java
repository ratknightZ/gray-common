package com.arms.common.exception;

public class PageException extends BizException {
    /**  */
    private static final long serialVersionUID = 1L;

    public PageException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PageException(String msg) {
        super(DEFAULT_ERROR_CODE, msg);
    }

}
