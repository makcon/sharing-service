package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.repository.domain.SharedItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class SharedItemsCellBuilder implements SharedItemsBuilder {

    @Override
    public boolean isApplicable(SharingRequest request) {
        return !request.getSheetCellIds().isEmpty();
    }

    @Override
    public Stream<SharedItem> build(SharingRequest request) {
        return request.getSheetCellIds().entrySet().stream()
                .flatMap(it -> buildCells(it, request));
    }

    private Stream<SharedItem> buildCells(Map.Entry<Long, List<Long>> entry,
                                          SharingRequest request) {
        return entry.getValue().stream()
                .flatMap(it -> buildCell(entry.getKey(), it, request));
    }

    private Stream<SharedItem> buildCell(Long sheetId,
                                         Long cellId,
                                         SharingRequest request) {
        return request.getRecipientShareeIds().stream()
                .map(it -> buildItem(it, sheetId, cellId, request));
    }

    private SharedItem buildItem(long shareeId,
                                 long sheetId,
                                 long cellId,
                                 SharingRequest request) {
        return SharedItem.builder()
                .shareeId(shareeId)
                .documentId(request.getDocumentId())
                .cellId(cellId)
                .sheetId(sheetId)
                .build();
    }
}
