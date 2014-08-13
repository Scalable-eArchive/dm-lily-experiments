package com.mycompany;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.lilyproject.client.LilyClient;
import org.lilyproject.repository.api.*;
import org.lilyproject.util.repo.PrintUtil;

import com.google.common.io.Files;


public class MyBlob {
    public static void main(String[] args) {
        try {
            new MyBlob().run();
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

        File file = new File("/home/funnyboy/Documents/lily/lily_2.0_documentation.pdf");
        byte[] fileData = Files.toByteArray(file);
        
//        String content = new Tika().parseToString(file);
        
//        System.out.println(content);
        
//        Metadata met = new Metadata();
        
        
        /* To store a blob in the repository, you first create a Blob object. You need to specify the size of the blob, 
         * the repository will use this to determine where to store the blob. Then you request an output stream to upload the blob 
         * via repository.getOutputStream(blob), and write all the data to it. Finally, the output stream is closed, 
         * at that moment the repository will update the Blob object with a reference to the storage location that sits behind 
         * the output stream.
         */
        Blob blob = new Blob("application/pdf", (long)fileData.length, "lily_2.0_documentation.pdf");
        OutputStream os = table.getOutputStream(blob);
        try {
            os.write(fileData);
        } finally {
            os.close();
        }

        // Once the blobs are uploaded, you can create the record object as usual, setting the blob field (here description) 
        // with the blob object, and then call repository.create() to create the record on the repository.
        RecordId id = repository.getIdGenerator().newRecordId("lily-content-"+ System.currentTimeMillis());
        Record record = table.newRecord(id);
        record.setRecordType(new QName(BNS, "Book"));
        record.setField(new QName(BNS, "title"), "Lily Content");
        record = table.create(record);
        
        
        record.setField("description", blob);
        record = table.update(record);

        PrintUtil.print(record, repository);
         
         //
         // Read a blob
         //
         InputStream is = null;
         OutputStream osfile = null;
         try {
             is = table.getInputStream(record, new QName(BNS, "description"));
             byte[] outputByteArray = IOUtils.toByteArray(is);
             
             osfile = new BufferedOutputStream(new FileOutputStream("/home/funnyboy/Documents/lily/out.pdf"));
             osfile.write(outputByteArray);

             /*             
             Reader reader = new InputStreamReader(is, "UTF-8");
             
             System.out.println("Data read from blob is:");
             
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
             if (osfile !=null)
                 osfile.close();
         }        
        
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
