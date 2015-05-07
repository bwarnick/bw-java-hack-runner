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
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonParseException;

public class CompanyNameParser {

  private File input = new File( "c:/io/mc2_name1_deduped.csv" );
  private File output = new File( "c:/io/mc2_name_words_test2.csv" );
  private int counter = 0;
  private int segment = 1000000;
  private String[][] reps = { { "http://", "" }, { "www.", "" }, { "!", "" }, { "\"", "" }, { "#", "" }, { "$", "" },
      { "%", "" }, { "&", " & " }, { "'", "" }, { "\\(", "" }, { "\\)", "" }, { "\\*", "" }, { "\\+", "" },
      { ",", "" }, { "\\-", " " }, { "\\.", "" }, { "/", "" }, { ":", "" }, { ";", "" }, { "<", "" }, { "=", "" },
      { ">", "" }, { "\\?", "" }, { "@", "" }, { "\\[", "" }, { "\\\\", "" }, { "\\]", "" }, { "^", "" }, { "_", " " },
      { "`", "" }, { "\\{", "" }, { "|", "" }, { "\\}", "" }, { "~", "" } };


  public CompanyNameParser( ) {
  }


  public void fileLineParser( ) throws JsonParseException, IOException {
    consoleWriter( "Start" );
    output.createNewFile();
    Hashtable<String, Integer> results = new Hashtable<String, Integer>();
    results.put( "manta", 1 );
    FileOutputStream fos = new FileOutputStream( output );
    BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );
    FileInputStream fstream = new FileInputStream( input );
    DataInputStream in = new DataInputStream( fstream );
    BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
    String[] words;
    String bline = "";
    try {
      // while ( ( bline = br.readLine() ) != null ) {
      while ( counter < 1000000 ) {
        bline = br.readLine();
        // consoleWriter( bline );
        counter++;
        bline = bline.toLowerCase();

        int j = 0;
        for ( j = 0; j < reps.length; j++ ) {
          // consoleWriter( reps[j][0] + " " + reps[j][1] );
          bline = bline.replaceAll( reps[j][0], reps[j][1] );
        }
        bline = bline.replaceAll( "  ", " " ).trim();
        // consoleWriter( bline );
        words = bline.split( " " );
        int i = 0;
        if ( words.length == 0 ) {
          words[0] = bline;
        }
        while ( i < words.length ) {
          bline = words[i];
          if ( results.containsKey( bline ) ) {
            results.put( bline, results.get( bline ) + 1 );
          } else {
            results.put( bline, 1 );
          }
          // consoleWriter( bline );
          i++;
        }
        if ( counter % segment == 0 ) {
          consoleWriter( String.valueOf( counter / segment ) );
        }
      }
      Iterator<Entry<String, Integer>> it = results.entrySet().iterator();
      while ( it.hasNext() ) {
        // Map.Entry pair = ( Map.Entry )it.next();
        Map.Entry<?, ?> pair = it.next();
        // consoleWriter( pair.getKey() + "|" + pair.getValue() );
        bow.write( pair.getKey() + "|" + pair.getValue() );
        bow.newLine();
      }
      // consoleWriter( results.toString() );
    } catch ( Exception e ) {
      System.out.println( bline );
      e.printStackTrace();
      // TODO Auto-generated catch block
    }
    consoleWriter( "Done" );
    br.close();
    bow.close();
  }


  private void consoleWriter( String s ) {
    System.out.println( s );
  }

} // end
