package com.manta.homeyou;

    import java.net.MalformedURLException;
    import java.net.URL;
    import java.io.File;

/**
 * Created by bradwarnick on 12/9/16.
 */
public class HomeyouEndpoint {

    public String base_url;
    public String endpoint;
    public String token;
    public String[] fields;
    public String user_agent;
    public String request_method;
    public String content_type;
    public String input_file;
    public String output_file;

    private StringBuilder surl;
    private static String qmk = "?";
    private static String amp = "&";
    private static String eqs = "=";
    private static String plh = "<p>";

    public HomeyouEndpoint(){
    }

    //https://affiliates.homeyou.com/api/report-by-day?token=936e04d1cddca0dcddf1063544c0d799fdf7004c24ca8fce7a331a65654a7572&month=<p>&campaign=<p>
    //https://affiliates.homeyou.com/api/contractors?token=936e04d1cddca0dcddf1063544c0d799fdf7004c24ca8fce7a331a65654a7572&status=<p>

    public void setParams( String[] p ) {
        fields = p;
    }

    public URL getUrl() {
        if (surl == null) setSurl();
        try {
            return new URL( surl.toString() );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setUrl( String[] p ) {
        setSurl();
        int i = 0;
        while( i < p.length ) {
            surl = new StringBuilder( surl.toString().replaceFirst( plh, p[i]));
            i++;
        }
    }

    private void setSurl() {
        surl = new StringBuilder( base_url );
        surl.append( endpoint );
        surl.append( qmk + token );
        int i = 0;
        while( i < fields.length ){
            surl.append( amp + fields[i] + eqs + plh );
            i++;
        }
    }

    public String getUserAgent(){
        return user_agent;
    }

    public String getRequestMethod(){
        return request_method;
    }

    public String getContentType(){
        return content_type;
    }

    public File getInput_file() {
        return new File( input_file );
    }

    public File getOutput_file() { return new File( output_file ); }

    public String[] getFields() {
        return fields;
    }

    public String toString() {
        return getUrl().toString();
    }
}

