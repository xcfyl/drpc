package com.github.xcfyl.drpc.core.common.enums;

/**
 * panda rpc中的所有枚举类型都要实现该接口
 * 
 * @author 西城风雨楼
 * @date create at 2023/6/23 15:42
 */
public interface DrpcEnum {
    int getCode();
    String getDescription();
}
