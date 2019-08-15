package test.maksim.sharing.ws.service;

import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.ws.builder.SharedDocumentBuilder;
import test.maksim.sharing.ws.repository.SharingRepository;
import test.maksim.sharing.ws.repository.domain.StoredSharedItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SharingLoadService {

    private final SharingRepository sharingRepository;
    private final SharedDocumentBuilder sharedDocumentBuilder;

    public Optional<SharedDocument> load(String documentId,
                                         Set<Long> shareeIds) {
        Set<StoredSharedItem> items = sharingRepository.load(documentId, shareeIds);

        return sharedDocumentBuilder.build(items);
    }
}
