package com.github.xcfyl.drpc.api;

import com.github.xcfyl.drpc.springboot.starter.annotation.DrpcReference;

/**
 * @author 西城风雨楼
 */
@DrpcReference
public interface ReplyService {
    String reply(String message);
}
