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
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.core.JsonParseException;

public class MantaBedrockMatchTest {

   // private String inputs = "c:/io/matching/xhackathon_mc2_mcp_all_";
   private String[] source = { "/Users/bradwarnick/io/manta_43015_source1.csv",
         "/Users/bradwarnick/io/bedrock_43015_source2.csv" };
   private String output_result = "/Users/bradwarnick/io/manta_bedrock_43015_result.csv";
   private File name_words = new File( "conf/resources/data/name_words.ser" );
   private int counter = 0;
   private int dupecount = 0;
   private int segment = 100000;
   private int geo_precision = 22;
   private int nam_precision = 32;
   private int[][] precision = { { 16, 3 } };

   // private int[][] precision = { { 22, 4 }, { 20, 4 }, { 18, 4 }, { 16, 4 }, { 22, 8 }, { 20, 8 }, { 18, 8 },
   // { 16, 8 }, { 22, 12 }, { 20, 12 }, { 18, 12 }, { 16, 12 }, { 16, 6 } };

   public MantaBedrockMatchTest( ) {
   }

   public void nameFileParser( ) throws JsonParseException, IOException, ClassNotFoundException {
      console( "Start" );
      NameCleaner cleaner = new NameCleaner();
      @SuppressWarnings( "unused" )
      WordsHash hashmap = new WordsHash( name_words );
      // CSVParser line = null;
      File input;
      File output;
      String[] codat = new String[8];
      String geodna;
      String inline = "";
      //String outline = "";
      //String subline = "";
      String hash = "";
      int i = 0;
      int j = 0;

      // temporary
      output = new File( output_result );
      FileOutputStream fos = new FileOutputStream( output );
      BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );
      HashMap<String, String> names = new HashMap<String, String>();
      ArrayList<CSVRecord> list = null;
      CSVRecord record = null;

      try {
         while ( i < precision.length ) {
            names = new HashMap<String, String>();
            geo_precision = precision[i][0];
            nam_precision = precision[i][1];
            while ( j < source.length ) {
               console( "Processing, precision" + " geo/name= " + geo_precision + "/" + nam_precision );
               input = new File( source[j] );
               console( "Reading file " + input.toString() );
               console( "Writing file " + output.toString() );
               FileInputStream fstream = new FileInputStream( input );
               DataInputStream in = new DataInputStream( fstream );
               BufferedReader br = new BufferedReader( new InputStreamReader( in ) );

               while ( ( inline = br.readLine() ) != null ) {
                  if ( inline.contains( "latitude" ) || ( inline.contains( "0,0" ) ) ) {
                     inline = br.readLine();
                  }
                  list = ( ArrayList<CSVRecord> ) CSVParser.parse( inline, CSVFormat.EXCEL ).getRecords();
                  record = list.get( 0 );
                  int k = 0;
                  while( k < record.size() ) {
                     codat[k] = record.get( k );
                     k++;
                  }
                  codat[6] = codat[6].replaceAll( " ", "." );
                  codat[7] = codat[7].replaceAll( " ", "." );                 
                  NameWords words = new NameWords( cleaner.Clean( codat[1] ), nam_precision );
                  hash = words.nameHash();
                  
                  // add leastscore check;
                  if ( words.getLeastscore() > 10000 ) {
                     geodna = GeoDNA.encode( Double.parseDouble( codat[6] ), Double.parseDouble( codat[7] ), 22 );
                  } else {
                     geodna = GeoDNA.encode( Double.parseDouble( codat[6] ), Double.parseDouble( codat[7] ),
                           geo_precision );
                  }
                  hash = ( geodna + "oooooooooooooooooooooo" ).substring( 0, 22 ) + hash;


                  if ( j == 1 ) {
                     if ( names.containsKey( hash ) ) {
                        // check the source2 duplicates
                        dupecount++;
                        bow.write( counter + "," + hash + "," + inline + "," + names.get( hash ) );
                        bow.newLine();
                     }
                  } else {
                     // load the source1 target data
                     if ( !names.containsKey( hash ) ) {
                        names.put( hash, inline );
                     } else {

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
               counter = 1000;
            }
            j = 0;
            i++;
         }
         bow.flush();
         bow.close();

      } catch ( Exception e ) {
         System.out.println( inline );
         e.printStackTrace();
         // TODO Auto-generated catch block
      }
      console( "Done" );
   }
   
   private void console( String s ) {
      System.out.println( s );
   }

} // end
