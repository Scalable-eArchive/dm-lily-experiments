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
import org.lilyproject.util.repo.PrintUtil;

import com.google.common.collect.Lists;


public class LilySampleComplexFieldType {
    public static void main(String[] args) {
        try {
            new LilySampleComplexFieldType().run();
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

        // <------------------- Complex Field Type ----------------------->
        final String ANS = "article";

        // we create the field types and record type for storing an author
        FieldType name = typeManager.createFieldType("STRING", new QName(ANS, "name"), Scope.NON_VERSIONED);
        FieldType email = typeManager.createFieldType("STRING", new QName(ANS, "email"), Scope.NON_VERSIONED);

        RecordType authorType = typeManager.newRecordType(new QName(ANS, "author"));
        authorType.addFieldTypeEntry(name.getId(), true);
        authorType.addFieldTypeEntry(email.getId(), true);
        authorType = typeManager.createRecordType(authorType);

        /* 
         * we create the field types and record type for storing an article. Note the definition of the authors field: 
         * its value type is "LIST<RECORD<{article}author>>". By this we say we want the field to contain a list of records of type author.
         * Specifying the type for the record is optional, so you can also use simply LIST<RECORD>, 
         * which then allows to use any kind of record (the same list could contain different kinds of records). 
         * Of course, not all complex fields need to be lists, you can use simply "RECORD" as value type as well.
         */
        FieldType title = typeManager.createFieldType("STRING", new QName(ANS, "title"), Scope.NON_VERSIONED);
        FieldType authors = typeManager.createFieldType("LIST<RECORD<{article}author>>",
            new QName(ANS, "authors"), Scope.NON_VERSIONED);
        FieldType body = typeManager.createFieldType("STRING", new QName(ANS, "body"), Scope.NON_VERSIONED);

        RecordType articleType = typeManager.newRecordType(new QName(ANS, "article"));
        articleType.addFieldTypeEntry(title.getId(), true);
        articleType.addFieldTypeEntry(authors.getId(), true);
        articleType.addFieldTypeEntry(body.getId(), true);
        articleType = typeManager.createRecordType(articleType);

        /*
         * we create two author record objects. Attention: we create just objects, nothing is persisted in the repository! 
         * It is not necessary to call repository.create() for these objects.
         */
        Record author1 = table.newRecord();
        author1.setRecordType(authorType.getName());
        author1.setField(name.getName(), "Author X");
        author1.setField(name.getName(), "author_x@authors.com");

        Record author2 = table.newRecord();
        author2.setRecordType(new QName(ANS, "author"));
        author2.setField(name.getName(), "Author Y");
        author2.setField(name.getName(), "author_y@authors.com");

        /*
         * we create an article record. We set the value of the authors field to an ArrayList containing 
         * the author1 and author2 objects. Lists.newArrayList() is an utility method provided by the Guava library.
         */
        Record article = table.newRecord();
        article.setRecordType(articleType.getName());
        article.setField(new QName(ANS, "title"), "Title of the article");
        article.setField(new QName(ANS, "authors"), Lists.newArrayList(author1, author2));
        article.setField(new QName(ANS, "body"), "Body text of the article");
        article = table.create(article);

        PrintUtil.print(article, repository);
    
    }

    private static QName q(String name) {
        return new QName("com.mycompany", name);
    }
}
