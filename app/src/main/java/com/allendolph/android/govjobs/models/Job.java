package com.allendolph.android.govjobs.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


/**
 * Created by allendolph on 1/26/15.
 */
public class Job {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    private String id;


    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    @SerializedName("position_title")
    private String positionTitle;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    @SerializedName("organization_name")
    private String organizationName;

    public String getRateIntervalCode() {
        return rateIntervalCode;
    }

    public void setRateIntervalCode(String rateIntervalCode) {
        this.rateIntervalCode = rateIntervalCode;
    }

    @SerializedName("rate_interval_code")
    private String rateIntervalCode;

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    @SerializedName("minimum")
    private int minimum;

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }

    @SerializedName("maximum")
    private int maximum;

    public java.util.Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @SerializedName("start_date")
    private java.util.Date startDate;

    public java.util.Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @SerializedName("end_date")
    private java.util.Date endDate;

    public String[] getLocations() {
        return locations;
    }

    public void setLocations(String[] locations) {
        this.locations = locations;
    }

    @SerializedName("locations")
    private String[] locations;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @SerializedName("url")
    private String url;


}
