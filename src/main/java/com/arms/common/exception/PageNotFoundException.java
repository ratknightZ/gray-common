package com.arms.common.exception;

public class PageNotFoundException extends PageException {
    /**  */
    private static final long   serialVersionUID = 1L;

    private static final String ERROR_MSG        = "页面不存在";

    public PageNotFoundException() {
        super(PAGE_NOT_FOUND_CODE, ERROR_MSG);
    }

    public PageNotFoundException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public PageNotFoundException(String msg) {
        super(PAGE_NOT_FOUND_CODE, msg);
    }

}
