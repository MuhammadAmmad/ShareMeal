package com.example.kristijan.sharemeal;

/**
 * Created by kristijan on 13/05/16.
 */
public class Event {
    private String meal;
    private String locationAddress;
    private String locationCoordinates;
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

    public String getLocationCoordinates() {
        return locationCoordinates;
    }

    public void setLocationCoordinates(String locationCoordinates) {
        this.locationCoordinates = locationCoordinates;
    }

    public int getPersonLimit() {
        return personLimit;
    }

    public void setPersonLimit(int personLimit) {
        this.personLimit = personLimit;
    }

    public Event(String meal, String locationAddress, String locationCoordinates, int personLimit) {
        this.meal = meal;
        this.locationAddress = locationAddress;
        this.locationCoordinates = locationCoordinates;
        this.personLimit = personLimit;
    }
}
