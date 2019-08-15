package test.maksim.sharing.common.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SharedDocument {

    private final String documentId;
    private final Map<Long, List<Long>> sheetCellIds;
}
