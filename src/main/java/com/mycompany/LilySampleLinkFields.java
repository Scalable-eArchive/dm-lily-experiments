package com.mycompany;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;


public class LilySampleLinkFields {
    public static void main(String[] args) {
        try {
            new LilySampleLinkFields().run();
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

        // <------------------- Link Fields ----------------------->
        // (1)
        Record record1 = table.newRecord();
        record1.setRecordType(new QName(BNS, "Book"));
        record1.setField(new QName(BNS, "title"), "Fishing 1");
        record1 = table.create(record1);

        // (2)
        Record record2 = table.newRecord();
        record2.setRecordType(new QName(BNS, "Book"));
        record2.setField(new QName(BNS, "title"), "Fishing 2");
        record2.setField(new QName(BNS, "sequel_to"), new Link(record1.getId())); 
        record2 = table.create(record2);

        // (3)
        Link sequelToLink = (Link)record2.getField(new QName(BNS, "sequel_to"));        //link them via sequel_to field
        RecordId sequelTo = sequelToLink.resolve(record2.getId(), repository.getIdGenerator()); //so the link is like a RecordID
        Record linkedRecord = table.read(sequelTo);
        System.out.println(linkedRecord.getField(new QName(BNS, "title")));

        
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
