package com.github.xcfyl.drpc.core.exception;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/15 13:56
 */
public class DrpcCommonException extends Exception {
    private static final long serialVersionUID = -6323156574611662095L;

    public DrpcCommonException() {
        super();
    }

    public DrpcCommonException(String message) {
        super(message);
    }

    public DrpcCommonException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrpcCommonException(Throwable cause) {
        super(cause);
    }

    protected DrpcCommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
