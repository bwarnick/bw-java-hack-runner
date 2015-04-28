package com.manta.bigquery;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.api.services.bigquery.model.TableDataInsertAllRequest;
import com.google.api.services.bigquery.model.TableDataInsertAllRequest.Rows;
import com.google.api.services.bigquery.model.TableRow;

/* GOOGLE BIGQUERY STREAM LOAD LIMITS
 * Maximum row size: 20 KB
 * Maximum data size of all rows, per insert: 1 MB
 * Maximum rows per second: 10,000 rows per second, per table. Exceeding this amount will cause quota_exceeded errors. 
 * (For additional support up to 100,000 rows per second, per table, please contact a sales representative.) p
 * Maximum rows per request: 500
 * Maximum bytes per second: 10 MB per second, per table. Exceeding this amount will cause quota_exceeded errors.
 */

public class BigqueryLoader {

  protected BigqueryConnection connection;
  private Configs conf;
  private TableDataInsertAllRequest.Rows rows;
  private List<Rows> rowlist;
  private ExecutorService executor;
  private int count = 0;
  private int totalrows = 0;
  private String id;

  public BigqueryLoader( ) throws IOException, GeneralSecurityException {
    connection = new BigqueryConnection();
    conf = new Configs( connection );
    rowlist = new ArrayList<Rows>();
    executor = Executors.newFixedThreadPool( 20 );
  }

  public void createTable( ) throws IOException, GeneralSecurityException {
    // conf.bigquery.tables().insert( conf.projectId, conf.datasetId, conf.table ).execute();
  }

  public void buildRows( Map<String, String> m ) throws IOException, GeneralSecurityException, ClassNotFoundException {
    Map<String, String> map = m;
    TableRow row = new TableRow();
    id = map.remove( "x_request_id" );
    row.putAll( map );
    rows = new TableDataInsertAllRequest.Rows();
    rows.setInsertId( id );
    rows.setJson( row );
    rowlist.add( rows );
    totalrows++;
    if ( rowlist.size() == conf.rownum ) {
      insertRows();
    }
  }

  private void insertRows( ) throws IOException, GeneralSecurityException, ClassNotFoundException {
    if ( rowlist.size() > 0 ) {
      count++;
      BigqueryInsert insert = new BigqueryInsert( conf, rowlist, count );
      executor.execute( insert );
      rowlist = new ArrayList<Rows>();
    }
  }

  public void closeLoader( ) throws ClassNotFoundException, IOException, GeneralSecurityException {
    insertRows();
    executor.shutdown();
  }

  private void testWriter( String s ) {
    System.out.println( s );
  }
}