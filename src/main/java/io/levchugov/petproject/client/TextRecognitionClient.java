package io.levchugov.petproject.client;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
public class TextRecognitionClient {

    private static final String SERVICE_MP3_RECOGNITION_URL
            = "http://pet-recognition-service.ap-southeast-1.elasticbeanstalk.com/recognition/audio/mp3";

    private final OkHttpClient client;

    public String recognize(File file) {

        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("file","16838kek2.mp3",
                        RequestBody.create(MediaType.parse("application/octet-stream"),
                                file))
                .build();

        Request request = new Request.Builder()
                .url(SERVICE_MP3_RECOGNITION_URL)
                .method("POST", body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            return null;
        }
    }
}
