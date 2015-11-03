package com.manta.mongo;

import java.io.File;
import java.net.UnknownHostException;

import com.manta.common.StreamToFile;

public class MongoExtract {

   static String collection = "products";
   static Integer count = 0;

   public static void run( int d ) {
      System.out.println( "Running..." );
      switch ( d ) {
      case 1:
         try {
            mongoExtractor();
         } catch ( UnknownHostException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         System.out.println( "Done..." );
         break;
        
      }
   }

   // BasicDBObject query = new BasicDBObject( "prices.price.low", new BasicDBObject( "$type", 2 ));
   // BasicDBObject fields = new BasicDBObject( "prices.price.low", 1 );
   private static void mongoExtractor( ) throws UnknownHostException {
      MongoDatabase.setMongoClient();
      //Yikes, hacked this in to make mongo product extract work, don't remeber where this was supposed to be set???
      File f = new File("/Users/bradwarnick/io/mongo_products.json");
      StreamToFile.newWriter( f );

      try {
         while ( MongoDatabase.cursor.hasNext() ) {
         //while ( count < 1000 ) {
            count++;
            // System.out.println( counter + " " + MongoToBQcleaner.mongoToBQ( cursor.next().toString(), collection ));
            StreamToFile.lineWriter( MongoToBQcleaner.mongoToBQ( MongoDatabase.cursor.next().toString(), collection ));
            if( count % 10000 == 0 ) System.out.println( "Total rows = " + count );
         }
      } finally {
         MongoDatabase.cursor.close();
         StreamToFile.closeWriter();
         System.out.println( "Total rows = " + count );
      }
   }
}
