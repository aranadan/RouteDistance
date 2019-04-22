package com.fox.studio.routedistance;



import io.realm.RealmObject;
import io.realm.annotations.Required;

public class RouteModel extends RealmObject {

    private double fromPointLat;
    private double fromPointLon;
    private double toPointLat;
    private double toPointLon;

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
