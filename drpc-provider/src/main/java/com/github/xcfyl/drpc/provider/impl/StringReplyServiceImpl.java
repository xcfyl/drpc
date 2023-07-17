package com.github.xcfyl.drpc.provider.impl;

import com.github.xcfyl.drpc.api.StringReplyService;
import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcService;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 19:50
 */
@DrpcService
public class StringReplyServiceImpl implements StringReplyService {
    @Override
    public String reply(String message) {
        return message;
    }
}
