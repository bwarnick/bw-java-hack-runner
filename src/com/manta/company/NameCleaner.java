package com.manta.company;

public class NameCleaner {

   private static String[][] reps = { { "http://", "http://", "" }, { "www.", "www.", "" }, { "|", "|", "|" },
         { "!", "!", "!" }, { "\"", "\"", "" }, { "#", "#", "" }, { "$", "$", "" }, { "%", "%", "" },
         { "&", "&", " & " }, { "'", "'", "" }, { "(", "\\(", " " }, { ")", "\\)", " " }, { "*", "\\*", " " },
         { "+", "\\+", " " }, { ",", ",", " " }, { "-", "\\-", " " }, { ".", "\\.", "" }, { "/", "/", " " },
         { ":", ":", " " }, { ";", ";", " " }, { "<", "<", "" }, { "=", "=", "" }, { ">", ">", "" },
         { "?", "\\?", "" }, { "@", "@", "" }, { "[", "\\[", "" }, { "\\", "\\\\", "" }, { "]", "\\]", "" },
         { "^", "^", "" }, { "_", "_", " " }, { "`", "`", "" }, { "{", "\\{", "" }, { "|", "|", "" },
         { "}", "\\}", "" }, { "~", "~", "" } };

   public NameCleaner( ) {
   }

   public String Clean( String name ) {

      name = name.toLowerCase();

      int j = 0;
      for ( j = 0; j < reps.length; j++ ) {
         if ( name.contains( reps[j][0] ) ) {
            name = name.replaceAll( reps[j][1], reps[j][2] );
         }
      }
      while ( name.contains( "  " ) ) {
         name = name.replaceAll( "  ", " " );
      }
      return name.trim();
   }

   private void console( String s ) {
      System.out.println( s );
   }

} // end
