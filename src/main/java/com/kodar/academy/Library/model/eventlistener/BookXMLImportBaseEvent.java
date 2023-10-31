package com.kodar.academy.Library.model.eventlistener;

public class BookXMLImportBaseEvent {

    private final String zipFileName;
    private final String xmlFileName;
    private final String message;

    public BookXMLImportBaseEvent(String zipFileName, String xmlFileName, String message) {
        this.zipFileName = zipFileName;
        this.xmlFileName = xmlFileName;
        this.message = message;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "BookXMLImportBaseEvent{" +
                "zipFileName='" + zipFileName + '\'' +
                ", xmlFileName='" + xmlFileName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
