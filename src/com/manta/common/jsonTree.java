package com.manta.common;

import java.io.IOException;
import java.util.Iterator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
 
public class jsonTree {
    static String test = "photos";
	
	public static JsonNode readJsonTree( String s ) throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree( s );
        //System.out.println(node.getNodeType());
        //System.out.println(node.isContainerNode());
        Iterator<String> fieldNames = node.fieldNames();
        Iterator<JsonNode> fieldValues = node.elements();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            String fieldValue = fieldValues.next().toString();
            System.out.println( fieldName + " : " + fieldValue );
        }
        JsonNode attrsdata = node.get( test );
        System.out.println(attrsdata.getNodeType());
        Iterator<String> attrsFields = attrsdata.fieldNames();
        while (attrsFields.hasNext()) {
            String field = attrsFields.next();
            String value = attrsdata.findValue( field ).toString();
            System.out.println( test + "." + field + " : " + value );
        }
        return node;
    }
	
}