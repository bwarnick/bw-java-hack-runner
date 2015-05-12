package com.manta.company;

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

import com.fasterxml.jackson.core.JsonParseException;

public class NameWordRunner {

  // private String inputs = "c:/io/matching/xhackathon_mc2_mcp_all_";
  private String inputs = "c:/io/matching/xhackathon_mc2_mcp_all_";
  private String outputs = "c:/io/matching/bedrock_name_words_all_p_20_20_";
  private File name_words = new File( "conf/resources/data/name_words.ser" );
  private HashMap<String, String> names = new HashMap<String, String>();
  private int counter = 0;
  private int segment = 100000;
  private int geo_precision = 22;
  private int nam_precision = 26;
  private String ptype = "todisc";


  public NameWordRunner( ) {
  }


  public void nameFileParser( String t ) throws JsonParseException, IOException, ClassNotFoundException {
    console( "Start" );
    ptype = t;
    NameCleaner cleaner = new NameCleaner();
    @SuppressWarnings( "unused" )
    WordsHash hashmap = new WordsHash( name_words );
    File input;
    File output;
    String[] codat;
    String geodna;
    String bline = "";
    String subline = "";
    String hash = "";
    int j = 0;

    try {
      while ( j < 1 ) {

        console( "Processing file " + j );
        input = new File( inputs + j );
        output = new File( outputs + j + ".csv" );
        FileInputStream fstream = new FileInputStream( input );
        DataInputStream in = new DataInputStream( fstream );
        BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
        FileOutputStream fos = new FileOutputStream( output );
        BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );

        while ( ( bline = br.readLine() ) != null ) {
          // while ( i < 1000 ) {
          // bline = br.readLine();

          if ( bline.contains( "latitude" ) || ( bline.contains( "0,0" ) ) ) {
            bline = br.readLine();
          }
          if ( bline.contains( "\"" ) ) {
            bline = bline.replace( "$", "" );
            int b = bline.indexOf( "\"" );
            int e = bline.indexOf( "\"", b + 1 );
            subline = bline.substring( b + 1, e );
            subline = subline.replace( ",", " " );
            subline = subline.replace( "  ", " " );
            bline = bline.replaceAll( "\".*?\"", subline );
          }
          codat = bline.split( "," );
          codat[2] = codat[2].replaceAll( " ", "." );
          codat[3] = codat[3].replaceAll( " ", "." );
          geodna = GeoDNA.encode( Double.parseDouble( codat[2] ), Double.parseDouble( codat[3] ), geo_precision );
          bline = cleaner.Clean( codat[1] );
          NameWords words = new NameWords( bline, nam_precision );
          // includes temporary fix for company names that have "Pipes", replace with "Tilde"
          hash = geodna + words.nameHash().replace( "|", "~" );
          hash = hash.replace( "|", "~" );
          if ( ptype == "todisc" ) {
            // console( codat[0] + "|" + hash );
            bow.write( codat[0] + "|" + hash );
            bow.newLine();
          } else {
            if ( names.containsKey( hash ) ) {
              console( codat[0] + "|" + names.get( hash ) + "|" + hash );
              bow.write( codat[0] + "|" + names.get( hash ) + "|" + hash );
              bow.newLine();
            } else {
              names.put( hash, codat[0] );
            }
          }
          // i++;
          counter++;
          if ( counter % segment == 0 ) {
            console( String.valueOf( counter / segment ) );
          }
        }
        j++;
        br.close();
        bow.close();
      }

    } catch ( Exception e ) {
      System.out.println( subline );
      System.out.println( bline );
      e.printStackTrace();
      // TODO Auto-generated catch block
    }
    console( "Done" );
  }


  private void console( String s ) {
    System.out.println( s );
  }

} // end
