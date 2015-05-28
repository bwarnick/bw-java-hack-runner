package com.manta.company;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.RefinedSoundex;
import org.apache.commons.codec.language.Soundex;

public class NameWords {

   private Map<Integer, String> wordsort;
   private int precision = 22;
   private String delimiter = "";
   private int leastscore = 0;

   public NameWords( String w, int p ) {
      precision = p;
      String[] raw = w.split( " " );
      HashMap<Integer, String> words = new HashMap<Integer, String>();
      int i = 0;
      int q = 0;
      while ( i < raw.length ) {
         if ( WordsHash.map.containsKey( raw[i] ) ) {
            q = ( int ) WordsHash.map.get( raw[i] );
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
      // RefinedSoundex soundslike = new RefinedSoundex();
      // RawName soundslike = new RawName();
      // Soundex soundslike = new Soundex();
      DoubleMetaphone soundslike = new DoubleMetaphone();
      // System.out.println( soundslike.getMaxCodeLen() );
      String hash = "";
      String result = "";
      String temp = "";

      int i = 0;
      while ( it.hasNext() ) {
         result = it.next().toString();
         temp = temp + "-" + result.replaceAll( ".*=", "" ).toLowerCase();
         if ( i == 0 ) {
            setLeastscore( Integer.parseInt( result.replaceAll( "=.*", "" ) ) );
         }
         hash = hash + soundslike.encode( result.replaceAll( ".*=", "" ) ).toLowerCase();
         i++;
      }
      hash = hash + "jpslsafrfkskktkprnnyzkzytqwtfdsxcvmjplmy";
      if ( temp.contains( "starbucks" ) ) {
         // System.out.println( leastscore + temp );
      }
      return hash.substring( 0, precision );
   }

   public int getLeastscore( ) {
      return leastscore;
   }

   public void setLeastscore( int leastscore ) {
      this.leastscore = leastscore;
   }
   
   
}
