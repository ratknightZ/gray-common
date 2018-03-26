package com.arms.common.exception;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 系统级别异常
 * @author mingqian
 * @version $Id: TaotieSystemException.java, v 0.1 Nero Exp $
 */
public class SystemException extends RuntimeException {
    protected final Log       logger           = LogFactory.getLog(this.getClass());

    /**  */
    private static final long serialVersionUID = -1139997083075473334L;

    public SystemException() {
        super();
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
        logger.error(message, cause);
    }

    public SystemException(String message) {
        super(message);
        logger.error(message);
    }

    public SystemException(Throwable cause) {
        super(cause);
        logger.error(cause);
    }

}
