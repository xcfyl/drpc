package com.github.xcfyl.pandarpc.core.exception;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/23 23:49
 */
public class ConfigErrorException extends Exception {
    private static final long serialVersionUID = 970578430898267112L;

    public ConfigErrorException() {
        super();
    }

    public ConfigErrorException(String message) {
        super(message);
    }

    public ConfigErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigErrorException(Throwable cause) {
        super(cause);
    }

    protected ConfigErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
