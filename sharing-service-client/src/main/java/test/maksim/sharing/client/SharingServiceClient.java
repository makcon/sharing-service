package test.maksim.sharing.client;

import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.common.dto.SharingRequest;

import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public interface SharingServiceClient {

    CompletableFuture<HttpResponse<Void>> share(SharingRequest request);

    CompletableFuture<HttpResponse<Void>> unshare(SharingRequest request);

    CompletableFuture<Optional<SharedDocument>> load(String documentId,
                                                     Set<Long> shareeIds);
}
