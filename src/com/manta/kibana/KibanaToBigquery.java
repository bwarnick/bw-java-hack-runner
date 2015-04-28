package com.manta.kibana;

import java.io.File;
import java.util.Map;

import com.manta.bigquery.BigqueryLoader;
import com.manta.common.JsonInputStreamParser;

public class KibanaToBigquery {

  public KibanaToBigquery( ) {
  }


  public void runLoader( ) throws Exception {
    File qfile = new File( "conf/resources/queries/kibana_search_2.json" );
    KibanaConnection kibana = new KibanaConnection();
    KibanaQuery query = new KibanaQuery();
    KibanaQuerySequence seq = new KibanaQuerySequence( qfile, "04-13-15 00:00:000", "04-14-15 00:00:000", 24 );
    while ( seq.hasNext() ) {
      JsonInputStreamParser parser = new JsonInputStreamParser( query.getBufferedReader( kibana, seq.getNextQuery() ) );
      BigqueryLoader loader = new BigqueryLoader();
      loader.createTable();
      Map<String, String> map = parser.getFields();
      while ( !( map == null ) ) {
        loader.buildRows( map );
        map = parser.getFields();
      }
      loader.closeLoader();
      parser.closeParser();
    }
  }

}// end
