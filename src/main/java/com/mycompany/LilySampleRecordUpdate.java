package com.mycompany;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleRecordUpdate {
    public static void main(String[] args) {
        try {
            new LilySampleRecordUpdate().run();
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
        
        // This instantiates the record
        Record record = table.newRecord(id);
        
        record.setDefaultNamespace(BNS);
        record.setField("title", "Lily, the definitive guide, third edition");
        record.setField("pages", Long.valueOf(912));
        record.setField("manager", "Manager M");
        record = table.update(record);

        PrintUtil.print(record, repository);

    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
