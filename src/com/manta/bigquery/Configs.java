package com.manta.bigquery;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.services.bigquery.Bigquery;
import com.google.api.services.bigquery.Bigquery.Tables;
import com.google.api.services.bigquery.model.Table;
import com.google.api.services.bigquery.model.TableReference;
import com.google.api.services.bigquery.model.TableSchema;

public class Configs {

  protected Bigquery bigquery;
  protected Table table;
  private TableSchema schema;
  private File jsonschema = new File( "conf/resources/schema/kibana_bq_schema.json" );
  protected String projectId = "united-course-544";
  protected String datasetId = "data_update_tests";
  protected String tableId = "xlogger_upload_040615";
  protected String mdatasetId = "";
  protected String mtableId = "";
  protected Boolean isBigquery = false;
  protected int rownum = 500;

  public Configs( ) {
  }

  public Configs( BigqueryConnection c ) throws IOException, GeneralSecurityException {
    setBigquery( c );
    if ( mtableId == "" ) {
      setSchema();
    } else
      setSchemaFromTable( mdatasetId, mtableId );
    // setTable();
    // System.out.println( table.getSchema().toString() );
  }

  public void setBigquery( BigqueryConnection c ) throws IOException, GeneralSecurityException {
    this.bigquery = c.getBigquery();
    isBigquery = true;
  }

  public void setTable( ) throws JsonProcessingException, IOException, GeneralSecurityException {
    this.table = new Table();
    this.table.setSchema( schema );
    TableReference tableRef = new TableReference();
    tableRef.setDatasetId( datasetId );
    tableRef.setProjectId( projectId );
    tableRef.setTableId( tableId );
    this.table.setTableReference( tableRef );
  }

  public void setSchemaFromTable( String d, String t ) throws GeneralSecurityException, IOException {
    Tables.Get request = bigquery.tables().get( projectId, d, t );
    Table table = request.execute();
    schema = table.getSchema();
  }

  public TableSchema getSchema( ) {
    return schema;
  }

  public void setSchema( TableSchema schema ) {
    this.schema = schema;
  }

  public void setSchema( ) throws JsonParseException, IOException {
    // BigquerySchema.testParse( jsonschema );
    this.schema = BigquerySchema.getTableSchema( jsonschema );
  }

  public File getJsonschema( ) {
    return jsonschema;
  }

  public void setJsonschema( File jsonschema ) {
    this.jsonschema = jsonschema;
  }

  public void setProjectId( String projectId ) {
    this.projectId = projectId;
  }

  public void setDatasetId( String datasetId ) {
    this.datasetId = datasetId;
  }

  public void setTableId( String tableId ) {
    this.tableId = tableId;
  }

  public void setRownum( int rownum ) {
    this.rownum = rownum;
  }

}
