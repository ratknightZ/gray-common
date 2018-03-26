package com.arms.common.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 业务异常
 * @author mingqian
 * @version $Id: TaotieBizException.java, v 0.1 Nero Exp $
 */
public class BizException extends Exception {

    /**  */
    private static final long serialVersionUID    = -1105695927225940250L;

    protected final Log       logger              = LogFactory.getLog(this.getClass());

    /**
     * 用于返回页头StatusCode，如 404,500,200 等
     * */
    public static final int   DEFAULT_ERROR_CODE  = 500;
    public static final int   PAGE_NOT_FOUND_CODE = 404;
    public static final int   PAGE_ON_SUCCESS     = 200;

    private String            errorCode;

    public BizException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode + "";
        logger.warn("ErrorCode: " + errorCode + ",Message:" + msg);

    }

    public BizException(String errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        logger.warn("ErrorCode: " + errorCode + ",Message:" + msg);

    }

    public BizException(String msg) {
        super(msg);
        logger.warn("Message:" + msg);
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

}
