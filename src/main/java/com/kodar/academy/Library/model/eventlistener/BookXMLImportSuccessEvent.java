package com.kodar.academy.Library.model.eventlistener;

import com.kodar.academy.Library.model.constants.Constants;

public class BookXMLImportSuccessEvent extends BookXMLImportBaseEvent{
    public BookXMLImportSuccessEvent(String zipFileName, String xmlFileName) {
        super(zipFileName, xmlFileName, Constants.SUCCESS);
    }
}
