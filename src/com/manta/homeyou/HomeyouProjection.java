package com.manta.homeyou;

/**
 * Created by bradwarnick on 11/17/16.
 * NOT USED
 */
public class HomeyouProjection {
    private String date = "";
    private String[] fields;
    private static char qt = '"';
    private static char cm = ',';


    public HomeyouProjection( ){
    }

    @Override
    public String toString() {
        StringBuilder line = new StringBuilder();
        line.append('{');
        line.append( '}' );
        return line.toString();
    }

}
