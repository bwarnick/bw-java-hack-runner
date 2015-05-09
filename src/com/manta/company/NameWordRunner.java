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

  private String inputs = "c:/io/matching/xhackathon_mc2_mcp_all_";
  private File output = new File( "c:/io/matching/mc2_name_words_dupes4.csv" );
  private File name_words = new File( "conf/resources/data/name_words.ser" );
  private HashMap<String, String> names = new HashMap<String, String>();
  private int counter = 0;
  private int segment = 100000;


  public NameWordRunner( ) {
  }


  public void nameFileParser( ) throws JsonParseException, IOException, ClassNotFoundException {
    console( "Start" );
    output.createNewFile();
    NameCleaner cleaner = new NameCleaner();
    @SuppressWarnings( "unused" )
    WordsHash hashmap = new WordsHash( name_words );
    File input;
    String[] codat;
    String geodna;
    String bline = "";
    String subline = "";
    String hash = "";
    int i = 0;
    int j = 0;

    FileOutputStream fos = new FileOutputStream( output );
    BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );

    try {
      while ( j < 4 ) {

        console( "Processing file " + j );
        input = new File( inputs + j );
        FileInputStream fstream = new FileInputStream( input );
        DataInputStream in = new DataInputStream( fstream );
        BufferedReader br = new BufferedReader( new InputStreamReader( in ) );

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
          geodna = GeoDNA.encode( Double.parseDouble( codat[2] ), Double.parseDouble( codat[3] ) );
          bline = cleaner.Clean( codat[1] );
          NameWords words = new NameWords( bline );
          hash = geodna + words.nameHash();
          // console( codat[0] + " - " + hash );
          if ( names.containsKey( hash ) ) {
            // console( codat[0] + "-" + names.get( hash ) + "-" + hash );
            bow.write( codat[0] + "-" + names.get( hash ) + "-" + hash );
            bow.newLine();
          } else {
            names.put( hash, codat[0] );
          }
          i++;
          counter++;
          if ( counter % segment == 0 ) {
            console( String.valueOf( counter / segment ) );
          }
        }
        j++;
        br.close();
      }
      bow.close();

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
