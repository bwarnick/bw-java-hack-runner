package com.manta.homeyou;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import com.manta.common.StreamToFile;

public class HomeyouReader {

  private HttpURLConnection connection;

  public HomeyouReader( ) {
  }

  public BufferedReader getBufferedReader( HomeyouConnection homeyou ) throws Exception {
    connection = homeyou.getConnection();
    DataOutputStream writer = new DataOutputStream( connection.getOutputStream() );
    if ( connection.getResponseCode() == 200 ) {
      return new BufferedReader( new InputStreamReader( connection.getInputStream() ), 8 * 1024 );
    } else
      return null;
  }

  public BufferedReader getBufferedReader(HomeyouConnection homeyou, String q ) throws Exception {
    connection = homeyou.getConnection();
    DataOutputStream writer = new DataOutputStream( connection.getOutputStream() );
    writer.writeBytes( q );
    if ( connection.getResponseCode() == 200 ) {
      return new BufferedReader( new InputStreamReader( connection.getInputStream() ), 8 * 1024 );
    } else
      return null;
  }

}
