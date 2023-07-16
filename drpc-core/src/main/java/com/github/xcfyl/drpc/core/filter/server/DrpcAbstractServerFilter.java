package com.github.xcfyl.drpc.core.filter.server;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 14:19
 */
public abstract class DrpcAbstractServerFilter implements DrpcServerFilter {
    @Override
    public String getName() {
        return getClass().getName();
    }

    /**
     * 默认拥有最小的优先级
     *
     * @return
     */
    @Override
    public Integer getOrder() {
        return Integer.MAX_VALUE;
    }
}
