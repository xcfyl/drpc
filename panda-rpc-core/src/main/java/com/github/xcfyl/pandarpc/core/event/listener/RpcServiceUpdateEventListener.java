package com.github.xcfyl.pandarpc.core.event.listener;

import com.github.xcfyl.pandarpc.core.event.RpcEventListener;
import com.github.xcfyl.pandarpc.core.event.RpcServiceUpdateEvent;
import com.github.xcfyl.pandarpc.core.event.data.RpcServiceUpdateEventData;

/**
 * 如果rpc服务列表发生变化，那么会执行该事件监听器的逻辑
 * 具体来说，需要更新本地客户端服务列表的缓存
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:30
 */
public class RpcServiceUpdateEventListener implements RpcEventListener<RpcServiceUpdateEvent> {
    @Override
    public void callback(RpcServiceUpdateEvent event) {
        RpcServiceUpdateEventData data = event.getData();
        System.out.println(data);
    }
}
