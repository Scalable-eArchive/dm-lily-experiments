package com.mycompany;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleUserSpecificIDRecordCreate {
    public static void main(String[] args) {
        try {
            new LilySampleUserSpecificIDRecordCreate().run();
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

        // <------------------- Creating UserSpecificID Records ----------------------->
        RecordId id = repository.getIdGenerator().newRecordId("lily-definitive-guide-3rd-edition");
        Record record = table.newRecord(id);
        
        // This is just a volatile helper, it is not stored in the record
        // Internally, QName object is still created, but here won't be needed for setRecordType() and setField().
        record.setDefaultNamespace(BNS);
        
        record.setRecordType("Book");
        record.setField("title", "Lily, the definitive guide, 3rd edition");
        record = table.create(record);

        PrintUtil.print(record, repository);
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
