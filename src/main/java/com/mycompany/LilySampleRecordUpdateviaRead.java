package com.mycompany;

import java.util.Arrays;

import org.apache.tika.metadata.Metadata;
import org.joda.time.LocalDate;
import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleRecordUpdateviaRead {
    public static void main(String[] args) {
        try {
            new LilySampleRecordUpdateviaRead().run();
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

        // <------------------- Updating Records ----------------------->
        RecordId id = repository.getIdGenerator().newRecordId("lily-definitive-guide-3rd-edition");
        
        // Read existing record (and modify the data on it)
        Record record = table.read(id);
        
        record.setDefaultNamespace(BNS);
//        record.setField("released", new LocalDate());
//        record.setField("authors", Arrays.asList("Author A", "Author B"));
        
        record.setVersion(1L);      // here we can switch back to previous versions
        record.setField("review_status", "v2");
        record.setField("manager", "Manager Z");
        
        // Here we do modify (update) the table
        record = table.update(record, true, false);


        PrintUtil.print(record, repository);

    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
