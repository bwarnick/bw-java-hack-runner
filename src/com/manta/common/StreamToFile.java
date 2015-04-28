package com.manta.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public final class StreamToFile {

  static BufferedWriter fos = null;

  public static void newWriter( File f ) {
    try {
      fos = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( f ), "UTF8" ) );
    } catch ( Exception e ) {
      System.out.println( e );
    }
  }

  public static void lineWriter( String s ) {
    try {
      fos.write( s );
      fos.newLine();
    } catch ( Exception e ) {
      System.out.println( e );
    }
  }

  public static void closeWriter( ) {
    try {
      fos.close();
    } catch ( Exception e ) {
      System.out.println( e );
    }
  }

} // end