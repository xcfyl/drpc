package com.github.xcfyl.drpc.core.common.exception;

/**
 * 请求异常
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:02
 */
public class RequestException extends Exception {
    private static final long serialVersionUID = 5891965362789296631L;

    public RequestException() {
        super();
    }

    public RequestException(String message) {
        super(message);
    }

    public RequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RequestException(Throwable cause) {
        super(cause);
    }

    protected RequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
