package com.manta.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HacksonInputParser {

  private JsonParser parser;
  private JsonToken current;
  private String fieldname;
  private String fieldvalue;
  private File input = new File( "c:/io/localblox-sample-few.json" );
  private File output = new File( "c:/io/localblox-sample-subset3.txt" );
  // private Pattern nap = Pattern.compile( "(.*?Aggregating big data for ')(.*?)('  Closed Signal.*)" );
  // private Pattern luv = Pattern.compile( "(.*?Love score: )(.*?)(  LoveScore calculated)" );
  private Pattern id = Pattern.compile( "(^.*,\"ID\":)([0-9]*)(,.*$)" );
  // private Pattern nam = Pattern.compile( "(^.*,\"BusinessName\":\")(.*?)(\",.*$)" );
  // private Pattern add = Pattern.compile( "(^.*,\"Address\":\")(.*?)(\",.*$)" );
  // private Pattern cit = Pattern.compile( "(^.*,\"CityName\":\")(.*?)(\",.*$)" );
  // private Pattern sta = Pattern.compile( "(^.*,\"StateCode\":\")(.*?)(\",.*$)" );
  // private Pattern pho = Pattern.compile( "(^.*,\"Phone1\":\")(.*?)(\",.*$)" );
  // private Pattern hol = Pattern.compile( "(^.*,\"HolisticScore\":)([0-9]*.[0-9]*)(,.*$)" );
  // private Pattern frs = Pattern.compile( "(^.*,\"FreshnessScore\":)([0-9]*.[0-9]*)(,.*$)" );
  // private Pattern lat = Pattern.compile( "(^.*,\"Latitude\":)([0-9]*.[0-9]*)(,.*$)" );
  // private Pattern lon = Pattern.compile( "(^.*,\"Longitude\":)(.[0-9]*.[0-9]*)(,.*$)" );
  // private Pattern luv = Pattern.compile( "(^.*,\"LoveScore\":)([0-9]*.[0-9]*)(,.*$)" );
  private Pattern scode = Pattern.compile( "(^.*,\"CategorySICCode\":\")(.*?)(\".*$)" );
  private Pattern scate = Pattern.compile( "(^.*,\"CategorySICCategory\":\")(.*?)(\".*$)" );
  private Pattern scod8 = Pattern.compile( "(^.*,\"EightDigitSICCode\":\")(.*?)(\".*$)" );
  private Pattern scat8 = Pattern.compile( "(^.*,\"EightDigitSICCategory\":\")(.*?)(\".*$)" );
  private Pattern scod6 = Pattern.compile( "(^.*,\"SixDigitSICCode\":\")(.*?)(\".*$)" );
  private Pattern scat6 = Pattern.compile( "(^.*,\"SixDigitSICCategory\":\")(.*?)(\".*$)" );
  private Pattern url2 = Pattern.compile( "(^.*,\"Cross-validatedProfileUrl\":\")(.*?)(\".*$)" );
  private Pattern url1 = Pattern
      .compile( "(^.*,\"DirectoryandSocialMediaURLsString\":\".*?Link:)(http:....www.manta.com.*?)([;|,].*$)" );
  private Pattern period = Pattern.compile( "\\." );
  // Link:http:\/\/www.manta.com\/c\/mm2nvx9\/lamorte-burns-co-inc;
  private CharSequence mup = "/c/";
  private int counter = 0;


  // ,"ID":1829969,
  // "BusinessName":"Tea Here Now",
  // "Address":"1721 Webster St",
  // "CityName":"Oakland",
  // "StateCode":"CA",
  // "Phone1":"5108324832",
  // "Latitude":37.805871,
  // "Longitude":-122.267357,
  // "HolisticScore":43.1186962678974,
  // "FreshnessScore":17.8749173352267,
  // "LoveScore":90,
  // "Cross-validatedProfileUrl":"

  public HacksonInputParser( ) throws Exception {
    JsonFactory factory = new MappingJsonFactory();
    parser = factory.createParser( input );
    parser.configure( JsonParser.Feature.ALLOW_SINGLE_QUOTES, true );
    // current = JsonToken.START_OBJECT;
  }


  public void testParse( ) throws JsonParseException, IOException {
    current = parser.nextToken();
    fieldname = parser.getCurrentName();
    fieldvalue = parser.getValueAsString();
    int count = 0;
    while ( !( current == null ) ) {
      // testWriter( fieldname + "*\\*" + fieldvalue );
      // testWriter( current + "*\\*" + fieldname + "*\\*" + fieldvalue );
      // if ( current == JsonToken.FIELD_NAME ) {
      if ( fieldname == "ActivityTrace" ) {
        count = 0;
      }
      count++;
      // testWriter( count + "_" + current + "*\\*" + fieldname + "*\\*" + fieldvalue );
      testWriter( count + "_" + current );
      // }
      // testWriter( fieldname + "|" + fieldvalue );
      current = parser.nextToken();
      fieldname = parser.getCurrentName();
      fieldvalue = parser.getValueAsString();
    }
  }


  public void parseLocalBlox( ) throws JsonParseException, IOException {
    String field1 = "Sources.Key=Facebook";
    String[] fields;
    String target;
    int i = 0;
    int j = 0;
    current = parser.nextToken();
    fieldname = parser.getCurrentName();
    fieldvalue = parser.getValueAsString();
    while ( !( current == null ) ) {
      fields = field1.split( "\\." );
      target = fields[i];
      while ( !( fieldname == target ) ) {
        current = parser.nextToken();
        fieldname = parser.getCurrentName();
      }
    }
  }


  public void fileLineParser( ) throws JsonParseException, IOException {
    testWriter( "Start" );
    output.createNewFile();
    FileOutputStream fos = new FileOutputStream( output );
    BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );
    FileInputStream fstream = new FileInputStream( input );
    DataInputStream in = new DataInputStream( fstream );
    BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
    String bline = "";
    try {
      while ( ( bline = br.readLine() ) != null ) {
        counter++;
        // bow.write( parseString( bline ) );
        bow.newLine();
        testWriter( "line " + counter );
      }
    } catch ( Exception e ) {
      e.printStackTrace();
      // TODO Auto-generated catch block
    }
    testWriter( "Done" );
    br.close();
    bow.close();
  }


  public void parseMap( String line ) throws JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure( JsonParser.Feature.ALLOW_SINGLE_QUOTES, true );
    String json = mapper.writeValueAsString( line );
  }


  public Map<String, String> getFields( ) throws Exception {
    JsonNode node = null;
    // while ( !( ( current == JsonToken.END_OBJECT ) && ( fieldname == "hits" ) ) ) {
    while ( !( current == null ) ) {
      current = parser.nextToken();
      fieldname = parser.getCurrentName();
      // testWriter( current + " - " + fieldname );
      if ( ( ( current == JsonToken.FIELD_NAME ) && ( fieldname == "ActivityTrace" ) ) ) {
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
    // testWriter( "rowcount:" + counter );
    return map;
  }


  private String parseString( String t ) {
    String target = t;
    String result = "";
    String murl1 = "";
    // testWriter( target );
    /*
     * if ( napmatch.find() ) { result = napmatch.group( 2 ); result = result.replace( ", ", "|" ).replace( "(", ""
     * ).replace( ") ", "" ).replace( "-", "" ); } int i = 0; int j = 0; for ( i = 0; i < result.length(); i++ ) { if (
     * result.charAt( i ) == '|' ) j++; } for ( i = j; i < 4; i++ ) { result = result + "|"; }
     */
    result = result + matchOne( id.matcher( target ) ) + "|";
    // result = result + matchOne( nam.matcher( target ) ) + "|";
    // result = result + matchOne( add.matcher( target ) ) + "|";
    // result = result + matchOne( cit.matcher( target ) ) + "|";
    // result = result + matchOne( sta.matcher( target ) ) + "|";
    // result = result + matchOne( pho.matcher( target ) ) + "|";
    // result = result + matchOne( lat.matcher( target ) ) + "|";
    // result = result + matchOne( lon.matcher( target ) ) + "|";
    // result = result + matchOne( hol.matcher( target ) ) + "|";
    // result = result + matchOne( frs.matcher( target ) ) + "|";
    // result = result + matchOne( luv.matcher( target ) ) + "|";
    // result = result + matchOne( scode.matcher( target ) ) + "|";
    // result = result + matchOne( scate.matcher( target ) ) + "|";
    // result = result + matchOne( scod8.matcher( target ) ) + "|";
    // result = result + matchOne( scat8.matcher( target ) ) + "|";
    // result = result + matchOne( scod6.matcher( target ) ) + "|";
    // result = result + matchOne( scat6.matcher( target ) ) + "|";
    murl1 = matchOne( url1.matcher( target ) );
    if ( murl1 != "" ) {
      murl1 = murl1.replace( new Character( ( char ) 92 ).toString(), "" ).replace( "\"", "" );
    } else
      murl1 = matchOne( url2.matcher( target ) );
    if ( ( murl1.length() > 31 ) && ( murl1.contains( mup ) ) ) {
      result = result + Encryption.decryptMID( murl1.substring( 23, 30 ) );
    }
    result = result + "|" + murl1;
    return result;
  }


  private void parseWords( String w ) {
    String word = w;
    int i = 0;
    word.indexOf( ' ' );
  }


  private String matchOne( Matcher m ) {
    Matcher match = m;
    if ( match.find() )
      return match.group( 2 );
    else
      return "";
  }


  public void closeParser( ) throws Exception {
    parser.close();
  }


  private void testWriter( String s ) {
    System.out.println( s );
  }

} // end
