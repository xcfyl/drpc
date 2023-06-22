package com.github.xcfyl.drpc.core.constants;

/**
 * 对注册数据的系统保留扩展属性定义
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 22:04
 */
public enum RegistryDataAttr {
    CREATE_TIME("createTime", "创建时间"),
    WEIGHT("weight", "权重"),
    TYPE("type", "类型(provider/consumer)");

    private final String attrName;
    private final String description;

    RegistryDataAttr(String attrName, String description) {
        this.attrName = attrName;
        this.description = description;
    }

    public String getAttrName() {
        return attrName;
    }

    public String getDescription() {
        return description;
    }
}
