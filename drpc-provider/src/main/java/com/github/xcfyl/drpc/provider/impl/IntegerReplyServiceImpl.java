package com.github.xcfyl.drpc.provider.impl;

import com.github.xcfyl.drpc.api.IntegerReplyService;
import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcService;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 19:50
 */
@DrpcService
public class IntegerReplyServiceImpl implements IntegerReplyService {
    @Override
    public Integer reply(Integer num) {
        return num;
    }
}
