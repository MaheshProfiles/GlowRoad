package com.glowroad.photosexample.entites;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Mahesh Kumar on 8/14/2018.
 */
public class ContactDetails {
    @SerializedName("photos")
    private Photos photos;
    @SerializedName("stat")
    private String stat;

    public Photos getPhotos() {
        return photos;
    }

    public void setPhotos(Photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
