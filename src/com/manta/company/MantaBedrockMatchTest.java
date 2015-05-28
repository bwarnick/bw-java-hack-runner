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

public class MantaBedrockMatchTest {

   // private String inputs = "c:/io/matching/xhackathon_mc2_mcp_all_";
   private String[] mode = { "/Users/bradwarnick/io/xmc2_oh_sample_1", "/Users/bradwarnick/io/xmc2_oh_sample_2" };
   private String output_result = "/Users/bradwarnick/io/xmc2_oh_sample_output_bedrock_dmetaphone.csv";
   private File name_words = new File( "conf/resources/data/name_words.ser" );
   private int counter = 0;
   private int dupecount = 0;
   private int segment = 100000;
   private int geo_precision = 16;
   private int nam_precision = 8;
   private int[][] precision = { { 16, 16 } };
   // private int[][] precision = { { 22, 4 }, { 20, 4 }, { 18, 4 }, { 16, 4 }, { 22, 8 }, { 20, 8 }, { 18, 8 },
   //      { 16, 8 }, { 22, 12 }, { 20, 12 }, { 18, 12 }, { 16, 12 }, { 16, 6 } };

   public MantaBedrockMatchTest( ) {
   }

   public void nameFileParser( ) throws JsonParseException, IOException, ClassNotFoundException {
      console( "Start" );
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
      int i = 0;
      int j = 0;

      // temporary
      output = new File( output_result );
      FileOutputStream fos = new FileOutputStream( output );
      BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );
      HashMap<String, String> names = new HashMap<String, String>();

      try {
         while ( i < precision.length ) {
            names = new HashMap<String, String>();
            geo_precision = precision[i][0];
            nam_precision = precision[i][1];
            while ( j < mode.length ) {
               console( "Processing, precision" + " geo/name= " + geo_precision + "/" + nam_precision );
               input = new File( mode[j] );
               console( "Reading file " + input.toString() );
               console( "Writing file " + output.toString() );
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
                  // add leastscore check;

                  bline = cleaner.Clean( codat[1] );
                  NameWords words = new NameWords( bline, nam_precision );

                  // includes temporary fix for company names that have "Pipes", replace with "Tilde"
                  hash = words.nameHash();
                  hash = hash.replace( "|", "~" );

                  if ( words.getLeastscore() > 10000 ) {
                     geodna = GeoDNA.encode( Double.parseDouble( codat[2] ), Double.parseDouble( codat[3] ), 22 );
                  } else {
                     geodna = GeoDNA.encode( Double.parseDouble( codat[2] ), Double.parseDouble( codat[3] ),
                           geo_precision );
                  }
                  hash = ( geodna + "oooooooooooooooooooooo" ).substring( 0, 22 ) + hash;

                  if ( j == 1 ) {
                     // console( geo_precision + "/" + nam_precision + "|" + codat[0] + "|" + names.get( hash ) + "|" +
                     // hash );
                     if ( names.containsKey( hash ) ) {
                        dupecount++;
                        bow.write( geo_precision + "-" + nam_precision + "|" + codat[0] + "|" + names.get( hash ) + "|"
                              + hash );
                        bow.newLine();
                     }
                  } else {
                     if ( !names.containsKey( hash ) ) {
                        names.put( hash, codat[0] );
                     }
                     else {
                        dupecount++;
                     }
                  }

                  counter++;
                  if ( counter % segment == 0 ) {
                     // console( String.valueOf( counter / segment ) );
                  }
               }
               console( "Results = " + dupecount );
               j++;
               br.close();
               dupecount = 0;
            }
            j=0;
            i++;
         }
         bow.flush();
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
