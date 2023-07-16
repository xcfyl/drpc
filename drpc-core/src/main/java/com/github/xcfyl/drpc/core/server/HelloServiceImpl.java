package com.github.xcfyl.drpc.core.server;

/**
 * @author 西城风雨楼
 * @date create at 2023/6/22 16:47
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public Integer hello(String num) {
        return num.length();
    }
}
