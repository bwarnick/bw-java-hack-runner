package com.manta.homeyou;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bradwarnick on 11/17/16.
 */
public class HomeyouGetProsData {

    private File input_file;
    private File output_file;
    private String[] fields;
    private JsonNode pvals;
    private List<String> plist;
    private HomeyouConnection connection;
    private HomeyouReader reader;
    private HomeyouMapper mapper;
    private BufferedWriter fout;


    public HomeyouGetProsData() {
    }


    public void getData( HomeyouEndpoint endpoint ) throws IOException {
        // System.out.println( endpoint.toString() );
        input_file = endpoint.getInput_file();
        output_file = endpoint.getOutput_file();
        fields = endpoint.getFields();
        ObjectMapper map = new ObjectMapper();
        try {
            pvals = map.readTree( input_file );
            /*
            Add loop to iterate on fields if more than one field param
             */
            plist = pvals.findValuesAsText( fields[0] );
            connection = new HomeyouConnection( endpoint );
            fout = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( output_file ), "UTF8" ));
            reader = new HomeyouReader();
            // System.out.println( plist.toString());
            for( String s : plist ) {
                // System.out.println(s);
                connection.setUrl( new String[]{ s } );
                mapper = new HomeyouMapper( reader.getBufferedReader( connection ));
                while( mapper.hasMore ) {
                    //System.out.println( mapper.getMore() );
                    fout.write( mapper.getMore() );
                    fout.newLine();
                }
            }
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}