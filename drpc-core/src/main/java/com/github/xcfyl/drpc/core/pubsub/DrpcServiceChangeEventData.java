package com.github.xcfyl.drpc.core.pubsub;

import com.github.xcfyl.drpc.core.registry.DrpcProviderData;
import lombok.ToString;

import java.util.List;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 23:26
 */
@ToString
public class DrpcServiceChangeEventData {
    private String serviceName;
    private List<DrpcProviderData> newServiceList;

    public DrpcServiceChangeEventData() {

    }

    public DrpcServiceChangeEventData(String serviceName, List<DrpcProviderData> newServiceList) {
        this.serviceName = serviceName;
        this.newServiceList = newServiceList;
    }

    public String getServiceName() {
        return serviceName;
    }

    public List<DrpcProviderData> getNewServiceList() {
        return newServiceList;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setNewServiceList(List<DrpcProviderData> newServiceList) {
        this.newServiceList = newServiceList;
    }
}
