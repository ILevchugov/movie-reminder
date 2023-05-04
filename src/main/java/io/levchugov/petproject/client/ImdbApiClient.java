package io.levchugov.petproject.client;

import io.levchugov.petproject.client.model.imdb.SearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImdbApiClient {

    private final RestTemplate restTemplate;

    public SearchResponse findMovie(String movie) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<SearchResponse> responseEntity = restTemplate.exchange(
                "https://imdb-api.com/en/API/SearchMovie/k_29pc2hsr/" + movie,
                HttpMethod.GET,
                entity,
                SearchResponse.class);

        return responseEntity.getBody();
    }
}
