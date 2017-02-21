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
    private URL url;
    private StringBuilder url_template;
    private static String qmk = "?";
    private static String amp = "&";
    private static String eqs = "=";
    private static String plh = "<pval>";

    public HomeyouEndpoint(){
    }

    //https://affiliates.homeyou.com/api/report-by-day?token=936e04d1cddca0dcddf1063544c0d799fdf7004c24ca8fce7a331a65654a7572&month=<p>&campaign=<p>
    //https://affiliates.homeyou.com/api/contractors?token=936e04d1cddca0dcddf1063544c0d799fdf7004c24ca8fce7a331a65654a7572&status=<p>

    public URL getUrl() {
        return url;
    }

    public void setUrl( String[] f, String[] p ) {
        fields = f;
        setSurl();
        String t = url_template.toString();
        int i = 0;
        while (i < p.length) {
            t = t.replaceFirst(plh, p[i]);
            i++;
        }
        try {
            url = new URL(t);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void setSurl() {
        url_template = new StringBuilder( base_url );
        url_template.append( endpoint );
        url_template.append( qmk + token );
        int i = 0;
        while( i < fields.length ){
            url_template.append( amp + fields[i] + eqs + plh );
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

    //public String toString() {
    //    return getUrl().toString();
    //}
}

