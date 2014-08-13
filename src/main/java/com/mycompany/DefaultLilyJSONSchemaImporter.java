package com.mycompany;

import java.io.IOException;
import java.io.InputStream;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.LRepository;
import org.lilyproject.repository.api.LTable;
import org.lilyproject.tools.import_.cli.JsonImport;

public class DefaultLilyJSONSchemaImporter {

    /**
     * TODO: Verify and further test this application. (See Lily 2.0 manual, page 154) 
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        
        try {        
            LilyClient lilyClient = new LilyClient(System.getProperty("zkConn", "localhost:2181"), 20000);
            LRepository repository = lilyClient.getDefaultRepository();
            
            // Work on the default 'record' table in the repo. (This is the only default table in the repo)
            LTable table = repository.getDefaultTable();        
            
            System.out.println("Importing schema");
            InputStream is = DefaultLilyJSONSchemaImporter .class.getResourceAsStream("schema.json"); // or create schema.json based on Lily definition and test it!
    //        JsonImport.load(repository, is, false);
    
            JsonImport.load(table, repository, is);
        
            is.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        
        System.out.println("Schema successfully imported");        
        
    }

}
