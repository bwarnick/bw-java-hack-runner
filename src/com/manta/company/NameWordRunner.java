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

public class NameWordRunner {

   // Run Company Name, Lat, Lon from a csv file, generate a geodna key and a name_words key and output to a file

   private File input = new File( "/Users/bradwarnick/io/xLML_mc2_targets18.csv" );
   private File output = new File( "/Users/bradwarnick/io/xLML_mc2_targets18_results.csv" );
   private File name_words = new File( "conf/resources/data/name_words.ser" );
   private HashMap<String, Integer> geonam = new HashMap<String, Integer>();
   ArrayList<CSVRecord> list = null;
   CSVRecord record = null;
   private int counter = 0;
   private int dupes = 0;
   private int geo_precision = 22;
   private int nam_precision = 12;
   private String dl = "|";
   private boolean ishead = true;
   private int columns = 10;


   public NameWordRunner( ) {
   }

   public void nameFileParser( ) throws JsonParseException, IOException, ClassNotFoundException {
      console( "Start" );
      NameCleaner cleaner = new NameCleaner();
      @SuppressWarnings( "unused" )
      WordsHash hashmap = new WordsHash( name_words );
      String namdna = null;
      String[] codat;
      String geodna;
      String bline = "";
      // String subline = "";
      // String hash = "";
      // int j = 1;

      try {
         console( "Processing file " + input.toString() );
         FileInputStream fstream = new FileInputStream( input );
         DataInputStream in = new DataInputStream( fstream );
         BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
         FileOutputStream fos = new FileOutputStream( output );
         BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );

         if ( ishead ) {
            bow.write( "geodna" + dl + "namdna" + br.readLine().replace( ",", dl ) );
            bow.newLine();
         }
         while ( ( bline = br.readLine() ) != null ) {
            codat = parseCSVline( bline, columns );
            if ( !codat[1].equals( "" ) ) {
               geodna = GeoDNA.encode( Double.parseDouble( codat[1] ), Double.parseDouble( codat[2] ), geo_precision );
               namdna = ( new NameWords( cleaner.Clean( codat[0] ), nam_precision ) ).nameHash();
            } else {
               geodna = "";
               namdna = "";
            }
            if ( !deDupe( geodna + namdna ) ) {
               bow.write( geodna + dl + namdna + genColumns( codat ) );
               bow.newLine();
               counter++;
            } else {
               codat[columns - 1] = "D";
               bow.write( geodna + dl + namdna + genColumns( codat ) );
               bow.newLine();
               dupes++;
            }
            if ( counter % 100000 == 0 ) {
               console( "Processed " + counter + " lines" );
            }
         }
         console( "Processed " + counter + " unique records" );
         console( "Skipped " + dupes + " duplicate records" );
         br.close();
         bow.close();

      } catch ( Exception e ) {
         System.out.println( bline );
         System.out.println( "" );
         e.printStackTrace();
         // TODO Auto-generated catch block
      }
      console( "Done" );
   }

   private boolean deDupe( String k ) {
      if ( geonam.containsKey( k ) ) {
         geonam.put( k, geonam.get( k ) + 1 );
         return true;
      } else {
         geonam.put( k, 1 );
         return false;
      }
   }
   
   private String genColumns( String[] s ) {
      int i = 0;
      String columns = "";
      while( i < s.length ){
         columns = columns + dl + s[i];
         i++;
      }
      return columns;
   }

   private String[] parseCSVline( String l, int i ) throws IOException {
      String[] iLine = new String[i];
      list = ( ArrayList<CSVRecord> ) CSVParser.parse( l, CSVFormat.EXCEL ).getRecords();
      record = list.get( 0 );
      int k = 0;
      while ( k < record.size() ) {
         iLine[k] = record.get( k );
         k++;
      }
      return iLine;
   }

   private void console( int i ) {
      System.out.println( i );
   }
   
   private void console( String s ) {
      System.out.println( s );
   }

} // end
