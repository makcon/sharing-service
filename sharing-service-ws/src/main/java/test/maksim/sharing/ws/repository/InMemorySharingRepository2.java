package test.maksim.sharing.ws.repository;

import test.maksim.sharing.ws.repository.domain.Item2;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class InMemorySharingRepository2 implements SharingRepository2 {

    private static final Collection<Item2> STORED_ITEMS = new ArrayList<>();

    @Override
    public void save(Collection<Item2> items) {
        STORED_ITEMS.addAll(items);
    }

    @Override
    public void delete(Collection<Item2> items) {
        STORED_ITEMS.removeAll(items);
    }

    @Override
    public Collection<Item2> getBySheetIds(List<Long> sheetIds) {
        return STORED_ITEMS.stream()
                .filter(it -> sheetIds.contains(it.getSheetId()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item2> getByShareeIdAndCellIds(Long shareeId,
                                                     List<Long> cellIds) {
        return STORED_ITEMS.stream()
                .filter(it -> it.getShareeId() == shareeId)
                .filter(it -> cellIds.contains(it.getCellId()))
                .collect(Collectors.toList());
    }
}
