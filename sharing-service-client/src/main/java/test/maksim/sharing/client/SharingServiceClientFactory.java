package test.maksim.sharing.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import test.maksim.sharing.client.impl.SharingServiceClientImpl;

import java.net.http.HttpClient;

public class SharingServiceClientFactory {

    public SharingServiceClient defaultClient(String serviceUrl) {
        return SharingServiceClientImpl.builder()
                .gson(createGson())
                .httpClient(HttpClient.newHttpClient())
                .serviceUrl(serviceUrl)
                .build();
    }

    private Gson createGson() {
        return new GsonBuilder().create();
    }
}
