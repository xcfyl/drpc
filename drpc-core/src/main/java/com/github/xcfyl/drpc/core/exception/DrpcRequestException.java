package com.github.xcfyl.drpc.core.exception;

/**
 * 请求异常
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 10:02
 */
public class DrpcRequestException extends Exception {
    private static final long serialVersionUID = 5891965362789296631L;

    public DrpcRequestException() {
        super();
    }

    public DrpcRequestException(String message) {
        super(message);
    }

    public DrpcRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrpcRequestException(Throwable cause) {
        super(cause);
    }

    protected DrpcRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
