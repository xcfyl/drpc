package com.github.xcfyl.drpc.core.filter.client;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 14:18
 */
public abstract class DrpcAbstractClientFilter implements DrpcClientFilter {
    @Override
    public String getName() {
        return getClass().getName();
    }
}
