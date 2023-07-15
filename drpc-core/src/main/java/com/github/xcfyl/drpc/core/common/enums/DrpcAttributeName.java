package com.github.xcfyl.drpc.core.common.enums;

/**
 * 对注册数据的系统保留扩展属性定义
 *
 * @author 西城风雨楼
 * @date create at 2023/6/22 22:04
 */
public enum DrpcAttributeName implements DrpcEnum {
    CREATE_TIME(0, "createTime"),
    WEIGHT(1, "weight"),
    TYPE(2, "type");

    private final int code;
    private final String description;

    DrpcAttributeName(int code, String description) {
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

    public static DrpcAttributeName fromCode(int code) {
        for (DrpcAttributeName attrName : values()) {
            if (attrName.code == code) {
                return attrName;
            }
        }
        throw new RuntimeException("code转RegistryDataAttrName失败");
    }

    public static DrpcAttributeName fromDescription(String description) {
        for (DrpcAttributeName attrName : values()) {
            if (attrName.description.equals(description)) {
                return attrName;
            }
        }
        throw new RuntimeException("description转RegistryDataAttrName失败");
    }
}
