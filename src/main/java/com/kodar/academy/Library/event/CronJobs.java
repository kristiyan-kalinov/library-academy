package com.kodar.academy.Library.event;

import com.kodar.academy.Library.service.BookService;
import com.kodar.academy.Library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CronJobs {

    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public CronJobs(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
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

    @Scheduled(cron = "0 30 3 1 * ?")
    private void autoImportBooks() throws IOException {
        bookService.xmlImport("src/main/resources/zips-to-import");
    }
}
