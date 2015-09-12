package com.serien.android.androidserienprojekt.domain;

import java.io.Serializable;

/**
 * Created by oCocha on 29.07.2015.
 */

//Dateiformat zum Abspeichern von Serien
public class SeriesItem implements Serializable {

    private String name;
    private String year;
    private String actors;
    private String rating;
    private String plot;
    private String imgPath;
    private String imdbID;
    private String watched;


    //erstellt ein neues SeriesItem
    public SeriesItem(String name, String year, String actors, String rating, String plot, String imgPath, String imdbID, String watched) {
        this.name = name;
        this.year = year;
        this.actors = actors;
        this.rating = rating;
        this.plot = plot;
        this.imgPath = imgPath;
        this.imdbID = imdbID;
        this.watched = watched;
    }

    public String getWatched() {
        return watched;
    }


    public String getImdbID() {
        return imdbID;
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
