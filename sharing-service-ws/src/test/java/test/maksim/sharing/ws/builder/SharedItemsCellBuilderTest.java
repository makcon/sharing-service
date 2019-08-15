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

public class SharedItemsCellBuilderTest {

    private static final String DOC_ID = "docId";
    private static final long SHAREE_ID_1 = 1;
    private static final long SHAREE_ID_2 = 2;
    private static final long SHEET_ID_1 = 11;
    private static final long SHEET_ID_2 = 12;
    private static final long CELL_ID_1 = 111;
    private static final long CELL_ID_2 = 112;

    private final SharedItemsCellBuilder builder = new SharedItemsCellBuilder();

    @Test
    public void isApplicable_sheetCellsEmpty_shouldReturnFalse() {
        assertThat(builder.isApplicable(createRequest(Map.of())), Matchers.is(false));
    }

    @Test
    public void isApplicable_sheetCellsNotEmpty_shouldReturnTruw() {
        assertThat(builder.isApplicable(createRequest(Map.of(1L, List.of()))), Matchers.is(true));
    }

    @Test
    public void build_shouldBuildItemsForEachShareeId() {
        Map<Long, List<Long>> sheetCellIds = Map.of(
                SHEET_ID_1, List.of(),
                SHEET_ID_2, List.of(CELL_ID_1, CELL_ID_2)
        );
        List<SharedItem> sharedItems = builder.build(createRequest(sheetCellIds)).collect(toList());

        var expectedItem1 = createSharedItem(SHAREE_ID_1, CELL_ID_1);
        var expectedItem2 = createSharedItem(SHAREE_ID_2, CELL_ID_1);
        var expectedItem3 = createSharedItem(SHAREE_ID_1, CELL_ID_2);
        var expectedItem4 = createSharedItem(SHAREE_ID_2, CELL_ID_2);
        assertThat(sharedItems, containsInAnyOrder(expectedItem1, expectedItem2, expectedItem3, expectedItem4));
    }

    // Util methods

    private SharingRequest createRequest(Map<Long, List<Long>> sheetCellIds) {
        var request = new SharingRequest();
        request.setDocumentId(DOC_ID);
        request.setSheetCellIds(sheetCellIds);
        request.setRecipientShareeIds(Set.of(SHAREE_ID_1, SHAREE_ID_2));

        return request;
    }

    private SharedItem createSharedItem(long shareeId,
                                        long cellId) {
        return SharedItem.builder()
                .documentId(DOC_ID)
                .shareeId(shareeId)
                .sheetId(SHEET_ID_2)
                .cellId(cellId)
                .build();
    }
}