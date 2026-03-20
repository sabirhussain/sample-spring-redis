package com.hcltech.sample.redis.monitor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ThreadMonitor {

    @Scheduled(fixedRate = 500)
    public void logActiveThreadCount() {
        int activeThreadCount = Thread.activeCount();
        log.info("Active threads: {}", activeThreadCount);
    }
}
