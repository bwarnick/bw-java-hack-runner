package com.manta.bigquery;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.google.api.services.bigquery.model.TableDataInsertAllRequest;
import com.google.api.services.bigquery.model.TableDataInsertAllRequest.Rows;
import com.google.api.services.bigquery.model.TableDataInsertAllResponse;

public class BigqueryInsert implements Runnable {

  private int count;
  private Configs conf;
  private List<Rows> rowlist;
  private TableDataInsertAllRequest content;
  private TableDataInsertAllResponse response;

  public BigqueryInsert( Configs c, List<Rows> r, int i ) {
    conf = c;
    rowlist = r;
    count = i;
  }


  public void run( ) {
    Long datevalue = System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy HH:mm:ss.SSS" );
    String date = sdf.format( datevalue );
    testWriter( date + " - " + Thread.currentThread().getName() + " Start insert: " + count + " - " + rowlist.size() );
    content = new TableDataInsertAllRequest().setRows( rowlist );
    try {
      response = conf.bigquery.tabledata().insertAll( conf.projectId, conf.datasetId, conf.tableId, content ).execute();
      testWriter( date + " - " + Thread.currentThread().getName() + " End insert:   " + count + " - "
          + response.toString() );
    } catch ( IOException e ) {
      e.printStackTrace();
    }
  }

  private void testWriter( String s ) {
    System.out.println( s );
  }
}
