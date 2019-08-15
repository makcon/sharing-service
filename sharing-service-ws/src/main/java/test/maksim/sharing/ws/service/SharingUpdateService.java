package test.maksim.sharing.ws.service;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.builder.SharedItemsBuilder;
import test.maksim.sharing.ws.repository.SharingRepository;
import test.maksim.sharing.ws.repository.domain.SharedItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class SharingUpdateService {

    private final SharingRepository sharingRepository;
    private final List<SharedItemsBuilder> sharedItemsBuilders;

    public void share(SharingRequest request) {
        List<SharedItem> sharedItems = sharedItemsBuilders.stream()
                .filter(it -> it.isApplicable(request))
                .flatMap(it -> it.build(request))
                .collect(toList());

        sharingRepository.save(sharedItems);
    }

    public void unshare(SharingRequest request) {
        if (request.getSheetCellIds().isEmpty()) {
            log.debug("Unsharing whole document: {}", request.getDocumentId());
            sharingRepository.deleteDocument(request.getDocumentId(), request.getRecipientShareeIds());
        } else {
            unshareSheets(request);
            unshareCells(request);
        }
    }

    private void unshareCells(SharingRequest request) {
        List<Long> cellIds = getCellIdsToDelete(request);

        if (!cellIds.isEmpty()) {
            log.debug("Unsharing cellIds: {}", cellIds);
            sharingRepository.deleteCells(request.getDocumentId(), cellIds, request.getRecipientShareeIds());
        }
    }

    private void unshareSheets(SharingRequest request) {
        List<Long> sheetIds = getSheetIdsToDelete(request);

        if (!sheetIds.isEmpty()) {
            log.debug("Unsharing sheetIds: {}", sheetIds);
            sharingRepository.deleteSheets(request.getDocumentId(), sheetIds, request.getRecipientShareeIds());
        }
    }

    private List<Long> getSheetIdsToDelete(SharingRequest request) {
        return request.getSheetCellIds().entrySet().stream()
                .filter(it -> it.getValue().isEmpty())
                .map(Map.Entry::getKey)
                .collect(toList());
    }

    private List<Long> getCellIdsToDelete(SharingRequest request) {
        return request.getSheetCellIds().values().stream()
                .flatMap(Collection::stream)
                .collect(toList());
    }
}
