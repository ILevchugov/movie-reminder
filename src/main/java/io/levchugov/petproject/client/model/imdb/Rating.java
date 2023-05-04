package io.levchugov.petproject.client.model.imdb;

import lombok.Data;

@Data
public class Rating {

    private String imDbId;

    private String title;

    private String fullTitle;

    private String type;

    private String year;

    private String imDb;

    private String metacritic;

    private String theMovieDb;

    private String rottenTomatoes;

    private String tV_com;

    private String filmAffinity;
}