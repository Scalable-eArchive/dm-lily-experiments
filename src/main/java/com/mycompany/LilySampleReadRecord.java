package com.mycompany;

import java.util.ArrayList;
import java.util.List;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;


public class LilySampleReadRecord {
    public static void main(String[] args) {
        try {
            new LilySampleReadRecord().run();
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

        // <------------------- Reading Records ----------------------->
        RecordId id = repository.getIdGenerator().newRecordId("lily-definitive-guide-3rd-edition");
        /*
         * If we just supply an ID when reading a record, the latest version of the record is fully read. 
         * The Record.getField() method returns the value of the field 
         * (here again, you could make use of setDefaultNamespace to avoid using the QName objects). 
         * The signature of this method declares a return type of Object, so you need to cast it to the expected type.
         */
        Record record = table.read(id);
        String title = (String)record.getField(new QName(BNS, "title"));
        System.out.println(title);
        
        /*
         * We can specify a version number as second argument to read a specific version of the record
         */
        record = table.read(id, 1L);
        System.out.println(record.getField(new QName(BNS, "title")));
     
        /*
         * It is also possible to read just the fields of the record that we are interested in. 
         * This way, the others do not need to be decoded and transported to us.
         */
        record = table.read(id, 1L, new QName(BNS, "title"));
        System.out.println(record.getField(new QName(BNS, "title")));
        
        
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
