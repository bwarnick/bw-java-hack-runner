package com.manta.bigquery;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

public class BigquerySchema {

  public static void testParse( File file ) throws JsonParseException, IOException {
    JsonFactory factory = new MappingJsonFactory();
    JsonParser parser = factory.createParser( file );
    JsonToken current = parser.nextToken();
    String fieldname = parser.getCurrentName();
    while ( !( current == null ) ) {
      System.out.println( current + " - " + fieldname );
      current = parser.nextToken();
      fieldname = parser.getCurrentName();
    }
  }

  public static TableSchema getTableSchema( File file ) throws JsonParseException, IOException {
    TableSchema tableschema = new TableSchema();
    // TableFieldSchema tablefieldschema = new TableFieldSchema();
    List<TableFieldSchema> tablefieldschema = new ArrayList<TableFieldSchema>();
    TableFieldSchema schemaEntry;
    JsonFactory factory = new MappingJsonFactory();
    Iterator<Entry<String, JsonNode>> fieldIterator;
    Entry<String, JsonNode> field;
    JsonParser parser = factory.createParser( file );
    Map<String, String> map = new HashMap<String, String>();
    JsonNode node;
    JsonToken current = parser.nextToken();
    while ( current != JsonToken.START_ARRAY ) {
      current = parser.nextToken();
    }
    current = parser.nextToken();
    while ( current != JsonToken.END_ARRAY ) {
      if ( current == JsonToken.START_OBJECT ) {
        node = parser.readValueAsTree();
        current = parser.nextToken();
        fieldIterator = node.fields();
        schemaEntry = new TableFieldSchema();
        while ( fieldIterator.hasNext() ) {
          field = fieldIterator.next();
          // System.out.println( field.getKey() + " - " + field.getValue().toString() );
          // String key = "\"" + field.getKey() + "\"";
          // schemaEntry.set( key, field.getValue().toString() );
          // map.put( field.getKey(), field.getValue().toString() );
          // *
          if ( field.getKey() == "name" )
            schemaEntry.setName( field.getValue().toString() );
          if ( field.getKey() == "type" )
            schemaEntry.setType( field.getValue().toString() );
          if ( field.getKey() == "mode" )
            schemaEntry.setType( field.getValue().toString() );
          // */
        }
        tablefieldschema.add( schemaEntry );
      }
      tableschema.setFields( tablefieldschema );
    }
    // System.out.println( tableschema.toPrettyString() );
    parser.close();
    return tableschema;
  }
}
