package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharedDocument;
import test.maksim.sharing.ws.repository.domain.StoredSharedItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.*;

@Component
public class SharedDocumentBuilder {

    public Optional<SharedDocument> build(Set<StoredSharedItem> items) {
        return items.isEmpty()
                ? Optional.empty()
                : Optional.of(doBuild(items));
    }

    private SharedDocument doBuild(Set<StoredSharedItem> items) {
        var sharedItem = items.iterator().next();
        var builder = SharedDocument.builder()
                .documentId(sharedItem.getDocumentId());

        Map<Long, List<Long>> sheetCellIds = items.size() == 1 && sharedItem.getSheetId() == null
                ? Map.of()
                : buildSheetCellIds(items);

        return builder
                .sheetCellIds(sheetCellIds)
                .build();
    }

    private static Map<Long, List<Long>> buildSheetCellIds(Set<StoredSharedItem> items) {
        return items.stream()
                .filter(it -> it.getSheetId() != null)
                .collect(groupingBy(StoredSharedItem::getSheetId, mapping(it -> it, toList())))
                .entrySet()
                .stream()
                .collect(toMap(Map.Entry::getKey, it -> resolveCells(it.getValue())));
    }

    private static List<Long> resolveCells(List<StoredSharedItem> items) {
        return items.size() == 1 && items.get(0).getCellId() == null
                ? List.of()
                : items.stream()
                    .map(StoredSharedItem::getCellId)
                    .collect(toList());
    }
}
