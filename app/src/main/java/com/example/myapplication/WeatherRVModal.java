package com.example.myapplication;

public class WeatherRVModal {
    private String  time;
    private String icon;
    private String temperature;
    private String windspeed;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public WeatherRVModal(String time, String icon, String temperature, String windspeed) {
        this.time = time;
        this.icon = icon;
        this.temperature = temperature;
        this.windspeed = windspeed;
    }
}
