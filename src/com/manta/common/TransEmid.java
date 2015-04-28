package com.manta.common;

import java.io.File;

public class TransEmid {

  // private String pp = "|";
  // private String cc = ",";
  // private String ur = "http://www.manta.com/c/";
  private String[] man = { "0137134735" };

  // private String[] man = {"mr0jhtr"};
  // private Integer proc = 3;

  public void run( int p ) {
    File fin = new File( "c:/io/mids-emids7.txt" );
    File fout = new File( "c:/io/mids-emids7-out.txt" );
    // mid file format - mid_14,mid_13
    switch ( p ) {
    case 1:
      decrypt();
      break;
    case 2:
      decrypt( fin, fout );
      break;
    case 3:
      encrypt();
      break;
    case 4:
      encrypt( fin, fout );
      break;
    }
    System.out.println( "Done" );
  }

  private void encrypt( File fin, File fout ) {
    int i;
    String[] mids = FileIO.FileToString( fin );
    for ( i = 0; i < mids.length; i++ ) {
      // Create rec_stat_0194 record for inserting 302 re-directs
      // mids[i] =mids[i].substring( 11 ) + cc + "r" + pp + "302" + pp + Encryption.encryptMID( mids[i].substring( 0, 10
      // ));
      // Export test urls - mid_13 & mid_14
      mids[i] = mids[i] + ",r|301|" + Encryption.encryptMID( mids[i].substring( 0, 10 ) );
      // Export a list of mid_13 values only - for Johnson's tests
      // mids[i] =mids[i].substring( 11 );
    }
    FileIO.ArrayToFile( mids, fout );
  }

  private void encrypt( ) {
    int i;
    for ( i = 0; i < man.length; i++ ) {
      // System.out.println( man[i].substring( 11 ) + cc + "r" + pp + "302" + pp + Encryption.encryptMID(
      // man[i].substring( 0, 10 )));
      // System.out.println( Encryption.encryptMID( man[i].substring( 0, 10 )) + " " + man[i].substring( 0, 10 ));
      System.out.println( Encryption.encryptMID( man[i].substring( 0, 10 ) ) );
    }
  }

  private String decrypt( String s ) {
    return Encryption.decryptMID( s );

  }

  private void decrypt( File fin, File fout ) {
    int i;
    String[] mids = FileIO.FileToString( fin );
    for ( i = 0; i < mids.length; i++ ) {
      mids[i] = Encryption.decryptMID( mids[i].substring( 0, 7 ) );
    }
    FileIO.ArrayToFile( mids, fout );
  }

  private void decrypt( ) {
    int i;
    for ( i = 0; i < man.length; i++ ) {
      // System.out.println( man[i].substring( 11 ) + cc + "r" + pp + "302" + pp + Encryption.encryptMID(
      // man[i].substring( 0, 10 )));
      System.out.println( Encryption.decryptMID( man[i].substring( 0, 7 ) ) );
    }
  }

}
