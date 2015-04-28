package com.manta.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;

public class JsonInputStreamParser {

  private JsonParser parser;
  private JsonToken current;
  private String fieldname;
  // private BufferedReader myreader;
  private File test = new File( "conf/resources/samples/kibana_query_sample_15.json" );
  private int counter = 0;

  /*
   * JsonInputStreamParser - is hard-coded to parse Kibana search results, generalize later Kibana query results Json
   * String BufferedReader stream in -> parse json to re
   */
  public JsonInputStreamParser( BufferedReader reader ) throws Exception {
    // myreader = reader;
    // setInputStream( reader );
    JsonFactory factory = new MappingJsonFactory();
    parser = factory.createParser( reader );
    current = parser.nextToken();
  }

  public Boolean hasMore( ) {
    if ( ( ( current == JsonToken.END_ARRAY ) && ( fieldname == "hits" ) ) ) {
      return false;
    } else
      return true;
  }

  // private void setInputStream( BufferedReader reader ) throws JsonParseException, IOException {
  // JsonFactory factory = new MappingJsonFactory();
  // parser = factory.createParser( reader );
  // }

  public void testParse( ) throws JsonParseException, IOException {
    current = parser.nextToken();
    fieldname = parser.getCurrentName();
    while ( !( current == null ) ) {
      testWriter( current + " - " + fieldname );
      current = parser.nextToken();
      fieldname = parser.getCurrentName();
    }
  }

  public Map<String, String> getFields( ) throws Exception {
    JsonNode node = null;
    // while ( !( ( current == JsonToken.END_OBJECT ) && ( fieldname == "hits" ) ) ) {
    while ( !( current == null ) ) {
      current = parser.nextToken();
      fieldname = parser.getCurrentName();
      // testWriter( current + " - " + fieldname );
      if ( ( ( current == JsonToken.START_OBJECT ) && ( fieldname == "fields" ) ) ) {
        node = parser.readValueAsTree();
        current = parser.nextToken();
        fieldname = parser.getCurrentName();
        return jsonNodeIterator( node );
      }
    }
    return null;
  }

  public Map<String, String> jsonNodeIterator( JsonNode n ) throws Exception {
    JsonNode node = n;
    JsonNode array = null;
    String key = "";
    Iterator<Map.Entry<String, JsonNode>> fieldIterator = node.fields();
    Map<String, String> map = new HashMap<String, String>();
    Map.Entry<String, JsonNode> field = null;
    while ( fieldIterator.hasNext() ) {
      field = fieldIterator.next();
      key = field.getKey().replace( '@', '_' ).replace( '.', '_' ).replace( '-', '_' ).toLowerCase();
      array = field.getValue();
      map.put( key, array.get( 0 ).asText() );
    }
    // testWriter( map.toString() );
    counter++;
    // testWriter( "rowcount:" + counter );
    return map;
  }

  public void closeParser( ) throws Exception {
    parser.close();
  }

  private void testWriter( String s ) {
    System.out.println( s );
  }

} // end
