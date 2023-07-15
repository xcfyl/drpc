package com.github.xcfyl.drpc.core.pubsub.event;

import com.github.xcfyl.drpc.core.registry.ProviderData;
import lombok.ToString;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:26
 */
@ToString
public class ServiceUpdateEventData {
    private String serviceName;
    private List<ProviderData> newServiceList;

    public ServiceUpdateEventData() {

    }

    public ServiceUpdateEventData(String serviceName, List<ProviderData> newServiceList) {
        this.serviceName = serviceName;
        this.newServiceList = newServiceList;
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<ProviderData> getNewServiceList() {
        return newServiceList;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setNewServiceList(List<ProviderData> newServiceList) {
        this.newServiceList = newServiceList;
    }
}
