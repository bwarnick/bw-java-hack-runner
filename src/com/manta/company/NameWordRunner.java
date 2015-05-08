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

  private String inputs = "c:/io/matching/xhackathon_mc2_mcp_all00000000000";
  private File output = new File( "c:/io/mc2_name_words_all.csv" );
  private File name_words = new File( "conf/resources/data/name_words.ser" );
  private HashMap<String, String> names = new HashMap<String, String>();


  public NameWordRunner( ) {
  }


  public void nameFileParser( ) throws JsonParseException, IOException, ClassNotFoundException {
    console( "Start" );
    File input = new File( inputs + 0 + ".csv" );
    output.createNewFile();
    NameCleaner cleaner = new NameCleaner();
    @SuppressWarnings( "unused" )
    WordsHash hashmap = new WordsHash( name_words );
    String[] codat;
    String geodna;
    String bline = "";
    String hash = "";
    int i = 0;

    FileOutputStream fos = new FileOutputStream( output );
    BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );

    FileInputStream fstream = new FileInputStream( input );
    DataInputStream in = new DataInputStream( fstream );
    BufferedReader br = new BufferedReader( new InputStreamReader( in ) );

    try {
      // while ( ( bline = br.readLine() ) != null ) {
      while ( i < 1000000 ) {

        bline = br.readLine();
        codat = bline.split( "," );
        geodna = GeoDNA.encode( Long.parseLong( codat[2], 10 ), Long.parseLong( codat[3], 10 ) );
        bline = cleaner.Clean( codat[1] );
        NameWords words = new NameWords( bline );
        hash = geodna + words.nameHash();
        if ( names.containsKey( hash ) ) {
          console( codat[0] + "-" + names.get( hash ) + "-" + hash );
          bow.write( codat[0] + "-" + names.get( hash ) + "-" + hash );
          bow.newLine();
        } else {
          names.put( hash, codat[0] );
        }
        i++;
      }
    } catch ( Exception e ) {
      e.printStackTrace();
      // TODO Auto-generated catch block
    }
    console( "Done" );
    br.close();
    bow.close();
  }


  private void console( String s ) {
    System.out.println( s );
  }

} // end
