package test.maksim.sharing.client.impl;

import com.google.gson.Gson;
import test.maksim.sharing.client.SharingServiceClient;
import test.maksim.sharing.common.constants.Endpoints;
import test.maksim.sharing.common.constants.RequestParams;
import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.common.dto.SharingRequest;
import lombok.Builder;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpResponse.BodySubscribers;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.joining;

@Builder
public class SharingServiceClientImpl implements SharingServiceClient {

    private final String serviceUrl;
    private final HttpClient httpClient;
    private final Gson gson;

    @Override
    public CompletableFuture<HttpResponse<Void>> share(SharingRequest request) {
        return executePost(Endpoints.SHARE, request);
    }

    @Override
    public CompletableFuture<HttpResponse<Void>> unshare(SharingRequest request) {
        return executePost(Endpoints.UNSHARE, request);
    }

    @Override
    public CompletableFuture<Optional<SharedDocument>> load(String documentId,
                                                            Set<Long> shareeIds) {
        String shareeIdsAsString = shareeIds.stream()
                .map(Objects::toString)
                .collect(joining(","));
        Map<String, String> params = Map.of(
                RequestParams.DOCUMENT_ID, documentId,
                RequestParams.SHAREE_IDS, shareeIdsAsString
        );

        return executeGet(Endpoints.LOAD, params)
                .thenApply(HttpResponse::body)
                .thenApply(it -> gson.fromJson(it, SharedDocument.class))
                .thenApply(Optional::ofNullable);
    }

    private CompletableFuture<HttpResponse<Void>> executePost(String endpoint,
                                                              Object payload) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(payload)))
                .build();

        return httpClient.sendAsync(request, BodyHandlers.discarding());
    }

    private CompletableFuture<HttpResponse<String>> executeGet(String endpoint,
                                                               Map<String, String> params) {
        HttpRequest request = HttpRequest.newBuilder(URI.create(serviceUrl + endpoint + '?' + buildParamsAsString(params)))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        return httpClient.sendAsync(request, getStringBodyHandler());
    }

    private String buildParamsAsString(Map<String, String> params) {
        return params.entrySet()
                .stream()
                .map(e -> e.getKey() + '=' + URLEncoder.encode(e.getValue(), UTF_8))
                .collect(joining("&"));
    }

    private HttpResponse.BodyHandler<String> getStringBodyHandler() {
        return responseInfo -> BodySubscribers.ofString(UTF_8);
    }
}
