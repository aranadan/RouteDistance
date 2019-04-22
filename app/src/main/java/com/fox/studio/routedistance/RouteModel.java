package com.fox.studio.routedistance;


import java.util.Date;

import io.realm.RealmObject;

public class RouteModel extends RealmObject {

    private double fromPointLat;
    private double fromPointLon;
    private double toPointLat;
    private double toPointLon;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public double getFromPointLat() {
        return fromPointLat;
    }

    void setFromPointLat(double fromPointLat) {
        this.fromPointLat = fromPointLat;
    }

    public double getFromPointLon() {
        return fromPointLon;
    }

    void setFromPointLon(double fromPointLon) {
        this.fromPointLon = fromPointLon;
    }

    public double getToPointLat() {
        return toPointLat;
    }

    void setToPointLat(double toPointLat) {
        this.toPointLat = toPointLat;
    }

    public double getToPointLon() {
        return toPointLon;
    }

    void setToPointLon(double toPointLon) {
        this.toPointLon = toPointLon;
    }
}
