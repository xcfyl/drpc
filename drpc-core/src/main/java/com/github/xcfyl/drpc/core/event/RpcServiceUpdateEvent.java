package com.github.xcfyl.drpc.core.event;

import com.github.xcfyl.drpc.core.event.data.RpcServiceUpdateEventData;

/**
 * 服务列表发生变化时，需要发送该事件
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:25
 */
public class RpcServiceUpdateEvent implements RpcEvent<RpcServiceUpdateEventData> {
    private RpcServiceUpdateEventData serviceChangeInfo;

    @Override
    public RpcServiceUpdateEventData getData() {
        return serviceChangeInfo;
    }

    @Override
    public void setData(RpcServiceUpdateEventData serviceChangeInfo) {
        this.serviceChangeInfo = serviceChangeInfo;
    }

    @Override
    public String getName() {
        return RpcServiceUpdateEvent.class.getName();
    }
}
