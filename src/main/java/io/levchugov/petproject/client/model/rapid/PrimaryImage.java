package io.levchugov.petproject.client.model.rapid;

import lombok.Data;

@Data
public class PrimaryImage {
    private String id;
    private String url;
    private Caption caption;
}

/*
"primaryImage": {
                "id": "rm720078080",
                "width": 691,
                "height": 1023,
                "url": "https://m.media-amazon.com/images/M/MV5BMTczMzU0MjM1MV5BMl5BanBnXkFtZTcwMjczNzgyNA@@._V1_.jpg",
                "caption": {
                    "plainText": "Philip Seymour Hoffman in Capote (2005)",
                    "__typename": "Markdown"
                },
                "__typename": "Image"
            }
 */