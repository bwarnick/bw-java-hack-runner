package com.manta.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonParseException;

public class NameWordBuilder {

  private File input = new File( "c:/io/mc2_name1_deduped.csv" );
  private File output = new File( "c:/io/mc2_name_words_test.csv" );
  private File name_words = new File( "conf/resources/data/name_words.ser" );
  private int counter = 0;
  private int segment = 1000000;


  public NameWordBuilder( ) {
  }


  public void nameFileParser( ) throws JsonParseException, IOException {
    console( "Start" );
    NameCleaner cleaner = new NameCleaner();
    output.createNewFile();
    HashMap<String, Integer> results = new HashMap<String, Integer>();
    // HashMap<Integer, Integer> counts = new HashMap<Integer, Integer>();
    results.put( "manta", 1 );
    FileOutputStream fos = new FileOutputStream( output );
    BufferedWriter bow = new BufferedWriter( new OutputStreamWriter( fos ) );
    FileInputStream fstream = new FileInputStream( input );
    DataInputStream in = new DataInputStream( fstream );
    BufferedReader br = new BufferedReader( new InputStreamReader( in ) );
    String[] words;
    String bline = "";

    try {
      while ( ( bline = br.readLine() ) != null ) {
        // while ( counter < 1000 ) {
        // bline = br.readLine();
        counter++;
        bline = cleaner.Clean( bline );
        // console( bline );
        words = bline.split( " " );

        if ( words.length < 1 ) {
          words[0] = bline;
        }

        int i = 0;
        while ( i < words.length ) {
          bline = words[i];
          if ( results.containsKey( bline ) ) {
            results.put( bline, results.get( bline ) + 1 );
          } else {
            results.put( bline, 1 );
          }
          i++;
          // console( bline );
        }

        if ( counter % segment == 0 ) {
          console( String.valueOf( counter / segment ) );
        }
      }
      serializeHashMap( results, name_words );
      console( "serialized results" );

      // This builds a test map of counts of name_word counts
      /*
       * Iterator<Entry<String, Integer>> it = results.entrySet().iterator(); while ( it.hasNext() ) { Map.Entry<?, ?>
       * pair = it.next(); bow.write( pair.getKey() + "|" + pair.getValue() ); bow.newLine(); if ( counts.containsKey(
       * pair.getValue() ) ) { counts.put( ( Integer ) pair.getValue(), counts.get( pair.getValue() ) + 1 ); } else {
       * counts.put( ( Integer ) pair.getValue(), 1 ); } } // console( counts.toString() );
       * 
       * // Outputs counts to a file Iterator<Entry<Integer, Integer>> ct = counts.entrySet().iterator(); BufferedWriter
       * out = fileWriter( new File( "c:/io/mc2_name_words_counts.csv" ) ); while ( ct.hasNext() ) { Map.Entry<?, ?>
       * nvpr = ct.next(); out.write( nvpr.getKey() + "|" + nvpr.getValue() ); out.newLine(); }
       */
      // end
    } catch ( Exception e ) {
      e.printStackTrace();
      // TODO Auto-generated catch block
    }
    console( "Done" );
    br.close();
    bow.close();
  }


  private BufferedWriter fileWriter( File f ) throws IOException {
    f.createNewFile();
    FileOutputStream fos = new FileOutputStream( f );
    return new BufferedWriter( new OutputStreamWriter( fos ) );
  }


  private void serializeHashMap( HashMap<String, Integer> hmap, File file ) throws IOException {
    FileOutputStream fos = new FileOutputStream( file );
    ObjectOutputStream oos = new ObjectOutputStream( fos );
    oos.writeObject( hmap );
    oos.close();
    fos.close();
  }


  @SuppressWarnings( "unchecked" )
  private HashMap<String, Integer> deserializeHashMap( File file ) throws IOException, ClassNotFoundException {
    HashMap<String, Integer> results = null;
    FileInputStream fis = new FileInputStream( file );
    ObjectInputStream ois = new ObjectInputStream( fis );
    results = ( HashMap<String, Integer> ) ois.readObject();
    ois.close();
    fis.close();
    return results;
  }


  private void console( String s ) {
    System.out.println( s );
  }

} // end
