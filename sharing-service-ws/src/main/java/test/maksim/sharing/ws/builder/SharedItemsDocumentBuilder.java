package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.repository.domain.SharedItem;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SharedItemsDocumentBuilder implements SharedItemsBuilder {

    @Override
    public boolean isApplicable(SharingRequest request) {
        return request.getSheetCellIds().isEmpty();
    }

    @Override
    public Stream<SharedItem> build(SharingRequest request) {
        return request.getRecipientShareeIds().stream()
                .map(it -> buildItem(it, request));
    }

    private SharedItem buildItem(Long shareeId,
                                 SharingRequest request) {
        return SharedItem.builder()
                .shareeId(shareeId)
                .documentId(request.getDocumentId())
                .build();
    }
}
