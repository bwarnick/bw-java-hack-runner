package com.manta.homeyou;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by bradwarnick on 11/17/16.
 */
public class HomeyouMapper {

    private JsonParser parser;
    private JsonToken current;
    private StringBuilder newline;
    public boolean hasMore;
    private HomeyouLineItem line = new HomeyouLineItem();


    public HomeyouMapper( BufferedReader reader, String i, String n, String v, String p ) {
        line.setCampaignId( i );
        line.setCampaignName( n );
        line.setIvrName( v );
        line.setPhoneNumber( p );
        MappingJsonFactory factory = new MappingJsonFactory();
        try {
            parser = factory.createParser( reader );
            current = parser.nextToken();
            if( current != null ) hasMore = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public HomeyouMapper( BufferedReader reader ) {
        MappingJsonFactory factory = new MappingJsonFactory();
        try {
            parser = factory.createParser( reader );
            current = parser.nextToken();
            while ( current != JsonToken.START_ARRAY ) {
                current = parser.nextToken();
                if( current != null ) hasMore = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getMore() {
        newline = new StringBuilder();
        // map.
        try {
            while( hasMore && current != JsonToken.END_ARRAY ) {
                current = parser.nextToken();
                if( current == JsonToken.START_OBJECT ) {
                    newline.append( parser.readValueAsTree().toString() );
                    return newline.toString();
                }
            }
            hasMore = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // rewrite Leads process then deprecate this
    public String getNext() {
        try {
            while( !(current.toString().equals("VALUE_STRING") && parser.getCurrentName().equals("day")) && !current.toString().equals("END_ARRAY" )) {
                current = parser.nextToken();
            }
            if( current.toString() != "END_ARRAY" ) {
                line.setDate(parser.getText());
                current = parser.nextToken();
                current = parser.nextToken();
                line.setLeads(parser.getValueAsInt());
                current = parser.nextToken();
                current = parser.nextToken();
                line.setRevenue(parser.getFloatValue());
                current = parser.nextToken();
                current = parser.nextToken();
                if( current.toString() == "END_ARRAY" ) hasMore = false;
                return line.toString();
            } else {
                hasMore = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
}
