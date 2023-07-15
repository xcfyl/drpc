package com.github.xcfyl.drpc.core.exception;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:49
 */
public class DrpcConfigException extends Exception {
    private static final long serialVersionUID = 970578430898267112L;

    public DrpcConfigException() {
        super();
    }

    public DrpcConfigException(String message) {
        super(message);
    }

    public DrpcConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public DrpcConfigException(Throwable cause) {
        super(cause);
    }

    protected DrpcConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
