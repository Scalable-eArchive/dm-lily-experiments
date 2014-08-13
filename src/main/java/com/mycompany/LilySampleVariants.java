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


public class LilySampleVariants {
    public static void main(String[] args) {
        try {
            new LilySampleVariants().run();
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

        // <------------------- Blob variants ----------------------->
        /*
         * We generate a master ID that we will use for the two variants.
         */
        IdGenerator idGenerator = repository.getIdGenerator();
        RecordId masterId = idGenerator.newRecordId();

        // We create the variant properties for the English language variant. This is simply a map.
        Map<String, String> variantProps = new HashMap<String, String>();
        variantProps.put("language", "en");

        // We create the record ID for the English variant, consisting of the master record ID and the variant properties.
        RecordId enId = idGenerator.newRecordId(masterId, variantProps);

        // We create the actual record.
        Record enRecord = table.newRecord(enId);
        enRecord.setRecordType(new QName(BNS, "Book"));
        enRecord.setField(new QName(BNS, "title"), "Car maintenance");
        enRecord = table.create(enRecord);

        /* 
         * We do the same for the Dutch language variant. Just as illustration, we get the master record ID 
         * by retrieving it from the English variant. A shortcut notation is used to create the variant properties map.
         */
        RecordId nlId = idGenerator.newRecordId(enRecord.getId().getMaster(), Collections.singletonMap("language", "nl"));
        Record nlRecord = table.newRecord(nlId);
        nlRecord.setRecordType(new QName(BNS, "Book"));
        nlRecord.setField(new QName(BNS, "title"), "Wagen onderhoud");
        nlRecord = table.create(nlRecord);

        // We use the getVariants method to get the list of all variants sharing the same master record ID, and print them out.
        Set<RecordId> variants = table.getVariants(masterId);
        for (RecordId variant : variants) {
            System.out.println(variant);
        }
        
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
