package com.manta.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;

import java.net.UnknownHostException;


public class MongoDatabase {
	
	// Host
	private static Boolean ismongoclient = false;
	private static MongoClient mongoclient;
	// Host defaults for now
	private static String host = "mongo-e.mongodb.main.production.aws.ecnext.net";
	private static int port = 27017;
	
	// Project
	private static DB mydatabase;
	private static DBCollection mycollection;
	private static BasicDBObject query;
	public static DBCursor cursor;
	//Project defaults for now
	private static String database = "mstest";
	private static String collection = "connections";
	private static int toskip = 0;
	private static int tolimit = 0;

	
	
	public static void setMongoClient() throws UnknownHostException {
		if( !ismongoclient ) setMongoClient();
		setDatabase( database, collection );
		setQuery( "type" , "recommendation" );
		cursor = mycollection.find( query ).skip( toskip ).limit( tolimit );
	}


	public static BasicDBObject getQuery() {
		return query;
	}


	public static void setQuery( BasicDBObject query ) {
		MongoDatabase.query = query;
	}

	
	public static void setQuery( String field, String value ) {
		query = new BasicDBObject( field, value );
	}

	
	public static void setHost() throws UnknownHostException {
		setHost( host, port );
	}

	
	public static void setHost( String host, int port ) throws UnknownHostException {
		mongoclient = new MongoClient( host, port );
		mongoclient.setReadPreference( ReadPreference.secondaryPreferred() );
	}


	public static void setDatabase( String db, String col ) {
		mydatabase = mongoclient.getDB( db );
		mycollection = mydatabase.getCollection( col );
	}
	
}
