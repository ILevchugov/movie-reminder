package io.levchugov.petproject.client.model.rapid;

import lombok.Data;

@Data
public class RapidMovieResult {

    private String id;
    private PrimaryImage primaryImage;
    private TitleType titleType;
    private TitleText titleText;
    private ReleaseYear releaseYear;


}