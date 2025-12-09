package uk.gov.companieshouse.documentrender.utils;

import uk.gov.companieshouse.documentrender.model.Document;

public class DocumentUtils {

    public static Document createValidDocument() {
        Document document = new Document();
        document.setProperty("CompanyName", "Test Company Ltd");
        document.setProperty("CompanyNumber", "00006400");
        return document;
    }

}
