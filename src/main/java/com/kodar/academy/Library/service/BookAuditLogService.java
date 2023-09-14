package com.kodar.academy.Library.service;

import com.kodar.academy.Library.model.eventlistener.BookUpdateEvent;

public interface BookAuditLogService {

    void createBookAuditLog(BookUpdateEvent bookUpdateEvent);

}
