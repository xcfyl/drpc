package com.github.xcfyl.drpc.core.exception;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 10:53
 */
public class RouterException extends Exception {
    private static final long serialVersionUID = -5744562791718343849L;

    public RouterException() {
        super();
    }

    public RouterException(String message) {
        super(message);
    }

    public RouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouterException(Throwable cause) {
        super(cause);
    }

    protected RouterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
