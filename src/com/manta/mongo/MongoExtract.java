package com.manta.mongo;

import java.net.UnknownHostException;

import com.manta.common.StreamToFile;

public class MongoExtract {

   static String collection = "connections";

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
         break;
      }
   }

   // BasicDBObject query = new BasicDBObject( "prices.price.low", new BasicDBObject( "$type", 2 ));
   // BasicDBObject fields = new BasicDBObject( "prices.price.low", 1 );
   private static void mongoExtractor( ) throws UnknownHostException {
      MongoDatabase.setMongoClient();

      try {
         while ( MongoDatabase.cursor.hasNext() ) {
            // System.out.println( counter + " " + MongoToBQcleaner.mongoToBQ( cursor.next().toString(), collection ));
            StreamToFile.lineWriter( MongoToBQcleaner.mongoToBQ( MongoDatabase.cursor.next().toString(), collection ) );
         }
      } finally {
         MongoDatabase.cursor.close();
         StreamToFile.closeWriter();
      }
   }
}
