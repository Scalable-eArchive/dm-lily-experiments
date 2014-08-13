package com.mycompany;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleRecordCreate {
    public static void main(String[] args) {
        try {
            new LilySampleRecordCreate().run();
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

        // <------------------- Creating Records ----------------------->
        /*
         * First we create a Record object. 
         * Again, this creates nothing in the repository yet, this is only a factory method, since Record is an interface.
         */
        Record record = table.newRecord();
        
        /*
         * We set the record type for the record. The second argument is the version, specified as a Long object. 
         * Setting it to null will cause the last version of the record type to be used, which is usually what you want. 
         * This argument is optional, shown here only to explain it, in further examples we will leave it off.
         */
        record.setRecordType(new QName(BNS, "Book"));

        /*
         * We set a field on the record. This is done by specifying its name and its value. 
         * The value argument is an Object, the actual type of value required depends on the value type of the field type.
         */
        record.setField(new QName(BNS, "title"), "Lily, the definitive guide, 3rd edition");

        /*
         * Create the record in the repository. The updated record is returned, 
         * which will contain the record ID and version assigned by the repository.
         */
        record = table.create(record);
        
        /*
         * We use the PrintUtil to dump the record to screen, the output is shown below.
         */
        PrintUtil.print(record, repository);
       
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
