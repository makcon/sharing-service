package test.maksim.sharing.ws.service;

import test.maksim.sharing.ws.repository.SharingRepository2;
import test.maksim.sharing.ws.repository.domain.Item2;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final SharingRepository2 sharingRepository2;

    Collection<Long> getPermittedCellIds(long ownerShareeId,
                                         Map<Long, List<Long>> cells) {
        List<Long> cellIdsToCheck = cells.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        return sharingRepository2.getByShareeIdAndCellIds(ownerShareeId, cellIdsToCheck).stream()
                .map(Item2::getCellId)
                .collect(Collectors.toList());
    }

//    Map<Long, List<Long>> filterRestri
}
