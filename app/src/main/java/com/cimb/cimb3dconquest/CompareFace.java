package com.cimb.cimb3dconquest;

import com.amazonaws.regions.Region;

public class CompareFace {
    private boolean result;
    private String pic1;
    private String pic2;
    private String bucket;
    private Region region;

    public CompareFace(boolean result, String pic1, String pic2, String bucket) {
        this.result = result;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.bucket = bucket;
    }

    public CompareFace(boolean result, String pic1, String pic2, String bucket, Region region) {
        this.result = result;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.bucket = bucket;
        this.region = region;
    }


    public void setResult(boolean result) {
        this.result = result;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public boolean isResult() {
        return result;
    }

    public String getPic1() {
        return pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public String getBucket() {
        return bucket;
    }

    public Region getRegion() {
        return region;
    }
}
