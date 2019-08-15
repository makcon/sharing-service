package test.maksim.sharing.common.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class SharingRequest {

    private Set<Long> recipientShareeIds;
    private String documentId;
    private Map<Long, List<Long>> sheetCellIds = Map.of();
}
