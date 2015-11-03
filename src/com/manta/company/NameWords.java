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
   //private String delimiter = "";
   private int leastscore = 0;
   private int maxscore = 1000000;
   private String pad = "quicker brownish foxes leaping over lazyish doggy each day";
   private DoubleMetaphone soundslike = new DoubleMetaphone();
   private String[] set;
   private String[] raw;

   public NameWords( String name, int p ) {
      precision = p;
      raw = name.split( " " );
      set = pad.split( " " );
      soundslike.setMaxCodeLen( 8 );
      
      HashMap<Integer, String> words = new HashMap<Integer, String>();
      int i = 0;
      int j = 0;
      pad = "";
      
      while ( j < set.length ) {
         pad = pad + soundslike.encode( set[j] );
         pad = pad.toLowerCase();
         j++;
      }

      while ( i < raw.length ) {
         if ( WordsHash.map.containsKey( raw[i] ) ) {
            if( ( int ) WordsHash.map.get( raw[i] ) < maxscore ) {
               words.put( ( int ) WordsHash.map.get( raw[i] ), raw[i] );
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
      // RefinedSoundex soundslike = new RefinedSoundex();
      // RawName soundslike = new RawName();
      // Soundex soundslike = new Soundex();
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
      hash = hash + pad;
      //if ( temp.contains( "starbucks" ) ) {
         // System.out.println( leastscore + temp );
      //}

      return hash.substring( 0, precision );
   }

   public int getMaxScore( ) {
      return maxscore;
   }

   public void setMaxScore( int max ) {
      this.maxscore = max;
   }

   public int getLeastscore( ) {
      return leastscore;
   }

   public void setLeastscore( int leastscore ) {
      this.leastscore = leastscore;
   }

}
