package com.github.xcfyl.drpc.consumer.controller;

import com.github.xcfyl.drpc.api.IntegerReplyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 20:46
 */
@RestController
@ResponseBody
public class ReplyController {
    @Resource
    private IntegerReplyService replyService;

    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @GetMapping("/reply")
    public String reply(@RequestParam("num") Integer num) {
        Integer reply = replyService.reply(num);
        int sum = atomicInteger.addAndGet(reply);
        System.out.println(sum);
        return String.valueOf(sum);
    }
}
