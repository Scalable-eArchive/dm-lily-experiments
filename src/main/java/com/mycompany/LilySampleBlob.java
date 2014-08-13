package com.mycompany;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;


public class LilySampleBlob {
    public static void main(String[] args) {
        try {
            new LilySampleBlob().run();
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

        // <------------------- Blob ----------------------->
        //
        // Write a blob
        //

        String description = "<html><body>This book gives thorough insight into Lily, ...</body></html>";
        byte[] descriptionData = description.getBytes("UTF-8");

        /* To store a blob in the repository, you first create a Blob object. You need to specify the size of the blob, 
         * the repository will use this to determine where to store the blob. Then you request an output stream to upload the blob 
         * via repository.getOutputStream(blob), and write all the data to it. Finally, the output stream is closed, 
         * at that moment the repository will update the Blob object with a reference to the storage location that sits behind 
         * the output stream.
         */
        Blob blob = new Blob("text/html", (long)descriptionData.length, "description.xml");
        OutputStream os = table.getOutputStream(blob);
        try {
            os.write(descriptionData);
        } finally {
            os.close();
        }

        // Once the blobs are uploaded, you can create the record object as usual, setting the blob field (here description) 
        // with the blob object, and then call repository.create() to create the record on the repository.
         RecordId id = repository.getIdGenerator().newRecordId("lily-definitive-guide-3rd-edition");
         Record record = table.newRecord(id);
         record.setField(new QName(BNS, "description"), blob);
         record = table.update(record);

//         PrintUtil.print(record, repository);
         
         //
         // Read a blob
         //
         InputStream is = null;
         try {
             is = table.getInputStream(record, new QName(BNS, "description"));
             System.out.println("Data read from blob is:");
             Reader reader = new InputStreamReader(is, "UTF-8");
             BufferedReader br = new BufferedReader(reader);
             System.out.println(br.readLine());
/*             
             char[] buffer = new char[20];
             int read;
             while ((read = reader.read(buffer)) != -1) {
                 System.out.print(new String(buffer, 0, read));
             }
             System.out.println();
*/             
         } finally {
             if (is != null) {
                 is.close();
             }
         }        
        
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
