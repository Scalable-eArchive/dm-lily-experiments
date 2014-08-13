package com.mycompany;

import java.util.ArrayList;
import java.util.List;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleRecordUpdateConditionally {
    public static void main(String[] args) {
        try {
            new LilySampleRecordUpdateConditionally().run();
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

        // <------------------- Updating Records conditionally ----------------------->
        List<MutationCondition> conditions = new ArrayList<MutationCondition>();
        conditions.add(new MutationCondition(new QName(BNS, "manager"), "Manager Z"));

        RecordId id = repository.getIdGenerator().newRecordId("lily-definitive-guide-3rd-edition");
        Record record = table.read(id);
        record.setField(new QName(BNS, "manager"), "Manager P");
        record = table.update(record, conditions);

        System.out.println(record.getResponseStatus());

    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
