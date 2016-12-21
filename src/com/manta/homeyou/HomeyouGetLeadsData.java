package com.manta.homeyou;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * Created by bradwarnick on 11/17/16.
 */
public class HomeyouGetLeadsData {

    private String[] months = {"2016-04",
            "2016-05",
            "2016-06",
            "2016-07",
            "2016-08",
            "2016-09",
            "2016-10",
            "2016-11"};
    //private File idToPhone = new File("/Users/bradwarnick/Documents/workspace/bw-java-hack-runner/conf/resources/homeyou/homeyou_leads_campaigns.json");
    private File idToPhone = new File("./conf/resources/homeyou/homeyou_leads_campaigns.json");
    private String result_path = "/Users/bradwarnick/io/homeyou_revenue_id_to_phone_";
    private String test = "./conf/resources/homeyou/homeyou_endpoint_leads";
    private File result_file;
    private String[] params = { "month" , "campaign" };
    private JsonNode cid;
    private List<String> idList;
    private List<String> nameList;
    private List<String> ivrList;
    private List<String> phoneList;
    private ObjectMapper map;
    private HomeyouConnection connection;
    private HomeyouReader reader;
    private HomeyouMapper mapper;
    private BufferedWriter fout;


    public HomeyouGetLeadsData() {
    }

    public void getData() throws IOException {
        ObjectMapper jsonmapper = new ObjectMapper();
        HomeyouEndpoint endpoint = jsonmapper.readValue( new File( test ), HomeyouEndpoint.class );
        System.out.println( endpoint.toString() );
        map = new ObjectMapper();
        try {
            cid = map.readTree( idToPhone );
            idList = cid.findValuesAsText( "campaign" );
            nameList = cid.findValuesAsText( "name" );
            ivrList = cid.findValuesAsText( "ivr" );
            phoneList = cid.findValuesAsText( "phone" );
            connection = new HomeyouConnection( endpoint );
            //System.out.println( cid.toString());
            //System.out.println( idList.toString());
            //System.out.println( nameList.toString());
            int i = 0;
            while( i < 1 ) {
            //while( i < months.length ) {
                result_file = new File( result_path + months[i].replace( "-", "" ) + ".json" );
                fout = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( result_file ), "UTF8" ) );
                params[0] = "month" + "=" + months[i];
                int j = 0;
                //while ( j < idList.size() )  {
                while ( j < 2 ) {
                    if (idList.get( j ) != "0000") {
                        params[1] = "campaign" + "=" + idList.get(j);
                        System.out.println( params[0] + " " + params[1] );
                        connection.setParams( params );
                        reader = new HomeyouReader();
                        mapper = new HomeyouMapper(reader.getBufferedReader(connection), idList.get(j),
                                nameList.get(j), ivrList.get(j), phoneList.get(j));
                        while (mapper.hasMore) {
                            String output = mapper.getNext();
                            System.out.println( output );
                            //fout.write( output );
                            //fout.write(mapper.getNext());
                            //fout.newLine();
                        }
                    }
                    Thread.sleep( 5000 );
                    j++;
                }
                fout.flush();
                fout.close();
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}