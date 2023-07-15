package com.github.xcfyl.drpc.core.pubsub;

/**
 * 服务列表发生变化时，需要发送该事件
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:25
 */
public class DrpcServiceChangeEvent implements DrpcEvent<DrpcServiceChangeEventData> {
    private DrpcServiceChangeEventData serviceChangeInfo;

    @Override
    public DrpcServiceChangeEventData getData() {
        return serviceChangeInfo;
    }

    @Override
    public void setData(DrpcServiceChangeEventData serviceChangeInfo) {
        this.serviceChangeInfo = serviceChangeInfo;
    }

    @Override
    public String getName() {
        return DrpcServiceChangeEvent.class.getName();
    }
}
