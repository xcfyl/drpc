package com.github.xcfyl.pandarpc.core.event.data;

import com.github.xcfyl.pandarpc.core.registry.RegistryData;
import lombok.ToString;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:26
 */
@ToString
public class ServiceUpdateEventData {
    private String serviceName;
    private List<RegistryData> newServiceList;

    public ServiceUpdateEventData() {

    }

    public ServiceUpdateEventData(String serviceName, List<RegistryData> newServiceList) {
        this.serviceName = serviceName;
        this.newServiceList = newServiceList;
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<RegistryData> getNewServiceList() {
        return newServiceList;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setNewServiceList(List<RegistryData> newServiceList) {
        this.newServiceList = newServiceList;
    }
}
