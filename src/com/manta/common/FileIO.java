package com.manta.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

public final class FileIO {

	public final static String crlf = new String( new byte[] { '\r', '\n' } ) ;
	public final static String cr = new String( new byte[] { '\r' } ) ;
	public final static String lf = new String( new byte[] { '\n' } ) ;

	private FileIO( ) {
	}
     
	// Used to write a serializeable object to a file.
	public static void ObjectToFile( Object o, File f ) {
		try  {
			Object object = o ;
			File file  = f ;
			FileOutputStream fos = new FileOutputStream( file ) ;
			ObjectOutputStream oos = new ObjectOutputStream( fos ) ;
			oos.writeObject( object ) ;
			fos.close( ) ;
		}
		catch( Exception e ) {
			//Trace.out( 1, e ) ;
		}
	}
	 
    // Used to read a serializeable object from a file.
    public static Object FileToObject( File f ) {
        Object object ;
        try  {
            File file  = f ;
            FileInputStream fis = new FileInputStream( file ) ;
            ObjectInputStream ois = new ObjectInputStream( fis ) ;
            object = ois.readObject( ) ;
            fis.close( ) ;
            return object ;
        }
        catch( Exception e ) {
            //Trace.out( 1, e ) ;
            return null ;
        }
    }
    
	// Used to write a String to a file.
	public static void StringToFile( String s, File f ) {
		String str = s ;
		File fout  = f ;
		try  {
			FileOutputStream fos = new FileOutputStream( fout ) ;
			BufferedWriter bow = new BufferedWriter(new OutputStreamWriter( fos ));
			
			bow.write( str ) ;
			bow.newLine();
			bow.close();
			//System.out.println( str ) ;
		}
		catch( Exception e ) {
        	System.out.println(e);
			//Trace.out( 1, e ) ;
		}
	}
	
	// Used to write a String to a file.
	public static void ArrayToFile( String[] s, File f ) {
		String str[] = s ;
		File fout  = f ;
		try  {
			FileOutputStream fos = new FileOutputStream( fout ) ;
			BufferedWriter bow = new BufferedWriter(new OutputStreamWriter( fos ));

			for ( int i = 0; i < str.length; i++ ) {
				bow.write( str[i] ) ;
				bow.newLine();
				//System.out.println( str[i] ) ;
			}
			bow.close();
		}
		catch( Exception e ) {
        	System.out.println(e);
			//Trace.out( 1, e ) ;
		}
	}
	
	// Used for reading a text file of n crlf defined lines into String[n].
	public static String[] FileToString( File f ) {
		String strLine = "" ;
		Vector<String> vector = new Vector<String>() ; 
	    try{
	        // Open the file that is the first 
	        // command line parameter
	        FileInputStream fstream = new FileInputStream( f );
	        // Get the object of DataInputStream
	        DataInputStream in = new DataInputStream( fstream );
	        BufferedReader br = new BufferedReader( new InputStreamReader(in));
	        //Read File Line By Line
	        int i = 0 ;
	        while ((strLine = br.readLine()) != null )   {
	        	vector.add( i++, strLine);
	        	//System.out.println(i + " " + strLine);
	        }
	        //Close the input stream
	        in.close();
	        return vector.toArray(new String[vector.size()]);
	      } catch (Exception e){//Catch exception if any
	          System.err.println("Error: " + e.getMessage());
	          return null;
	      }
	}
	
	// Used for reading a text file of n crlf defined lines into String.
	public static String FileToStr( File f ) {
		File file  = f ;
		String str = "" ;
		int len ;
		if( !file.exists( ) ) return null ;
		try  {
			FileInputStream fis = new FileInputStream( file ) ;
			BufferedInputStream bis = new BufferedInputStream( fis ) ;
			byte[] buf = new byte[2048] ;
			while( ( len = bis.read( buf ) ) != -1 ) {
				str = str + new String( buf, 0, len ) ;
			}
			bis.close( ) ;
			return str ;
		}
		catch( Exception e ) {
			//Trace.out( 1, e ) ;
			return null ;
		}
	}

	/*
	public static String dirFileStr( File f ) {
		File[] file = dirFileList( f ) ;
		String filestr = "" ;
		int i ;
		if( file == null ) return null ;
		for( i = 0; i < file.length; i ++ ) filestr = filestr + file[i].getName() + crlf ;
		return filestr ;
	}
	
	public static File[] dirFileList( File f ) {
		File file = f ;
		GenfileFilter filter = new GenfileFilter( ) ;
		if( file.exists( ) ) return file.listFiles( filter ) ;
		else return null ;
	}
	
	public static boolean deleteFile( File f ) {
		File file = f ;
		return file.delete( ) ;
	}
	*/
		
} //end

