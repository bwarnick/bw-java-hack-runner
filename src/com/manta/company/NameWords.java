package com.manta.company;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class NameWords {

  private Map<Integer, String> wordsort;
  // deprecate - private int usecap = 10000000;
  // deprecate - private int wordcap = 2;
  private int precision = 22;
  private String delimiter = "";


  public NameWords( String w, int p ) {
    precision = p;
    // bigquery load 1 - cap=1000000, wordcap = 5, precision null and delimiter = "-"
    String[] raw = w.split( " " );
    HashMap<Integer, String> words = new HashMap<Integer, String>();
    int i = 0;
    int q = 0;
    while ( i < raw.length ) {
      if ( WordsHash.map.containsKey( raw[i] ) ) {
        q = ( int ) WordsHash.map.get( raw[i] );
        // if ( q > 1000000 )
        // System.out.println( 1000000 + " - " + raw[i] );
        // if ( q == 1 )
        // System.out.println( 1 + " - " + raw[i] );
        words.put( q, raw[i] );
        // }
      }
      i++;
    }
    wordsort = new TreeMap<Integer, String>( words );
  }


  public String toString( ) {
    return wordsort.toString();
  }


  public String nameHash( ) {
    Set<Entry<Integer, String>> entrySet = wordsort.entrySet();
    Iterator<Entry<Integer, String>> it = entrySet.iterator();
    String hash = "";
    int i = 0;
    while ( it.hasNext() ) {
      // if ( i < wordcap ) {
      hash = hash + delimiter + it.next().toString().replaceAll( ".*=", "" );
      // i++;
      // } else {
      // it.next();
      // }
    }
    // System.out.println( hash );
    hash = hash + "abcdefghijklmnopqrstuvwxyz";
    return hash.substring( 0, precision );
  }
}
