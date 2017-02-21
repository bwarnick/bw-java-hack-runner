package com.manta.hack;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manta.homeyou.*;

import java.io.File;


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
      // MantaBedrockMatchTest names = new MantaBedrockMatchTest();
      // names.nameFileParser();
      //NameWordRunner names = new NameWordRunner();
      //names.nameFileParser();
      //MongoExtract.run( 1 );

      //HOMEYOU pro_data update SAVE
      ObjectMapper jsonmapper = new ObjectMapper();
      HomeyouEndpoint endpoint = jsonmapper.readValue( new File( "./conf/resources/homeyou/homeyou_endpoint_pros" ), HomeyouEndpoint.class );
      HomeyouGetProsData job = new HomeyouGetProsData();
      job.getData( endpoint );
      //

      /* HOMEYOU leads_data update SAVE
      ObjectMapper jsonmapper = new ObjectMapper();
      HomeyouEndpoint endpoint = jsonmapper.readValue( new File( "./conf/resources/homeyou/homeyou_endpoint_leads" ), HomeyouEndpoint.class );
      HomeyouGetLeadsData job = new HomeyouGetLeadsData();
      job.getData( endpoint );
      */



      // Test KibanaQuerySequence
      /*
       * File query = new File( "conf/resources/queries/kibana_search_2.json" ); KibanaQuerySequence seq = new
       * KibanaQuerySequence( query, "04-13-15 00:00:000", "04-14-15 00:00:000", 24 ); while ( seq.hasNext() ) {
       * System.out.println( seq.getNextQuery() ); // System.out.println( current ); }
       */

   }
}
