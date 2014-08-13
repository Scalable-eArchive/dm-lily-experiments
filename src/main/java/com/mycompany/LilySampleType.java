package com.mycompany;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleType {
    public static void main(String[] args) {
        try {
            new LilySampleType().run();
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
        
        // <----------- Creating Field and Record type -------------->
        /*
         * Get a reference to the value type we want to use for our field. 
         * The value type is specified as string (rather than an enum or so), 
         * as there are lots of variations possible and the value types are designed to be extensible.
         */
        ValueType stringValueType = typeManager.getValueType("STRING");
        
        /*
         * Create the field type object. Since FieldType is an interface, we cannot instantiate it directly. 
         * We want to keep our code implementation-independent, therefore we will not directly instantiate an implementation 
         * but use the factory method newFieldType(). This method does nothing more than instantiating a field type object, 
         * at this point nothing changes yet in the repository. The same holds for all methods in the API that start with "new".
         */
        FieldType title = typeManager.newFieldType(stringValueType, new QName(BNS, "title"), Scope.VERSIONED);
        
        /*
         * Create the field type in the repository. 
         * The updated field type object, in which in this case the ID of the field type will be assigned, is returned by this method.
         */
        title = typeManager.createFieldType(title);
        
        /*
         * Create the record type object. This is pretty much the same as step (2): it creates an object, but nothing yet in the repository. 
         * The field type is added to the record type. The boolean argument specifies if the field type is mandatory.
         */
        RecordType book = typeManager.newRecordType(new QName(BNS, "Book"));
        book.addFieldTypeEntry(title.getId(), true);
        
        /*
         * Create the record type. Similar to point (3), here the record type is actually created in the repository, 
         * the updated record type object is returned.
         */
        book = typeManager.createRecordType(book);

        
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
