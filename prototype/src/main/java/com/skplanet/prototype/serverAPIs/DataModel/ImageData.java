package com.skplanet.prototype.serverAPIs.DataModel;

/**
 * Created by 1001955 on 3/8/16.
 */
public class ImageData {
    private String buid, thumbnailUrl;
    private String major, minor;

    public ImageData(String buid, String thumbnailUrl, int major, int minor) {
        this.buid = buid;
        this.thumbnailUrl = thumbnailUrl;
        this.major = String.valueOf(major);
        this.minor = String.valueOf(minor);
    }

    public String getBuid() {
        return buid;
    }

    public void setBuid(String title) {
        this.buid = title;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getMinor() {
        return minor;
    }

    public void setMinor(String minor) {
        this.minor = minor;
    }
}
