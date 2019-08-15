package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.repository.domain.SharedItem;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SharedItemsSheetBuilder implements SharedItemsBuilder {

    @Override
    public boolean isApplicable(SharingRequest request) {
        return !request.getSheetCellIds().isEmpty();
    }

    @Override
    public Stream<SharedItem> build(SharingRequest request) {
        return request.getSheetCellIds().entrySet().stream()
                .filter(it -> it.getValue().isEmpty())
                .flatMap(it -> buildSheet(it.getKey(), request));
    }

    private Stream<SharedItem> buildSheet(Long sheetId,
                                          SharingRequest request) {
        return request.getRecipientShareeIds().stream()
                .map(it -> buildItem(sheetId, request, it));
    }

    private SharedItem buildItem(Long sheetId,
                                 SharingRequest request,
                                 Long shareeId) {
        return SharedItem.builder()
                .shareeId(shareeId)
                .documentId(request.getDocumentId())
                .sheetId(sheetId)
                .build();
    }
}
