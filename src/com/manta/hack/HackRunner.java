package com.manta.hack;

import com.manta.company.NameWordSampler;

public class HackRunner {

  public static void main( String[] args ) throws Exception {
    // TransEmid.run(1);
    // MongoExtract.run(1);

    // Run Bigquery loader
    // BigqueryDataLoad.createTable();
    // BigqueryLoader.setProject();
    // KibanaToBigquery query = new KibanaToBigquery();
    // query.runLoader();

    // HacksonStringParser parser = new HacksonStringParser();
    // parser.fileParse();
    // parser.parseLocalBlox();
    // parser.testParse();
    // KibanaQuery.testBufferedReader();
    // BigqueryDataLoad.streamingInsert( "test" );
    // BigqueryDataLoad.test();

    // NameWordBuilder hash = new NameWordBuilder();
    // hash.nameFileParser();
    NameWordSampler names = new NameWordSampler();
    names.nameFileParser( "tohash" );

    // Test KibanaQuerySequence
    /*
     * File query = new File( "conf/resources/queries/kibana_search_2.json" ); KibanaQuerySequence seq = new
     * KibanaQuerySequence( query, "04-13-15 00:00:000", "04-14-15 00:00:000", 24 ); while ( seq.hasNext() ) {
     * System.out.println( seq.getNextQuery() ); // System.out.println( current ); }
     */

  }
}
