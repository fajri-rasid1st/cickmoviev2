package com.example.cickmoviev2.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TvShow {
    @SerializedName("name")
    private String title;

    @SerializedName("first_air_date")
    private String firstAirDate;

    @SerializedName("last_air_date")
    private String lastAirDate;

    @SerializedName("number_of_episodes")
    private String totalEpisode;

    @SerializedName("number_of_seasons")
    private String totalSeason;

    @SerializedName("vote_average")
    private String voteAverage;

    @SerializedName("episode_run_time")
    private String[] runtime;

    private String overview;

    @SerializedName("poster_path")
    private String posterUrl;

    @SerializedName("backdrop_path")
    private String backdropUrl;

    @SerializedName("vote_count")
    private String voteCount;

    @SerializedName("genres")
    @Expose
    private List<Genres> genres;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstAirDate() {
        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd",
                new Locale("en", "US"));
        SimpleDateFormat resultFormat = new SimpleDateFormat("MMM dd, yyyy",
                new Locale("en", "US"));
        Date date = null;

        try {
            date = currentFormat.parse(firstAirDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date != null ? resultFormat.format(date) : "None";
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public String getLastAirDate() {
        SimpleDateFormat currentFormat = new SimpleDateFormat("yyyy-MM-dd",
                new Locale("en", "US"));
        SimpleDateFormat resultFormat = new SimpleDateFormat("MMM dd, yyyy",
                new Locale("en", "US"));
        Date date = null;

        try {
            date = currentFormat.parse(lastAirDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date != null ? resultFormat.format(date) : "None";
    }

    public void setLastAirDate(String lastAirDate) {
        this.lastAirDate = lastAirDate;
    }

    public String getFirstAndLastAirDate() {
        return String.format("%s to %s", getFirstAirDate(), getLastAirDate());
    }

    public String getTotalEpisode() {
        return totalEpisode;
    }

    public void setTotalEpisode(String totalEpisode) {
        this.totalEpisode = totalEpisode;
    }

    public String getTotalSeason() {
        return totalSeason;
    }

    public void setTotalSeason(String totalSeason) {
        this.totalSeason = totalSeason;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getRuntime() {
        return runtime.length > 0 ? String.format("%s Mins Per Ep.", runtime[0]) : "None";
    }

    public void setRuntime(String[] runtime) {
        this.runtime = runtime;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getBackdropUrl() {
        return backdropUrl;
    }

    public void setBackdropUrl(String backdropUrl) {
        this.backdropUrl = backdropUrl;
    }

    public String getVoteCount() {
        return String.format("%s Votes", voteCount);
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public List<Genres> getGenres() {
        if (genres.size() == 0) {
            genres.add(new Genres("None"));
        }

        return genres;
    }

    public void setGenres(List<Genres> genres) {
        this.genres = genres;
    }
}
