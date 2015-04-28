package com.manta.bigquery;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author Stephen Smith - from data-source-service, Bigquery test branch
 *
 */
public class Generator {

	public Generator() {
    }

    static private String getBqType(String type) {
        String bqType = "UNKNOWN";
        switch (type) {
            case "object":
                bqType = "RECORD";
                break;

            case "string":
                bqType = "STRING";
                break;

            case "boolean":
                bqType = "BOOLEAN";
                break;

            case "int":
                bqType = "INTEGER";
                break;
        }

        return bqType;
    }

    public TableSchema fromModelMapper(JsonNode node) throws IOException {
        ArrayList<TableFieldSchema> fields = getSchemaFields( "", node);
        TableSchema schema = new TableSchema();
        schema.setFields(fields.get(0).getFields());
        return schema;
    }

    private ArrayList<TableFieldSchema> getSchemaFields(String name, JsonNode node) throws IOException {
        String type = node.get("type").textValue();
        ArrayList<TableFieldSchema> schemaFields = new ArrayList<>();

        switch (type) {
            case "object":
                TableFieldSchema recordFields = new TableFieldSchema();
                recordFields.setName(name);
                recordFields.setType("RECORD");
                //compiler error from cloned class (Stephens)
                recordFields.setFields(new ArrayList<TableFieldSchema>());
                schemaFields.add(recordFields);

                JsonNode properties = node.get("properties");
                Iterator<Map.Entry<String, JsonNode>> nodes = properties.fields();
                while (nodes.hasNext()) {
                    Map.Entry<String, JsonNode> entry = nodes.next();
                    String fieldName = entry.getKey();
                    JsonNode property = entry.getValue();
                    ArrayList<TableFieldSchema> children = getSchemaFields(fieldName, property);
                    recordFields.getFields().addAll(children);
                }

                break;

            case "array":
                JsonNode itemDescription = node.get("items");
                ArrayList<TableFieldSchema> listFields = getSchemaFields(name, itemDescription);
                listFields.get(0).setMode("REPEATED");
                schemaFields.addAll(listFields);
                if (node.has("count")) {
                    if (node.get("count").asBoolean()) {
                        TableFieldSchema countRecord = new TableFieldSchema();
                        countRecord.setName("count");
                        countRecord.setType("INTEGER");
                        schemaFields.add(countRecord);
                    }
                }
                break;

            default:
                String bqType = getBqType(type);
                TableFieldSchema simpleField = new TableFieldSchema();
                simpleField.setName(name);
                simpleField.setType(bqType);
                schemaFields.add(simpleField);
                break;
        }
        return schemaFields;
    }
}