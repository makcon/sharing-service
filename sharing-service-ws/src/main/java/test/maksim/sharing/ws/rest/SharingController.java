package test.maksim.sharing.ws.rest;

import test.maksim.sharing.common.constants.Endpoints;
import test.maksim.sharing.common.constants.RequestParams;
import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.service.SharingLoadService;
import test.maksim.sharing.ws.service.SharingUpdateService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@Slf4j
@Api
public class SharingController {

    private final AsyncListenableTaskExecutor serviceExecutor;
    private final SharingUpdateService sharingUpdateService;
    private final SharingLoadService sharingLoadService;

    @PostMapping(Endpoints.SHARE)
    public void share(@RequestBody SharingRequest request) {
        log.info("Received sharing request: {}", request);
        serviceExecutor.submitListenable(() -> sharingUpdateService.share(request));
    }

    @PostMapping(Endpoints.UNSHARE)
    public void unshare(@RequestBody SharingRequest request) {
        log.info("Received unsharing request: {}", request);
        serviceExecutor.submitListenable(() -> sharingUpdateService.unshare(request));
    }

    @GetMapping(Endpoints.LOAD)
    public ListenableFuture<Optional<SharedDocument>> load(@RequestParam(RequestParams.DOCUMENT_ID) String documentId,
                                                           @RequestParam(RequestParams.SHAREE_IDS) Set<Long> shareeIds) {
        log.info("Received request to load: {} for sharee: {}", documentId, shareeIds);
        return serviceExecutor.submitListenable(() -> sharingLoadService.load(documentId, shareeIds));
    }
}
