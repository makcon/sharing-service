package test.maksim.sharing.ws.builder;

import test.maksim.sharing.common.dto.SharingRequest;
import test.maksim.sharing.ws.repository.domain.SharedItem;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class SharedItemsDocumentBuilderTest {

    private static final String DOC_ID = "docId";
    private static final long SHAREE_ID_1 = 1;
    private static final long SHAREE_ID_2 = 2;

    private final SharedItemsDocumentBuilder builder = new SharedItemsDocumentBuilder();

    @Test
    public void isApplicable_sheetCellsEmpty_shouldReturnTrue() {
        assertThat(builder.isApplicable(createRequest(Map.of())), Matchers.is(true));
    }

    @Test
    public void isApplicable_sheetCellsNotEmpty_shouldReturnFalse() {
        assertThat(builder.isApplicable(createRequest(Map.of(1L, List.of()))), Matchers.is(false));
    }

    @Test
    public void build_shouldBuildItemsForEachShareeId() {
        List<SharedItem> sharedItems = builder.build(createRequest(Map.of())).collect(toList());

        assertThat(sharedItems, containsInAnyOrder(createSharedItem(SHAREE_ID_1), createSharedItem(SHAREE_ID_2)));
    }

    // Util methods

    private SharingRequest createRequest(Map<Long, List<Long>> sheetCellIds) {
        var request = new SharingRequest();
        request.setDocumentId(DOC_ID);
        request.setSheetCellIds(sheetCellIds);
        request.setRecipientShareeIds(Set.of(SHAREE_ID_1, SHAREE_ID_2));

        return request;
    }

    private SharedItem createSharedItem(long shareeId) {
        return SharedItem.builder()
                .documentId(DOC_ID)
                .shareeId(shareeId)
                .build();
    }
}