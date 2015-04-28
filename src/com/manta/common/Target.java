package com.manta.common;

import com.fasterxml.jackson.core.JsonToken;

public class Target {

  protected JsonToken start;
  protected String name;
  protected int count;
  protected String action;

  public Target( String t, String n, int i, String a ) {
    setJsonToken( t );
    setName( n );
    setCount( i );
    setActions( a );
  }

  private void setJsonToken( String t ) {
    start = JsonToken.valueOf( t );
  }

  private void setName( String n ) {
    name = n;
  }

  private void setCount( int i ) {
    count = i;
  }

  private void setActions( String a ) {
    action = a;
  }

}
