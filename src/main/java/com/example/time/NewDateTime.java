package com.example.time;

import com.example.service.TouTiaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class NewDateTime {

    @Autowired
    private TouTiaoService touTiaoService;

    //    @Scheduled(cron = "0/5 * *  * * ? ")
    public void test() {
        log.info("测试定时任务！！！");
    }

    @Scheduled(cron = "0 0 14 * * ? ")
    public void index() {
        log.info("头条新闻更新！！！");
        touTiaoService.index();
    }


    @Scheduled(cron = "0 1/10 * * * ? *")
    public void contetn() {
        log.info("新闻详情更新！！！");
        touTiaoService.index();
    }
}
