package com.manta.mongo;

import java.io.IOException;
import java.util.regex.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.manta.common.jsonTree;


public class MongoToBQcleaner {

	private static CharSequence target[][] = {
		{ " { \"$oid\" : ", "}" },
		{ " { \"$date\" : ", "}" }};
	private static Pattern sprice = Pattern.compile( "(\"price\" :)([^{]*?})" );
	private static CharSequence rprice = "\"price\" : { \"value\" : ";
	private static Pattern ssubtype = Pattern.compile( "(\"subType\" : \"\")" );
	private static CharSequence rsubtype = "\"subType\" : null";
	private static Pattern screatedby1 = Pattern.compile( "(\"createdBy\" : false)" );
	private static CharSequence rcreatedby1 = "\"createdBy\" : { \"id\" : \"\" }";
	private static Pattern screatedby2 = Pattern.compile( "(\"createdBy\" : \")(.*?\")" );
	private static CharSequence rcreatedby2 = "\"createdBy\" : { \"id\" : \"";


	// flatten mongo _id and $date fields, change object types to strings for BQ	
	public static String mongoToBQ( String s, String t ) {
		String snip = "";
		String repl = "";
		int i = 0;

		while( i < target.length ) {
			while( s.indexOf( target[i][0].toString() ) > 0)  {
				snip = snipText( s, target[i][0].toString(), target[i][1].toString() );
				repl = snip.replace( target[i][0], "" );
				repl = repl.replace( target[i][1], "" );
				s = s.replace( snip, repl );
			}
			i++;	
		}
		if( t == "products") s = fixProducts( s );
		if( t == "connections" ) s = fixRecommendations( s );
		return s ;
	}
	
	// Use Jackson api to flatten mongo _id and $date fields, change object types to strings for BQ	
	public static String transformMongoToBQ( String s, String t ) {
		String snip = "";
		String repl = "";
		int i = 0;
		
		//future handle json fixes and formatting required for BQ using Jackson API instead of String parsing
		System.out.println( s );
		try {

			jsonTree.readJsonTree( s );
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while( i < target.length ) {
			while( s.indexOf( target[i][0].toString() ) > 0)  {
				snip = snipText( s, target[i][0].toString(), target[i][1].toString() );
				repl = snip.replace( target[i][0], "" );
				repl = repl.replace( target[i][1], "" );
				s = s.replace( snip, repl );
			}
			i++;	
		}
		if( t == "products") s = fixProducts( s );
		if( t == "connections" ) s = fixRecommendations( s );
		return s ;
	}

	private static String snipText( String z, String tb, String te ){
		int beg = z.indexOf( tb );
		int end = z.indexOf( te, beg ) + te.length();
		return z.substring( beg, end );
	}
	
	// Problem: inconsistent schema for product price field, sometimes treated as nested record, sometimes a flat field.
	// Fix: convert to always being a nested record, add nested field "value"
	// - now [ { "type" : "fixed" , "unit" : "each" , "price" : null }]
	// - fix [ { "type" : "fixed" , "unit" : "each" , "price" : { "value" : null}}]
	// - now [ { "type" : "fixed" , "unit" : "each" , "price" : 19.99}]
	// - fix [ { "type" : "fixed" , "unit" : "each" , "price" : { "value" : 19.99}}]
	// - not [ { "type" : "range" , "unit" : "each" , "price" : { "high" : 2100 , "low" : 549.5}}]
	// - not [ { "type" : "range" , "unit" : "each" , "price" : { "low" : 60 , "high" : 1000}}]
	private static String fixProducts( String s ) {
		Matcher match = sprice.matcher( s );
		if( match.find() ) {
			s = s.replace( match.group(1), rprice ).toString();
			s = s.replace( match.group(2), match.group(2) + '}').toString();
		}
		return s;
	}
	
	// - now "subType" : ""
	// - tbd "subType" : false
	public static String fixRecommendations( String s ) {
		Matcher match = ssubtype.matcher( s );
		if( match.find() ) {
			s = s.replace( match.group(1), rsubtype ).toString();
		}
		match = screatedby1.matcher( s );
		while( match.find() ){
			s = s.replace( match.group(1), rcreatedby1 ).toString();
		}
		match = screatedby2.matcher( s );
		while( match.find() ){
			s = s.replace( match.group(1), rcreatedby2 ).toString();
			s = s.replace( match.group(2), match.group(2) + "}" ).toString();
		}
		return s;
	}
}
	
