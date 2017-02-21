package com.manta.homeyou;

import org.jetbrains.annotations.Contract;

/**
 * Created by bradwarnick on 11/17/16.
 */
public class HomeyouLineItem {
    private String date = "";
    private String campaign_id = "";
    private String campaign_name = "";
    private String ivr_name = "";
    private String pool_root = "";
    private String phone_number = "";
    private int total_leads = 0;
    private float total_revenue = 0;
    private static String dt_label = "\"date\": ";
    private static String id_label = "\"campaign_id\": ";
    private static String cp_label = "\"campaign_name\": ";
    private static String iv_label = "\"ivr_name\": ";
    private static String pr_label = "\"pool_root\": ";
    private static String ph_label = "\"phone_number\": ";
    private static String ld_label = "\"total_leads\": ";
    private static String rv_label = "\"total_revenue\": ";
    private static char qt = '"';
    private static char cm = ',';


    public HomeyouLineItem( ){
    }

    public void setDate( String d ){
        this.date = d;
    }

    public void setCampaignId( String i ){
        this.campaign_id = i;
    }

    public void setCampaignName( String n ){
        this.campaign_name = n;
    }

    public void setIvrName( String v ){
        this.ivr_name = v;
    }

    public void setPoolRoot( String v ){
        this.pool_root = v;
    }

    public void setPhoneNumber( String v ){
        this.phone_number = v;
    }

    public void setLeads( int l ){
        this.total_leads = l;
    }

    public void setRevenue( float r ){
        this.total_revenue = r;
    }

    public String getDate() { return date; }

    public String getCampaignId() {
        return campaign_id;
    }

    public String getCampaignName() {
        return campaign_name;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public String getIvrName() {
        return ivr_name;
    }

    public String getPoolRoot() {
        return pool_root;
    }

    public int getTotal_leads() {
        return total_leads;
    }

    public float getTotalRevenue() {
        return total_revenue;
    }

    @Override
    public String toString() {
        StringBuilder line = new StringBuilder();
        line.append('{');
        line.append( dt_label ).append( qt + date + qt + cm );
        line.append( id_label ).append( qt + campaign_id + qt + cm );
        line.append( cp_label ).append( qt + campaign_name + qt + cm );
        line.append( iv_label ).append( qt + ivr_name + qt + cm );
        line.append( pr_label ).append( qt + pool_root + qt + cm );
        line.append( ph_label ).append( qt + phone_number + qt + cm );
        line.append( ld_label ).append(total_leads).append( cm );
        line.append( rv_label ).append(total_revenue);
        line.append( '}' );
        return line.toString();
    }

}
