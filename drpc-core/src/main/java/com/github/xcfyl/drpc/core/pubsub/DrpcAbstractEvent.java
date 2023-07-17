package com.github.xcfyl.drpc.core.pubsub;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/17 16:05
 */
public abstract class DrpcAbstractEvent<T> implements DrpcEvent<T> {
    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
