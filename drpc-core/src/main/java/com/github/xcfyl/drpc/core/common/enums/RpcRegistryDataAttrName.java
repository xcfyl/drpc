package com.github.xcfyl.drpc.core.common.enums;

/**
 * 对注册数据的系统保留扩展属性定义
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 22:04
 */
public enum RpcRegistryDataAttrName implements RpcEnum {
    CREATE_TIME(0, "createTime"),
    WEIGHT(1, "weight"),
    TYPE(2, "type");

    private final int code;
    private final String description;

    RpcRegistryDataAttrName(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public static RpcRegistryDataAttrName fromCode(int code) {
        for (RpcRegistryDataAttrName attrName : values()) {
            if (attrName.code == code) {
                return attrName;
            }
        }
        throw new RuntimeException("code转RegistryDataAttrName失败");
    }

    public static RpcRegistryDataAttrName fromDescription(String description) {
        for (RpcRegistryDataAttrName attrName : values()) {
            if (attrName.description.equals(description)) {
                return attrName;
            }
        }
        throw new RuntimeException("description转RegistryDataAttrName失败");
    }
}
