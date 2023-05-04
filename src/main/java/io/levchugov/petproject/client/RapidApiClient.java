package io.levchugov.petproject.client;

import io.levchugov.petproject.client.model.rapid.RapidMovieResponse;
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
public class RapidApiClient {

    private final RestTemplate restTemplate;

    public RapidMovieResponse findMovie(String movie) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.set("X-RapidAPI-Key", "3aa197ef25msh1637dd7c0bea1d7p153613jsn4a6c0e8d16a8");
        headers.set("X-RapidAPI-Host", "moviesdatabase.p.rapidapi.com");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<RapidMovieResponse> responseEntity = restTemplate.exchange(
                "https://moviesdatabase.p.rapidapi.com/titles/search/title/" + movie + "?titleType=movie",
                HttpMethod.GET,
                entity,
                RapidMovieResponse.class);

        return responseEntity.getBody();
    }

}
