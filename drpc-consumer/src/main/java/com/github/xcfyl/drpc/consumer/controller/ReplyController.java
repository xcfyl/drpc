package com.github.xcfyl.drpc.consumer.controller;

import com.github.xcfyl.drpc.api.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 西城风雨楼
 * @date create at 2023/7/16 20:46
 */
@RestController
@ResponseBody
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @GetMapping("/reply")
    public String reply(@RequestParam("msg") String message) {
        System.out.println("收到一个请求");
        String reply = replyService.reply(message);
        System.out.println(reply);
        return reply;
    }
}
