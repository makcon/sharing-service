package test.maksim.sharing.ws.repository;

import test.maksim.sharing.ws.repository.domain.Item2;

import java.util.Collection;
import java.util.List;

public interface SharingRepository2 {

    void save(Collection<Item2> items);

    void delete(Collection<Item2> items);

    Collection<Item2> getBySheetIds(List<Long> sheetIds);

    Collection<Item2> getByShareeIdAndCellIds(Long shareeId,
                                              List<Long> cellIds);
}
