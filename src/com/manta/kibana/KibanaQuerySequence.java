package com.manta.kibana;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.manta.common.FileIO;

public class KibanaQuerySequence {

  private String querytxt = "";
  private Date startime;
  private Date endtime;
  private int count;
  private int current;
  private long inc;
  private long from;
  private long to;
  private SimpleDateFormat format = new SimpleDateFormat( "MM-dd-yy hh:mm:ss" );


  public KibanaQuerySequence( File query, String s, String e, int c ) throws ParseException {
    querytxt = queryBase( query );
    startime = format.parse( s );
    // System.out.println( startime.getTime() );
    endtime = format.parse( e );
    // System.out.println( endtime.getTime() );
    count = c;
    from = startime.getTime();
    to = endtime.getTime();
    inc = ( to - from ) / count;
    // System.out.println( inc );
    current = 0;
  }


  public String getNextQuery( ) {
    if ( hasNext() ) {
      to = from + inc - 1;
      querytxt = querytxt.replaceFirst( "\"from\":[0-9]{13}", "\"from\":" + String.valueOf( from ) );
      querytxt = querytxt.replaceFirst( "\"to\":[0-9]{13}", "\"to\":" + String.valueOf( to ) );
      // System.out.println( current + ": " + from + " - " + to );
      from = to + 1;
      current++;
    }
    return querytxt;
  }


  public boolean hasNext( ) {
    if ( current < count )
      return true;
    else
      return false;
  }


  private String queryBase( File q ) {
    return FileIO.FileToStr( q ).replaceAll( "\n", "" ).replaceAll( " ", "" );
  }

}
