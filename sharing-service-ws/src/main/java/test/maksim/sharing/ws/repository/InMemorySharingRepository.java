package test.maksim.sharing.ws.repository;

import test.maksim.sharing.ws.repository.domain.SharedItem;
import test.maksim.sharing.ws.repository.domain.StoredSharedItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

@Repository
@Slf4j
public class InMemorySharingRepository implements SharingRepository {

    private final Set<SharedItem> storedItems = new HashSet<>();

    @Override
    public void save(Collection<SharedItem> items) {
        log.debug("Saving items: {}", items);
        storedItems.addAll(items);
        log.debug("StoredItems: {}", storedItems);
    }

    @Override
    public void deleteDocument(String documentId,
                               Collection<Long> shareeIds) {
        removeIf(documentId, shareeIds, it -> true);
    }

    @Override
    public void deleteSheets(String documentId,
                             Collection<Long> sheetIds,
                             Collection<Long> shareeIds) {
        removeIf(documentId, shareeIds, it -> sheetIds.contains(it.getSheetId()));
    }

    @Override
    public void deleteCells(String documentId,
                            Collection<Long> cellIds,
                            Collection<Long> shareeIds) {
        removeIf(documentId, shareeIds, it -> cellIds.contains(it.getCellId()));
    }

    @Override
    public Set<StoredSharedItem> load(String documentId,
                                      Collection<Long> shareeIds) {
        return storedItems.stream()
                .filter(it -> shareeIds.contains(it.getShareeId()))
                .filter(it -> Objects.equals(documentId, it.getDocumentId()))
                .collect(toSet())
                .stream()
                .map(this::createStoredItem)
                .collect(toSet());
    }

    private StoredSharedItem createStoredItem(SharedItem sharedItem) {
        return StoredSharedItem.builder()
                .documentId(sharedItem.getDocumentId())
                .sheetId(sharedItem.getSheetId())
                .cellId(sharedItem.getCellId())
                .build();
    }

    private void removeIf(String documentId,
                          Collection<Long> shareeIds,
                          Predicate<SharedItem> extraFilter) {
        storedItems.removeIf(
                it -> shareeIds.contains(it.getShareeId())
                        && Objects.equals(it.getDocumentId(), documentId)
                        && extraFilter.test(it)
        );
        log.debug("StoredItems: {}", storedItems);
    }
}
