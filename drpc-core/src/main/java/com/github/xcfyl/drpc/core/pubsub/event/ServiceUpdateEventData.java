package com.github.xcfyl.drpc.core.pubsub.event;

import com.github.xcfyl.drpc.core.registry.ProviderRegistryData;
import lombok.ToString;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:26
 */
@ToString
public class ServiceUpdateEventData {
    private String serviceName;
    private List<ProviderRegistryData> newServiceList;

    public ServiceUpdateEventData() {

    }

    public ServiceUpdateEventData(String serviceName, List<ProviderRegistryData> newServiceList) {
        this.serviceName = serviceName;
        this.newServiceList = newServiceList;
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<ProviderRegistryData> getNewServiceList() {
        return newServiceList;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setNewServiceList(List<ProviderRegistryData> newServiceList) {
        this.newServiceList = newServiceList;
    }
}
