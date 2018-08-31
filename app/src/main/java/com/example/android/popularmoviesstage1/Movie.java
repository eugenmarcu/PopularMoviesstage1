package com.example.android.popularmoviesstage1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private String title;
    private String releaseDate;
    private String vote;
    private String plot;
    private String posterUrl;

    public Movie(String title, String releaseDate, String vote, String plot, String poster){
        this.title = title;
        this.releaseDate = releaseDate;
        this.vote = vote;
        this.plot = plot;
        this.posterUrl = poster;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getVote() {
        return vote;
    }

    public String getPlot() {
        return plot;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel in){
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.vote =  in.readString();
        this.plot = in.readString();
        this.posterUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.title);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.vote);
        parcel.writeString(this.plot);
        parcel.writeString(this.posterUrl);
    }
}

