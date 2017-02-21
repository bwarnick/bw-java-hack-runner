package com.manta.homeyou;

        import java.net.HttpURLConnection;
        import java.net.URL;

public class HomeyouConnection {

  private URL url;
  private HomeyouEndpoint endpoint;
  private HttpURLConnection connection;


  public HomeyouConnection( HomeyouEndpoint e ) {
    this.setEndpoint( e );
  }

  public void setEndpoint( HomeyouEndpoint e ) {
    this.endpoint = e;
  }

  public void setUrl( String[] f, String[] p ) {
    this.endpoint.setUrl( f, p );
  }

  public URL getUrl() { return this.endpoint.getUrl(); }

  public HttpURLConnection getConnection( ) throws Exception {
    url = endpoint.getUrl( );
    connection = ( HttpURLConnection ) url.openConnection();
    connection.setRequestMethod( endpoint.getRequestMethod() );
    connection.setRequestProperty( "User-Agent", endpoint.getUserAgent() );
    connection.setRequestProperty( "Content-Type", endpoint.getContentType() );
    connection.setDoOutput( true );
    return connection;
  }

  //public String toString(){
  //  return endpoint.getUrl().toString();
  //}

}
