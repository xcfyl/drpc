package com.github.xcfyl.drpc.provider.impl;

import com.github.xcfyl.drpc.api.ReplyService;
import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcService;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 19:50
 */
@DrpcService
public class ReplyServiceImpl implements ReplyService {
    @Override
    public String reply(String message) {
        System.out.println("被调用了一次");
        return message;
    }
}
