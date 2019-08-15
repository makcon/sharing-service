package test.maksim.sharing.ws.repository.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StoredSharedItem {

    private final String documentId;
    private final Long sheetId;
    private final Long cellId;
}
