package com.manta.kibana;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.manta.common.FileIO;
import com.manta.common.StreamToFile;

public class KibanaQuery {

  private File query = new File( "conf/resources/queries/kibana_search.json" );
  private HttpURLConnection connection;


  public KibanaQuery( ) {
  }


  public BufferedReader getBufferedReader( KibanaConnection kibana ) throws Exception {
    connection = kibana.getConnection();
    DataOutputStream writer = new DataOutputStream( connection.getOutputStream() );
    writer.writeBytes( FileIO.FileToStr( query ).replaceAll( "\n", "" ).replaceAll( " ", "" ) );
    if ( connection.getResponseCode() == 200 ) {
      return new BufferedReader( new InputStreamReader( connection.getInputStream() ), 8 * 1024 );
    } else
      return null;
  }


  public BufferedReader getBufferedReader( KibanaConnection kibana, String q ) throws Exception {
    connection = kibana.getConnection();
    DataOutputStream writer = new DataOutputStream( connection.getOutputStream() );
    writer.writeBytes( q );
    if ( connection.getResponseCode() == 200 ) {
      return new BufferedReader( new InputStreamReader( connection.getInputStream() ), 8 * 1024 );
    } else
      return null;
  }


  public void testBufferedReader( ) throws Exception {
    KibanaConnection kibana = new KibanaConnection();
    BufferedReader response = getBufferedReader( kibana );
    String line = "";
    StreamToFile.newWriter( new File( "c:/io/search_results.json" ) );
    while ( ( line = response.readLine() ) != null ) {
      StreamToFile.lineWriter( line );
      // System.out.println( line );
    }
    StreamToFile.closeWriter();
    response.close();
  }

}
