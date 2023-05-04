package io.levchugov.petproject.client.model.rapid;

import lombok.Data;

@Data
public class TitleType {

    private String text;
    private String id;
    private Boolean isSeries;
    private Boolean isEpisode;

}
