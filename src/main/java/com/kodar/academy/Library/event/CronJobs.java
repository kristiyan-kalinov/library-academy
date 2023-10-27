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

    @Scheduled(cron = "0 0 3 1 * ?")
    private void checkSubscriptions() {
        userService.checkSubscriptions();
    }

    @Scheduled(cron = "0 45 2 * * ?")
    private void tax() {
        userService.tax();
    }

    @Scheduled(cron = "0 30 2 * * ?")
    private void checkProlongedBooks() {
        userService.checkHasProlonged();
    }
}
