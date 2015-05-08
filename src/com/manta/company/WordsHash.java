package com.manta.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

public class WordsHash {

  public static HashMap<String, Integer> map = null;
  private File name_words = new File( "conf/resources/data/name_words.ser" );


  public WordsHash( File file ) throws ClassNotFoundException, IOException {

    if ( map == null ) {
      console( "Loading HashMap" );
      map = deserializeHashMap( file );
      console( "HashMap Loaded" );
    }

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

}
