package com.example.kristijan.sharemeal;

/**
 * Created by kristijan on 13/05/16.
 */
public class Event {
    private String meal;
    private String locationAddress;
    private String latitude;
    private String longitude;
    private int personLimit;

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getPersonLimit() {
        return personLimit;
    }

    public void setPersonLimit(int personLimit) {
        this.personLimit = personLimit;
    }

    public Event(String meal, String locationAddress,String latitude, String longitude, int personLimit) {
        this.meal = meal;
        this.locationAddress = locationAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.personLimit = personLimit;
    }
}
