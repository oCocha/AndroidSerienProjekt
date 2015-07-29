package com.serien.android.androidserienprojekt.domain;

/**
 * Created by oCocha on 29.07.2015.
 */
public class SeriesItem {

    private String name;
    private String year;
    private String actors;
    private String rating;
    private String plot;
    private String imgPath;

    //erstellt ein neues SeriesItem
    public SeriesItem(String name, String year, String actors, String rating, String plot, String imgPath) {
        this.name = name;
        this.year = year;
        this.actors = actors;
        this.rating = rating;
        this.plot = plot;
        this.imgPath = imgPath;
    }

    public String getName() {
        return name;
    }

    public String getYear() {
        return year;
    }

    public String getActors() {
        return actors;
    }

    public String getRating() {
        return rating;
    }

    public String getPlot() {
        return plot;
    }

    public String getImgPath() {
        return imgPath;
    }

}
