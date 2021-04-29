package com.example.controller;

import com.example.common.ResultBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class TestController {

    @GetMapping("/index")
    public ResultBody index(){
        return ResultBody.success("基本操作");
    }
}
