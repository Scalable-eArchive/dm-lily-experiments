package com.mycompany;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.tools.import_.cli.JsonImport;

import java.io.InputStream;

public class LilySample {
    public static void main(String[] args) throws Exception {
        new LilySample().run();
    }

    public void run() throws Exception {
        //
        // Instantiate Lily client
        //
        LilyClient lilyClient = new LilyClient(System.getProperty("zkConn", "localhost:2181"), 20000);
        LRepository repository = lilyClient.getDefaultRepository();
        
        // Work on the default 'record' table in the repo. (This is the only default table in the repo)
        LTable table = repository.getDefaultTable();
        
        //
        // Create a schema
        //
        // We do this using the import library: this is easier than writing
        // out the schema construction code in Java, and automatically handles
        // the case where the schema would already exist (on second run of this app).
        //
        System.out.println("Importing schema");
        InputStream is = LilySample.class.getResourceAsStream("schema.json");
        JsonImport.loadSchema(repository, is);
        is.close();
        System.out.println("Schema successfully imported");

        //
        // Create a record
        //
        System.out.println("Creating a record");
        Record record = table.newRecord();
        record.setId(repository.getIdGenerator().newRecordId());
        record.setRecordType(q("Type1"));
        record.setField(q("field1"), "value1");
        // We use the createOrUpdate method as that one can automatically recover
        // from connection errors (idempotent behavior, like PUT in HTTP).
        table.createOrUpdate(record);
        System.out.println("Record created: " + record.getId());
        
        lilyClient.close();
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
