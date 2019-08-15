package test.maksim.sharing.ws.repository;

import test.maksim.sharing.ws.repository.domain.SharedItem;
import test.maksim.sharing.ws.repository.domain.StoredSharedItem;

import java.util.Collection;
import java.util.Set;

public interface SharingRepository {

    void save(Collection<SharedItem> items);

    void deleteDocument(String documentId,
                        Collection<Long> shareeIds);

    void deleteSheets(String documentId,
                      Collection<Long> sheetIds,
                      Collection<Long> shareeIds);

    void deleteCells(String documentId,
                     Collection<Long> cellIds,
                     Collection<Long> shareeIds);

    Set<StoredSharedItem> load(String documentId,
                               Collection<Long> shareeIds);
}
