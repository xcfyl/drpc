package com.github.xcfyl.drpc.core.exception;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 15:27
 */
public class DrpcServerException extends Exception {
    private static final long serialVersionUID = 4848638642668684315L;

    public DrpcServerException() {
        super();
    }

    public DrpcServerException(String message) {
        super(message);
    }

    public DrpcServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrpcServerException(Throwable cause) {
        super(cause);
    }

    protected DrpcServerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
