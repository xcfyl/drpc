package com.github.xcfyl.drpc.core.exception;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/24 10:53
 */
public class DrpcRouterException extends Exception {
    private static final long serialVersionUID = -5744562791718343849L;

    public DrpcRouterException() {
        super();
    }

    public DrpcRouterException(String message) {
        super(message);
    }

    public DrpcRouterException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrpcRouterException(Throwable cause) {
        super(cause);
    }

    protected DrpcRouterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
