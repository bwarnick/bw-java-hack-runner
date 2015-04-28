package com.manta.bigquery;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collection;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.BigqueryScopes;

/*
 * Sets a Bigquery object (com.google.api.services.bigquery.Bigquery) with credentials for accessing a specific Biguery project.
 * The Bigquery object can be used to access core methods for Bigquery API access to BQ data.
 * Currently hard coded security parameters for accessing Manta's Bigquery account.
 * Uses the "service" style implementation of Oauth2 library serviceAccount, privateKeyID, P12File from the Google Developers Console > Credentials
 */
public class BigqueryConnection {

  public BigqueryConnection( ) {
  }

  private java.util.List<String> SCOPES = Arrays.asList( BigqueryScopes.BIGQUERY );
  private HttpTransport TRANSPORT = new NetHttpTransport();
  private JsonFactory JSON_FACTORY = new JacksonFactory();
  private String serviceAccount = "61270651936-506qgq9c56ggs673vhn26utq6t1g844t@developer.gserviceaccount.com";
  private String privateKeyID = "93ab77aa1f42d1bbef9f02a4fa7c17be70d5f54e";
  private File p12File = new File( "conf/resources/bqcerts/Manta BigQuery-93ab77aa1f42.p12" );


  public Bigquery getBigquery( ) throws IOException, GeneralSecurityException {
    GoogleCredential credential = new GoogleCredential.Builder().setTransport( TRANSPORT )
        .setJsonFactory( JSON_FACTORY ).setServiceAccountId( serviceAccount ).setServiceAccountScopes(
            ( Collection<String> ) SCOPES ).setServiceAccountPrivateKeyId( privateKeyID )
        .setServiceAccountPrivateKeyFromP12File( p12File ).build();
    return new Bigquery.Builder( TRANSPORT, JSON_FACTORY, credential ).setApplicationName( "Manta Bigquery Service" )
        .setHttpRequestInitializer( credential ).build();
  }

}