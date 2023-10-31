package com.kodar.academy.Library.model.eventlistener;

public class BookXMLImportFailEvent extends BookXMLImportBaseEvent{
    public BookXMLImportFailEvent(String zipFileName, String xmlFileName, String message) {
        super(zipFileName, xmlFileName, message);
    }
}
