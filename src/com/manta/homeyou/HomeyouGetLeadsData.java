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
            "2016-11",
            "2016-12",
            "2017-01",
            "2017-02"};
    private File idToPhone = new File("./conf/resources/homeyou/homeyou_leads_campaigns.json");
    private File result_file;
    private String[] params = { "" , "" };
    private String[] fields = { "month" , "phone_number" };
    private JsonNode cid;
    private List<String> idList;
    private List<String> nameList;
    private List<String> ivrList;
    private List<String> phoneList;
    private List<String> poolList;
    private ObjectMapper map;
    private HomeyouConnection connection;
    private HomeyouReader reader;
    private HomeyouMapper mapper;
    private BufferedWriter fout;

    public HomeyouGetLeadsData() {
    }

    public void getData( HomeyouEndpoint p ) throws IOException {
        HomeyouEndpoint endpoint = p;
        map = new ObjectMapper();
        try {
            cid = map.readTree( idToPhone );
            idList = cid.findValuesAsText( "campaign" );
            nameList = cid.findValuesAsText( "campaign_name" );
            poolList = cid.findValuesAsText( "pool_root" );
            ivrList = cid.findValuesAsText( "ivr_name" );
            phoneList = cid.findValuesAsText( "phone_number" );
            connection = new HomeyouConnection( endpoint );
            int i = 0;
            while( i < 9 ) {
            //while( i < months.length ) {
                result_file = new File( endpoint.getOutput_file() + months[i].replace( "-", "" ) + ".json" );
                fout = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( result_file ), "UTF8" ) );
                params[0] = months[i];
                int j = 0;
                while ( j < idList.size() )  {
                //while ( j < 12 ) {
                    if( phoneList.get(j).equals( "0" )) {
                        params[1] = idList.get(j);
                        fields[1] = "campaign";
                    } else {
                        params[1] = phoneList.get(j);
                        fields[1] = "phone_number";
                    };
                    System.out.println(j + " " + fields[0] + "=" + params[0] + " " + fields[1] + "=" + params[1]);
                    connection.setUrl( fields, params );
                    reader = new HomeyouReader();
                    mapper = new HomeyouMapper(reader.getBufferedReader(connection), idList.get(j),
                                nameList.get(j), poolList.get(j), ivrList.get(j), phoneList.get(j));
                    while (mapper.hasMore) {
                        //String output = mapper.getNext();
                        //System.out.println( output );
                        //fout.write( output );
                        fout.write(mapper.getNext());
                        fout.newLine();
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