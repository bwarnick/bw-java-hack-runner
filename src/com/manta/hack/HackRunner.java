package com.manta.hack;

import java.io.File;

import com.manta.kibana.KibanaQuerySequence;

public class HackRunner {

  public static void main( String[] args ) throws Exception {
    // TransEmid.run(1);
    // MongoExtract.run(1);
    // BigqueryDataLoad.createTable();
    // BigqueryLoader.setProject();
    // KibanaToBigquery query = new KibanaToBigquery();
    // query.runLoader();
    // HacksonInputParser parser = new HacksonInputParser();
    // parser.fileParse();
    // parser.parseLocalBlox();
    // parser.testParse();
    // KibanaQuery.testBufferedReader();
    // BigqueryDataLoad.streamingInsert( "test" );
    // BigqueryDataLoad.test();
    File query = new File( "conf/resources/queries/kibana_search_2.json" );
    KibanaQuerySequence seq = new KibanaQuerySequence( query, "04-13-15 00:00:000", "04-14-15 00:00:000", 2 );
    while ( seq.hasNext() ) {
      System.out.println( seq.getNextQuery() );
      // System.out.println( current );
    }
  }
}
