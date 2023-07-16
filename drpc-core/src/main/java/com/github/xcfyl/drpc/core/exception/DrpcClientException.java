package com.github.xcfyl.drpc.core.exception;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 14:41
 */
public class DrpcClientException extends Exception {
    private static final long serialVersionUID = 3034815429298677756L;

    public DrpcClientException() {
        super();
    }

    public DrpcClientException(String message) {
        super(message);
    }

    public DrpcClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrpcClientException(Throwable cause) {
        super(cause);
    }

    protected DrpcClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
