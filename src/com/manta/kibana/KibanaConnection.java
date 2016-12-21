package com.manta.kibana;

        import java.net.HttpURLConnection;
        import java.net.URL;

public class KibanaConnection {

  private static String surl = "http://elasticsearch.logger-elasticsearch.main.production.aws.ecnext.net/_search?pretty";
  private static String USER_AGENT = "Hazel";
  private static String requestmethod = "POST";
  private static String contenttype = "application/json";
  private static URL url;
  public static HttpURLConnection connection;

  public KibanaConnection( ) {
  }

  public HttpURLConnection getConnection( ) throws Exception {
    url = new URL( surl );
    connection = ( HttpURLConnection ) url.openConnection();
    connection.setRequestMethod( requestmethod );
    connection.setRequestProperty( "User-Agent", USER_AGENT );
    connection.setRequestProperty( "Content-Type", contenttype );
    connection.setDoOutput( true );
    return connection;
  }

}
