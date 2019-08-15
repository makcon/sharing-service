package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.repository.domain.SharedItem;
import test.maksim.sharing.ws.repository.domain.SharedItem.SharedItemBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SharedItemsBuilderLegacy {

    public List<SharedItem> build(SharingRequest request) {
        return request.getSheetCellIds().isEmpty()
                ? buildForDocument(request)
                : buildMultiple(request);
    }

    private List<SharedItem> buildForDocument(SharingRequest request) {
        return request.getRecipientShareeIds().stream()
                .map(it -> buildBasicFields(it, request.getDocumentId()).build())
                .collect(Collectors.toList());
    }

    private List<SharedItem> buildMultiple(SharingRequest request) {
        return request.getSheetCellIds().entrySet().stream()
                .flatMap(it -> buildForSheet(it, request))
                .collect(Collectors.toList());
    }

    private Stream<SharedItem> buildForSheet(Map.Entry<Long, List<Long>> entry,
                                           SharingRequest request) {

        return entry.getValue().isEmpty()
                ? buildSingleSheet(entry.getKey(), request)
                : buildForCells(entry, request);
    }

    private Stream<SharedItem> buildForCells(Map.Entry<Long, List<Long>> entry,
                                           SharingRequest request) {
        return entry.getValue().stream()
                .flatMap(it -> buildForCell(entry.getKey(), it, request));
    }

    private Stream<SharedItem> buildForCell(Long sheetId,
                                            Long cellId,
                                            SharingRequest request) {
        return request.getRecipientShareeIds().stream()
                .map(it -> buildBasicFields(it, request.getDocumentId()).sheetId(sheetId).cellId(cellId).build());
    }

    private Stream<SharedItem> buildSingleSheet(Long sheetId,
                                              SharingRequest request) {
        return request.getRecipientShareeIds().stream()
                .map(it -> buildBasicFields(it, request.getDocumentId()).sheetId(sheetId).build());

    }

    private SharedItemBuilder buildBasicFields(long shareeId,
                                               String documentId) {
        return SharedItem.builder()
                .shareeId(shareeId)
                .documentId(documentId);
    }
}
