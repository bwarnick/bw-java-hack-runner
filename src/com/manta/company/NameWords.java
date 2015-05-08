package com.manta.company;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class NameWords {

  private Map<Integer, String> wordsort;
  private int threshold = 1000000;


  public NameWords( String w ) {

    String[] raw = w.split( " " );
    HashMap<Integer, String> words = new HashMap<Integer, String>();
    int i = 0;
    int q = 0;
    while ( i < raw.length ) {
      if ( WordsHash.map.containsKey( raw[i] ) ) {
        q = ( int ) WordsHash.map.get( raw[i] );
        if ( q < threshold ) {
          words.put( q, raw[i] );
        }
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
    while ( it.hasNext() ) {
      hash = hash + "|" + it.next().toString().replaceAll( ".*=", "" );
    }
    // System.out.println( hash );
    return hash;
  }

}
