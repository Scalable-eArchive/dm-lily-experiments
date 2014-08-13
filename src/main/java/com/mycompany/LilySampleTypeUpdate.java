package com.mycompany;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleTypeUpdate {
    public static void main(String[] args) {
        try {
            new LilySampleTypeUpdate().run();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void run() throws Exception {
        //
        // Instantiate Lily client
        //
        LilyClient lilyClient = new LilyClient(System.getProperty("zkConn", "localhost:2181"), 20000);
        LRepository repository = lilyClient.getDefaultRepository();
        
        String BNS = "book";
        
        TypeManager typeManager = repository.getTypeManager();
        
        // Work on the default 'record' table in the repo. (This is the only default table in the repo)
        LTable table = repository.getDefaultTable();
        
        
     // <----------- Creating Updates -------------->
        FieldType description = typeManager.createFieldType("BLOB", new QName(BNS, "description"), Scope.VERSIONED);
        FieldType authors = typeManager.createFieldType("LIST<STRING>", new QName(BNS, "authors"), Scope.VERSIONED);
        FieldType released = typeManager.createFieldType("DATE", new QName(BNS, "released"), Scope.VERSIONED);
        FieldType pages = typeManager.createFieldType("LONG", new QName(BNS, "pages"), Scope.VERSIONED);
        FieldType sequelTo = typeManager.createFieldType("LINK", new QName(BNS, "sequel_to"), Scope.VERSIONED);
        FieldType manager = typeManager.createFieldType("STRING", new QName(BNS, "manager"), Scope.NON_VERSIONED);
        FieldType reviewStatus = typeManager.createFieldType("STRING", new QName(BNS, "review_status"), Scope.VERSIONED_MUTABLE);
        
        RecordType book = typeManager.getRecordTypeByName(new QName(BNS, "Book"), null);

     // The order in which fields are added does not matter
        book.addFieldTypeEntry(description.getId(), false);
        book.addFieldTypeEntry(authors.getId(), false);
        book.addFieldTypeEntry(released.getId(), false);
        book.addFieldTypeEntry(pages.getId(), false);
        book.addFieldTypeEntry(sequelTo.getId(), false);
        book.addFieldTypeEntry(manager.getId(), false);
        book.addFieldTypeEntry(reviewStatus.getId(), false);

     // Now we call updateRecordType instead of createRecordType
        book = typeManager.updateRecordType(book);

        PrintUtil.print(book, repository);
       
       
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
