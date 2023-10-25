package com.kodar.academy.Library.event;

import com.kodar.academy.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CronJobs {

    private final UserService userService;

    @Autowired
    public CronJobs(UserService userService) {
        this.userService = userService;
    }

    //@Scheduled(cron = "0 0 3 1 * ?")
    @Scheduled(cron = "30 46 17 * * ?")
    private void checkSubscriptions() {
        userService.checkSubscriptions();
    }

    //@Scheduled(cron = "0 0 3 * * ?")
    @Scheduled(cron = "0 49 17 * * ?")
    private void tax() {
        userService.tax();
    }

    //@Scheduled(cron = "0 30 2 * * ?")
    @Scheduled(cron = "0 46 17 * * ?")
    private void checkProlongedBooks() {
        userService.checkHasProlonged();
    }
}
