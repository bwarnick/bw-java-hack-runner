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

public class MantaDedupeNamesTest {

  // private String inputs = "c:/io/matching/xhackathon_mc2_mcp_all_";
  private String inputs = "/Users/bradwarnick/io/xmc2_oh_sample_";
  private String outputs = "/Users/bradwarnick/io/xmc2_oh_sample_output_";
  private File name_words = new File( "conf/resources/data/name_words.ser" );
  private int counter = 0;
  private int dupecount = 0;
  private int segment = 100000;
  private int geo_precision = 18;
  private int nam_precision = 8;
  private int[][] precision = { { 18, 8 }, { 18, 12 }, { 18, 6 }, { 18, 4 }, { 22, 8 }, { 20, 8 }, { 18, 8 }, { 16, 8 }  };
  private String ptype = "todisc";


  public MantaDedupeNamesTest( ) {
  }

// step 1 load hash of Manta names (OH test)
  // step 2 run bedrock data against manta_names hash
  // step 2b export manta_ID, bedrock id, key
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
    
    //temporary
    output = new File( outputs + 2 + ".csv" );
    FileOutputStream fos = new FileOutputStream( output );
    BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );

    try {
      while ( j < 1 ) {
        geo_precision = precision[j][0];
        nam_precision = precision[j][1];
        console( "Processing file " + 2 );
        input = new File( inputs + 2 );
        console( "Reading file " + input.toString() );
        console( "Writing file " + output.toString() );
        FileInputStream fstream = new FileInputStream( input );
        DataInputStream in = new DataInputStream( fstream );
        BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
        HashMap<String, String> names = new HashMap<String, String>();
        HashMap<String, Integer> hits = new HashMap<String, Integer>();

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
          // add leastscore check;
          
          bline = cleaner.Clean( codat[1] );
          NameWords words = new NameWords( bline, nam_precision );
          
          // includes temporary fix for company names that have "Pipes", replace with "Tilde"
          hash = words.nameHash();
          hash = hash.replace( "|", "~" );
          if ( words.getLeastscore() > 10000 ) {
            geodna = GeoDNA.encode( Double.parseDouble( codat[2] ), Double.parseDouble( codat[3] ), 22 );
          }
          else {
            geodna = GeoDNA.encode( Double.parseDouble( codat[2] ), Double.parseDouble( codat[3] ), geo_precision );
          }
          hash = ( geodna + "oooooooooooooooooooooo" ).substring( 0, 22 ) + hash;

          if ( ptype == "todisc" ) {
            //bow.write( codat[0] + "|" + hash );
            bow.newLine();
          } else {
            if ( names.containsKey( hash ) ) {
              dupecount++;
              console( geo_precision + "/" + nam_precision + "|" + codat[0] + "|" + names.get( hash ) + "|" + hash );
              if( hits.get( hash ) == 1 ) {
                 bow.write(  geo_precision + "/" + nam_precision + "|" + names.get( hash ) + "|" + hash );
                 bow.newLine();
                 hits.put( hash, hits.get(hash) + 1 );
              }
              bow.write(  geo_precision + "/" + nam_precision + "|" + codat[0] + "|" + hash );
              bow.newLine();
            } else {
              names.put( hash, codat[0] );
              hits.put( hash, 1 );
            }
          }
          
          counter++;
          if ( counter % segment == 0 ) {
            //console( String.valueOf( counter / segment ) );
          }
        }
        console( "Results = " + dupecount );
        j++;
        br.close();
      }
        bow.flush();
        bow.close();
     // }

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
